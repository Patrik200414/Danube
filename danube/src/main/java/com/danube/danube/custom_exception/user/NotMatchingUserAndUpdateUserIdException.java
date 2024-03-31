package com.danube.danube.custom_exception.user;

public class NotMatchingUserAndUpdateUserIdException extends RuntimeException{
    public NotMatchingUserAndUpdateUserIdException() {
        super("Not matching user and update user id!");
    }
}
