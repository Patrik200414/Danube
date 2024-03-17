package com.danube.danube.custom_exception.user;

public class UserEntityPasswordMissMatchException extends RuntimeException{

    public UserEntityPasswordMissMatchException() {
        super("Unmatching password for the given usr!");
    }
}
