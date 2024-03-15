package com.danube.danube.custom_exception.login_registration;

public class RegistrationFieldNullException extends RuntimeException{
    public RegistrationFieldNullException(String fieldName) {
        super(fieldName);
    }
}
