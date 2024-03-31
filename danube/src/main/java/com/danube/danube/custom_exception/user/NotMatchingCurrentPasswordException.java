package com.danube.danube.custom_exception.user;

public class NotMatchingCurrentPasswordException extends RuntimeException{
    public NotMatchingCurrentPasswordException() {
        super("The entered current password does not matches that are in the system!");
    }
}
