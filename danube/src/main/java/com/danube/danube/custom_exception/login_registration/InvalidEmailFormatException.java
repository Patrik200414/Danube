package com.danube.danube.custom_exception.login_registration;

public class InvalidEmailFormatException extends RuntimeException{
    public InvalidEmailFormatException() {
        super("Invalid email exception!");
    }
}
