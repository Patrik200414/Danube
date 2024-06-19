package com.danube.danube.custom_exception.login_registration;

public class NonExistingUserException extends RuntimeException{
    public NonExistingUserException() {
        super("Non existing user exception!");
    }
}
