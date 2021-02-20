package com.waes.diffservice.exception;

import com.waes.diffservice.ApplicationErrorEnum;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;

/**
 * Implementation of the <a href="https://tools.ietf.org/html/rfc7807">RFC-7807</a>, a standard
 * for error response formats for HTTP APIs.
 */
@Getter
public abstract class GenericException extends RuntimeException {

    private final String type;
    private final String title;
    private final int status;
    private final String detail;

    @Setter
    private String instance;

    protected GenericException(Throwable cause, ApplicationErrorEnum error, Object... args) {
        super(cause);

        this.type = error.getType();
        this.title = error.getTitle();
        this.status = error.getStatus();
        this.detail = MessageFormat.format(error.getDetail(), args);
    }

}
