package com.danube.danube.custom_exception;

public class InputTooShortException extends RuntimeException{
    public InputTooShortException(String inputName) {
        super(inputName);
    }
}
