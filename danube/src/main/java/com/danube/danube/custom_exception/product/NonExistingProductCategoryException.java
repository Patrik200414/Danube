package com.danube.danube.custom_exception.product;

public class NonExistingProductCategoryException extends RuntimeException{
    public NonExistingProductCategoryException() {
        super("The searched category not exists!");
    }
}
