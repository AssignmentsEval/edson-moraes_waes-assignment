package com.waes.diffservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StringDiff {
    boolean different;
    List<StringDifference> stringDifferences;
}
