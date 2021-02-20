package com.waes.diffservice.exception;

import com.waes.diffservice.ApplicationErrorEnum;

public class DiffServiceException extends GenericException {

    protected DiffServiceException(Throwable cause, ApplicationErrorEnum error, Object... args) {
        super(cause, error, args);
    }
}
