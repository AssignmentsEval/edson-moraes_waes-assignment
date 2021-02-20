package com.waes.diffservice.dto;

import lombok.Getter;

import javax.json.JsonPatch;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.List;

@Getter
public class JsonDiff {
    private final boolean equal;
    private final List<JsonDifference> jsonDifferences;

    public JsonDiff(boolean equal, JsonPatch jsonPatch) {
        this.equal = equal;
        this.jsonDifferences = new ArrayList<>();

        for (JsonValue obj : jsonPatch.toJsonArray()) {
            jsonDifferences.add(new JsonDifference(

                    obj.asJsonObject().getString("op"),
                    obj.asJsonObject().getString("path"),
                    obj.asJsonObject().getString("value",null))
            );
        }
    }
}
