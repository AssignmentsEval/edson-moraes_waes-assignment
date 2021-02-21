package com.waes.diffservice.exception;

import com.waes.diffservice.ApplicationErrorEnum;

/**
 * A exception for problems related to input values
 */
public class InputException extends DiffServiceException{

    public InputException(Throwable cause, ApplicationErrorEnum error, Object... args) {
        super(cause, error, args);
    }
}
