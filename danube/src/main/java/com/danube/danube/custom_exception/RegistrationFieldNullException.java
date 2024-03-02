package com.danube.danube.custom_exception;

public class RegistrationFieldNullException extends RuntimeException{
    public RegistrationFieldNullException(String fieldName) {
        super(fieldName);
    }
}
