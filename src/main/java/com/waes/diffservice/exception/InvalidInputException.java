package com.waes.diffservice.exception;

import com.waes.diffservice.ApplicationErrorEnum;

public class InvalidInputException extends GenericException{

    public InvalidInputException(Throwable cause, ApplicationErrorEnum error, Object... args) {
        super(cause, error, args);
    }
}
