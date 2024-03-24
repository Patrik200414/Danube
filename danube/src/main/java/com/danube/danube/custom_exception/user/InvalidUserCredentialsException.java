package com.danube.danube.custom_exception.user;

public class InvalidUserCredentialsException extends RuntimeException{
    public InvalidUserCredentialsException() {
        super("User has invalid credentials for this operation!");
    }
}
