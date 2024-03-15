package com.danube.danube.custom_exception.product;

public class IncorrectProductObjectFormException extends RuntimeException{
    public IncorrectProductObjectFormException() {
        super("Incorrect product object form!");
    }
}
