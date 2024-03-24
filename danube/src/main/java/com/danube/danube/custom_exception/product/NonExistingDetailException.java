package com.danube.danube.custom_exception.product;

public class NonExistingDetailException extends RuntimeException{
    public NonExistingDetailException() {
        super("The searched detail doesn't exists!");
    }
}
