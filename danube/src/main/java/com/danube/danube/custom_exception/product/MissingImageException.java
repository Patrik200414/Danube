package com.danube.danube.custom_exception.product;

public class MissingImageException extends RuntimeException{
    public MissingImageException() {
        super("Missing image exception!");
    }
}
