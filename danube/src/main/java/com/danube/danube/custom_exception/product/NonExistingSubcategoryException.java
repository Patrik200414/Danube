package com.danube.danube.custom_exception.product;

public class NonExistingSubcategoryException extends RuntimeException{
    public NonExistingSubcategoryException() {
        super("The searched subcategory not exists!");
    }
}
