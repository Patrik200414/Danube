package com.danube.danube.custom_exception.user;

public class NotMatchingNewPasswordAndReenterPasswordException extends RuntimeException{
    public NotMatchingNewPasswordAndReenterPasswordException() {
        super("The new password and the reenter password does not matches!");
    }
}
