package com.waes.diffservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StringDifference {
    private final int offset;
    private final int length;
}
