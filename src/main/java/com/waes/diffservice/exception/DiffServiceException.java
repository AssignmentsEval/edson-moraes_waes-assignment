package com.waes.diffservice.exception;

import com.waes.diffservice.ApplicationErrorEnum;

/**
 * A generic exception from which all other application related exceptions should derive
 */
public class DiffServiceException extends GenericException {

    public DiffServiceException(Throwable cause, ApplicationErrorEnum error, Object... args) {
        super(cause, error, args);
    }
}
