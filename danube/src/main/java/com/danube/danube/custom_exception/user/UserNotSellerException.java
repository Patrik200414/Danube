package com.danube.danube.custom_exception.user;

public class UserNotSellerException extends RuntimeException{
    public UserNotSellerException() {
        super("User not seller exception!");
    }
}
