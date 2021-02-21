package com.waes.diffservice.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorDTO {
    private final String type;
    private final String title;
    private final int status;
    private final String detail;
    private final String instance;
}
