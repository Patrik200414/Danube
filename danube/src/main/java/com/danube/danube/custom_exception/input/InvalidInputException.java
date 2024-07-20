package com.danube.danube.custom_exception.input;

public class InvalidInputException extends RuntimeException{
    public InvalidInputException(String errorMessage) {
        super(errorMessage);
    }
}
