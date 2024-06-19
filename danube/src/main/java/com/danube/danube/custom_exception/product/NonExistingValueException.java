package com.danube.danube.custom_exception.product;

public class NonExistingValueException extends RuntimeException{
    public NonExistingValueException() {
        super("Non Existing value exception!");
    }
}
