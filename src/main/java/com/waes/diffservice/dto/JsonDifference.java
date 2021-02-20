package com.waes.diffservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JsonDifference {
    private final String operation;
    private final String path;
    private final String value;
}
