package com.waes.diffservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Difference {
    private final Long diffId;
    private final StringDiff stringDiff;
    private final JsonDiff jsonDiff;
}
