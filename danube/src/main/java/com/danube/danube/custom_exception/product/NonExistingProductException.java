package com.danube.danube.custom_exception.product;

public class NonExistingProductException extends RuntimeException{
    public NonExistingProductException() {
        super("Non existing product exception!");
    }
}
