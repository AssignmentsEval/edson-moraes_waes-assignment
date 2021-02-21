package com.waes.diffservice.services;

import com.waes.diffservice.ApplicationErrorEnum;
import com.waes.diffservice.dto.Difference;
import com.waes.diffservice.dto.JsonDiff;
import com.waes.diffservice.dto.StringDiff;
import com.waes.diffservice.dto.StringDifference;
import com.waes.diffservice.enitities.DiffData;
import com.waes.diffservice.exception.InputException;
import com.waes.diffservice.repositories.DiffRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DiffService {

    public static final String EMPTY_JSON = "{}";
    public static final String DIFF_CACHE = "diffs";

    private final DiffRepository diffRepository;
    private final Base64.Decoder decoder;

    /**
     * Helper method to save the leftJson of a given Diff
     * @param diffId The id of the diff to be saved
     * @param leftText The text to be saved
     * @return The current state of the object
     */
    @CacheEvict(value = DIFF_CACHE, key = "#diffId")
    public DiffData saveLeftJson(Long diffId, String leftText) {
        return saveJson(diffId, leftText, false);
    }

    /**
     * Helper method to save the rightJson of a given Diff
     * @param diffId The id of the diff to be saved
     * @param rightText The text to be saved
     * @return The current state of the object
     */
    @CacheEvict(value = DIFF_CACHE, key = "#diffId")
    public DiffData saveRightJson(Long diffId, String rightText) {
        return saveJson(diffId, rightText, true);
    }

    /**
     * Saves the given text, if valid, to the given position, left or right
     * @param diffId The id of the diff to be saved
     * @param text The text to be saved
     * @param right <code>True</code> if it's the rightJson that must be saved. False otherwise
     * @return The current state of the object
     */
    private DiffData saveJson(Long diffId, String text, boolean right) {
        String json = validateInput(text);

        Optional<DiffData> diff = diffRepository.findById(diffId);

        if (diff.isPresent()) {
            if (right) {
                diff.get().setRightJson(json);
            } else {
                diff.get().setLeftJson(json);
            }
            return diffRepository.save(diff.get());
        } else {
            return right
                    ? diffRepository.save(DiffData.builder().id(diffId).rightJson(json).build())
                    : diffRepository.save(DiffData.builder().id(diffId).leftJson(json).build());
        }
    }

    /**
     * Validate that the input String is a valid Base64 String and a valid Json Object.
     * To maintain consistency between comparisons the string is minified
     * @param input The input string
     * @return A valid, minified Json String
     */
    private String validateInput(String input) {
        try {
            String json = new String(decoder.decode(input));
            return Json.createReader(new StringReader(json)).read().toString();
        } catch (IllegalArgumentException e) {
            throw new InputException(e, ApplicationErrorEnum.INVALID_BASE64, e.getMessage());
        } catch (JsonParsingException e) {
            throw new InputException(e, ApplicationErrorEnum.INVALID_JSON, e.getMessage());
        }
    }


    /**
     * Evaluated the differences between the JSON objects.
     *
     * @param diffId The id of the diff
     * @return
     */
    @Cacheable(value = DIFF_CACHE, key = "#diffId")
    public Difference diff(Long diffId) {
        String leftJson = EMPTY_JSON;
        String rightJson = EMPTY_JSON;

        Optional<DiffData> optDiffData = diffRepository.findById(diffId);
        if (optDiffData.isPresent()) {
            DiffData diffData = optDiffData.get();
            leftJson = diffData.getLeftJson() == null ? EMPTY_JSON : diffData.getLeftJson();
            rightJson = diffData.getRightJson() == null ? EMPTY_JSON : diffData.getRightJson();
        }

        StringDiff stringDiff = stringDiff(leftJson, rightJson);
        JsonDiff jsonDiff = jsonDiff(leftJson, rightJson);

        return new Difference(diffId, leftJson, rightJson, stringDiff, jsonDiff);
    }

    /**
     * Calculates the difference between the input Json's in the form
     * of a Json Patch in accordance to <a href="https://tools.ietf.org/html/rfc6902">RFC-6902</a>.
     *
     * @param jsonSource The source json, from which the patch will be generated
     * @param jsonTarget The target json.
     * @return Sets {@link JsonDiff#isEqual()} to <code>true</code> when both strings are equal.
     * Otherwise sets {@link JsonDiff#isEqual()} to <code>false</code> and {@link JsonDiff#getJsonDifferences()}
     * with all the operations required to transform jsonSource to jsonTarget
     */
    private JsonDiff jsonDiff(String jsonSource, String jsonTarget) {
        JsonReader sourceReader = Json.createReader(new StringReader(jsonSource));
        JsonReader targetReader = Json.createReader(new StringReader(jsonTarget));

        JsonObject source = sourceReader.readObject();
        JsonObject target = targetReader.readObject();

        JsonPatch diff = Json.createDiff(source, target);

        return new JsonDiff(diff.toJsonArray().size() == 0, diff);
    }

    /**
     * Calculates the differences between the inputs Strings
     *
     * @param a Input string 'a'
     * @param b Input string 'b'
     * @return Sets {@link StringDiff#isEqual()} to <code>true</code> when both strings are equal.
     * Otherwise sets {@link StringDiff#isEqual()} to <code>false</code> and a list of offsets and lengths
     * identifying the differences
     */
    private StringDiff stringDiff(String a, String b) {
        boolean equal = true;
        int initialIndex = -1, finalIndex = -1;
        List<StringDifference> differenceList = new ArrayList<>();

        if (a.length() != b.length()) {
            return new StringDiff(false, differenceList);
        }

        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                equal = false;
                if (initialIndex == -1) initialIndex = i;
                finalIndex = i;
            } else {
                if (initialIndex != -1) {
                    differenceList.add(new StringDifference(initialIndex, finalIndex - initialIndex + 1));
                    initialIndex = -1;
                    finalIndex = -1;
                }
            }
        }

        /*
        Since all valid JSON ends with a '}', there should be no need to check differences in the last character
        if (initialIndex != -1) {
            differenceList.add(new StringDifference(initialIndex, finalIndex - initialIndex + 1));
        }*/

        return new StringDiff(equal, differenceList);
    }
}
