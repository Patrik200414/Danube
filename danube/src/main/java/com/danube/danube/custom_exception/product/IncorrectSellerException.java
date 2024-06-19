package com.danube.danube.custom_exception.product;

public class IncorrectSellerException extends RuntimeException{
    public IncorrectSellerException() {
        super("Incorrect seller exception!");
    }
}
