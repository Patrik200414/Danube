package com.danube.danube.custom_exception.login_registration;

public class InputTooShortException extends RuntimeException{
    public InputTooShortException(String inputName, int minLength) {
        super(String.format("%s-%s", inputName, minLength));
    }
}
