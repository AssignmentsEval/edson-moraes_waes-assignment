package com.waes.diffservice;

import lombok.Getter;

@Getter
public enum ApplicationErrorEnum {

    // Generic Application errors
    UNEXPECTED_ERROR("DFE-001", "Ops, an unxepected error ocurred!", 500,
            "Sorry an unexpected error ocurred. We are doing our bet to fix it "),


    // Input related errors
    INVALID_BASE64("IPT-001", "The input is not a valid base64 string", 400,
            "The input is not a valid base64 string. Cause: {0}"),
    INVALID_JSON("IPT-002", "The input is not a valid Json object", 400,
            "The input is not a valid Json object. Cause: {0}");

    private final String type;
    private final String title;
    private final int status;
    private final String detail;

    ApplicationErrorEnum(String type, String title, int status, String detail) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
    }

}
