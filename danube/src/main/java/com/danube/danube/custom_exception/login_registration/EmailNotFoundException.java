package com.danube.danube.custom_exception.login_registration;

public class EmailNotFoundException extends RuntimeException{
    public EmailNotFoundException(String email) {
        super(String.format("Email '%s' not found!", email));
    }
}
