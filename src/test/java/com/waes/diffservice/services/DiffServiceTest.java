package com.waes.diffservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.diffservice.ApplicationErrorEnum;
import com.waes.diffservice.dto.Difference;
import com.waes.diffservice.dto.JsonDifference;
import com.waes.diffservice.enitities.DiffData;
import com.waes.diffservice.exception.InvalidInputException;
import com.waes.diffservice.repositories.DiffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiffServiceTest {

    private static final long DIFF_ID = 1L;
    private static final long INVALID_DIFF_ID = 999L;

    public static final String CAUSE_STRING = "TEST";

    private static final String INVALID_JSON = "Invalid Json";
    private static final String JSON_1 = "{\"name\":\"Ebony\",\"species\":\"Diospyros ebenum\"}";
    private static final String JSON_1_WITH_TYPO = "{\"name\":\"Ebony\",\"species\":\"Diospyros ebanun\"}";
    private static final String JSON_2 = "{\"name\":\"Oak\",\"species\":\"Quercus robur\"}";
    private static final String JSON_3 = "{\"name\":\"Pau-brasil\",\"species\":\"Paubrasilia echinata\"}";
    private static final String JSON_3_WITH_WHITESPACE = "{\"name\" : \"Pau-brasil\",\"species\" : \"Paubrasilia echinata\"}";


    private DiffData DIFF_DATA_NOT_EQUAL;
    private DiffData DIFF_DATA_NOT_EQUAL_WITH_TYPO;
    private DiffData DIFF_DATA_EQUAL;
    private DiffData DIFF_DATA_ONLY_JSON_EQUAL;
    private DiffData DIFF_DATA_LEFT_ONLY;
    private DiffData DIFF_DATA_RIGHT_ONLY;

    @Mock
    private DiffRepository diffRepository;

    @Mock
    private Base64.Decoder decoder;

    @InjectMocks
    private DiffService diffService;

    @Captor
    ArgumentCaptor<DiffData> captor;

    @BeforeEach
    void setUp() {
        DIFF_DATA_NOT_EQUAL = DiffData.builder().id(DIFF_ID).leftJson(JSON_1).rightJson(JSON_2).build();
        DIFF_DATA_NOT_EQUAL_WITH_TYPO = DiffData.builder().id(DIFF_ID).leftJson(JSON_1).rightJson(JSON_1_WITH_TYPO).build();
        DIFF_DATA_EQUAL = DiffData.builder().id(DIFF_ID).leftJson(JSON_3).rightJson(JSON_3).build();
        DIFF_DATA_ONLY_JSON_EQUAL = DiffData.builder().id(DIFF_ID).leftJson(JSON_3).rightJson(JSON_3_WITH_WHITESPACE).build();
        DIFF_DATA_LEFT_ONLY = DiffData.builder().id(DIFF_ID).leftJson(JSON_1).build();
        DIFF_DATA_RIGHT_ONLY = DiffData.builder().id(DIFF_ID).rightJson(JSON_1).build();
    }

    @Test
    void createNewEntryWhenSavingLeftJsonWhenEntryNotExist() {
        when(diffRepository.findById(DIFF_ID)).thenReturn(Optional.empty());
        when(diffRepository.save(DIFF_DATA_LEFT_ONLY)).thenReturn(DIFF_DATA_LEFT_ONLY);
        when(decoder.decode(anyString())).thenReturn(JSON_1.getBytes(StandardCharsets.UTF_8));

        DiffData diffData = diffService.saveLeftJson(DIFF_ID, JSON_1);
        verify(diffRepository).save(captor.capture());

        assertEquals(DIFF_ID, captor.getValue().getId());
        assertEquals(JSON_1, captor.getValue().getLeftJson());
        assertNull(captor.getValue().getRightJson());

        assertEquals(DIFF_DATA_LEFT_ONLY, diffData);
    }

    @Test
    void createNewEntryWhenSavingRightJsonWhenEntryNotExist() {
        when(diffRepository.findById(DIFF_ID)).thenReturn(Optional.empty());
        when(diffRepository.save(DIFF_DATA_RIGHT_ONLY)).thenReturn(DIFF_DATA_RIGHT_ONLY);
        when(decoder.decode(anyString())).thenReturn(JSON_1.getBytes(StandardCharsets.UTF_8));

        DiffData diffData = diffService.saveRightJson(DIFF_ID, JSON_1);
        verify(diffRepository).save(captor.capture());

        assertEquals(DIFF_ID, captor.getValue().getId());
        assertEquals(JSON_1, captor.getValue().getRightJson());
        assertNull(captor.getValue().getLeftJson());

        assertEquals(DIFF_DATA_RIGHT_ONLY, diffData);
    }

    @Test
    void updateEntryWhenSavingLeftJsonAndEntryExists() throws IOException {
        when(diffRepository.findById(DIFF_ID)).thenReturn(Optional.of(DIFF_DATA_NOT_EQUAL));
        when(decoder.decode(anyString())).thenReturn(JSON_3.getBytes(StandardCharsets.UTF_8));
        DiffData diffDataClone = deepCopyDiffData(DIFF_DATA_NOT_EQUAL);
        diffDataClone.setLeftJson(JSON_3);
        when(diffRepository.save(diffDataClone)).thenReturn(diffDataClone);

        DiffData diffData = diffService.saveLeftJson(DIFF_ID, JSON_3);
        verify(diffRepository).save(captor.capture());

        assertEquals(DIFF_ID, captor.getValue().getId());
        assertEquals(JSON_3, captor.getValue().getLeftJson());
        assertEquals(JSON_2, captor.getValue().getRightJson());

        assertEquals(diffDataClone, diffData);
    }

    @Test
    void updateEntryWhenSavingRightJsonAndEntryExists() throws IOException {
        when(diffRepository.findById(DIFF_ID)).thenReturn(Optional.of(DIFF_DATA_NOT_EQUAL));
        when(decoder.decode(anyString())).thenReturn(JSON_3.getBytes(StandardCharsets.UTF_8));
        DiffData diffDataClone = deepCopyDiffData(DIFF_DATA_NOT_EQUAL);
        diffDataClone.setRightJson(JSON_3);
        when(diffRepository.save(diffDataClone)).thenReturn(diffDataClone);

        DiffData diffData = diffService.saveRightJson(DIFF_ID, JSON_3);
        verify(diffRepository).save(captor.capture());

        assertEquals(DIFF_ID, captor.getValue().getId());
        assertEquals(JSON_1, captor.getValue().getLeftJson());
        assertEquals(JSON_3, captor.getValue().getRightJson());

        assertEquals(diffDataClone, diffData);
    }

    @Test
    void returnsEqualWhenDiffIdDoesNotExist() {
        Difference diff = diffService.diff(INVALID_DIFF_ID);

        assertEquals(INVALID_DIFF_ID, diff.getDiffID());

        assertNotNull(diff.getJsonDiff());
        assertNotNull(diff.getStringDiff());
        assertFalse(diff.getStringDiff().isDifferent());
        assertFalse(diff.getJsonDiff().isDifferent());
        assertTrue(diff.getStringDiff().getStringDifferences().isEmpty());
        assertTrue(diff.getJsonDiff().getJsonDifferences().isEmpty());
    }

    @Test
    void returnsNotEqualWhenOnlyLeftJsonIsSet() {
        when(diffRepository.findById(DIFF_ID)).thenReturn(Optional.of(DIFF_DATA_LEFT_ONLY));

        Difference diff = diffService.diff(DIFF_ID);

        assertEquals(DIFF_ID, diff.getDiffID());

        assertNotNull(diff.getJsonDiff());
        assertNotNull(diff.getStringDiff());
        assertTrue(diff.getStringDiff().isDifferent());
        assertTrue(diff.getJsonDiff().isDifferent());
        assertTrue(diff.getStringDiff().getStringDifferences().isEmpty());
        assertFalse(diff.getJsonDiff().getJsonDifferences().isEmpty());
    }

    @Test
    void returnsNotEqualWhenOnlyRightJsonIsSet() {
        when(diffRepository.findById(DIFF_ID)).thenReturn(Optional.of(DIFF_DATA_RIGHT_ONLY));

        Difference diff = diffService.diff(DIFF_ID);

        assertEquals(DIFF_ID, diff.getDiffID());

        assertNotNull(diff.getJsonDiff());
        assertNotNull(diff.getStringDiff());
        assertTrue(diff.getStringDiff().isDifferent());
        assertTrue(diff.getJsonDiff().isDifferent());
        assertTrue(diff.getStringDiff().getStringDifferences().isEmpty());
        assertFalse(diff.getJsonDiff().getJsonDifferences().isEmpty());
    }

    @Test
    void returnsEqualWhenRightAndLeftAreEqual() {
        when(diffRepository.findById(DIFF_ID)).thenReturn(Optional.of(DIFF_DATA_EQUAL));

        Difference diff = diffService.diff(DIFF_ID);

        assertEquals(DIFF_ID, diff.getDiffID());

        assertNotNull(diff.getJsonDiff());
        assertNotNull(diff.getStringDiff());
        assertFalse(diff.getStringDiff().isDifferent());
        assertFalse(diff.getJsonDiff().isDifferent());
        assertTrue(diff.getStringDiff().getStringDifferences().isEmpty());
        assertTrue(diff.getJsonDiff().getJsonDifferences().isEmpty());
    }

    @Test
    void returnsNotEqualWhenRightAndLeftAreNotEqual() {
        when(diffRepository.findById(DIFF_ID)).thenReturn(Optional.of(DIFF_DATA_NOT_EQUAL));

        Difference diff = diffService.diff(DIFF_ID);

        assertEquals(DIFF_ID, diff.getDiffID());

        assertNotNull(diff.getJsonDiff());
        assertNotNull(diff.getStringDiff());
        assertTrue(diff.getStringDiff().isDifferent());
        assertTrue(diff.getJsonDiff().isDifferent());
        assertTrue(diff.getStringDiff().getStringDifferences().isEmpty());
        assertFalse(diff.getJsonDiff().getJsonDifferences().isEmpty());
        assertEquals(2, diff.getJsonDiff().getJsonDifferences().size());

        List<JsonDifference> jsonDifferences = diff.getJsonDiff().getJsonDifferences();

        assertEquals("replace", jsonDifferences.get(0).getOperation());
        assertEquals("/name", jsonDifferences.get(0).getPath());
        assertEquals("Oak", jsonDifferences.get(0).getValue());

        assertEquals("replace", jsonDifferences.get(1).getOperation());
        assertEquals("/species", jsonDifferences.get(1).getPath());
        assertEquals("Quercus robur", jsonDifferences.get(1).getValue());
    }

    @Test
    void returnsNotEqualWhenRightAndLeftAreNotEqualAndHaveTheSameSize() {
        when(diffRepository.findById(DIFF_ID)).thenReturn(Optional.of(DIFF_DATA_NOT_EQUAL_WITH_TYPO));

        Difference diff = diffService.diff(DIFF_ID);

        assertEquals(DIFF_ID, diff.getDiffID());

        assertNotNull(diff.getJsonDiff());
        assertNotNull(diff.getStringDiff());
        assertTrue(diff.getStringDiff().isDifferent());
        assertTrue(diff.getJsonDiff().isDifferent());
        assertFalse(diff.getStringDiff().getStringDifferences().isEmpty());
        assertFalse(diff.getStringDiff().getStringDifferences().isEmpty());
        assertEquals(2, diff.getStringDiff().getStringDifferences().size());
        assertEquals(1, diff.getStringDiff().getStringDifferences().get(0).getLength());
        assertEquals(39, diff.getStringDiff().getStringDifferences().get(0).getOffset());
        assertEquals(1, diff.getStringDiff().getStringDifferences().get(1).getLength());
        assertEquals(42, diff.getStringDiff().getStringDifferences().get(1).getOffset());
        assertFalse(diff.getJsonDiff().getJsonDifferences().isEmpty());
    }

    @Test
    void returnsNotJSONEqualButNotStringEqual() {
        when(diffRepository.findById(DIFF_ID)).thenReturn(Optional.of(DIFF_DATA_ONLY_JSON_EQUAL));

        Difference diff = diffService.diff(DIFF_ID);

        assertEquals(DIFF_ID, diff.getDiffID());
        assertNotNull(diff.getJsonDiff());
        assertNotNull(diff.getStringDiff());
        assertTrue(diff.getStringDiff().isDifferent());
        assertFalse(diff.getJsonDiff().isDifferent());
        assertTrue(diff.getStringDiff().getStringDifferences().isEmpty());
        assertTrue(diff.getJsonDiff().getJsonDifferences().isEmpty());
    }

    private DiffData deepCopyDiffData(DiffData diffData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(objectMapper.writeValueAsBytes(diffData), DiffData.class);
    }

    @Test
    void throwsInvalidInputExceptionWhenBase64isInvalid() {
        InvalidInputException invalidInputException;
        when(decoder.decode(anyString())).thenThrow(new IllegalArgumentException(CAUSE_STRING));

        invalidInputException = assertThrows(InvalidInputException.class,
                () -> diffService.saveLeftJson(DIFF_ID, JSON_1));
        assertEquals(invalidInputException.getType(), ApplicationErrorEnum.INVALID_BASE64.getType());
        assertEquals(invalidInputException.getTitle(), ApplicationErrorEnum.INVALID_BASE64.getTitle());
        assertEquals(invalidInputException.getStatus(), ApplicationErrorEnum.INVALID_BASE64.getStatus());
        assertEquals(invalidInputException.getDetail(),
                MessageFormat.format(ApplicationErrorEnum.INVALID_BASE64.getDetail(), CAUSE_STRING));
    }

    @Test
    void throwsInvalidInputExceptionWhenJsonisInvalid() {
        InvalidInputException invalidInputException;
        when(decoder.decode(anyString())).thenReturn(INVALID_JSON.getBytes(StandardCharsets.UTF_8));

        invalidInputException = assertThrows(InvalidInputException.class,
                () -> diffService.saveLeftJson(DIFF_ID, INVALID_JSON));
        assertEquals(invalidInputException.getType(), ApplicationErrorEnum.INVALID_JSON.getType());
        assertEquals(invalidInputException.getTitle(), ApplicationErrorEnum.INVALID_JSON.getTitle());
        assertEquals(invalidInputException.getStatus(), ApplicationErrorEnum.INVALID_JSON.getStatus());
        assertTrue(invalidInputException.getDetail()
                .contains(MessageFormat.format(ApplicationErrorEnum.INVALID_JSON.getDetail(), "")));
    }
}