package com.danube.danube.custom_exception.product;

public class NonExistingProductCategoryException extends RuntimeException{
    public NonExistingProductCategoryException(String categoryName) {
        super(String.format("The searched category '%s' not exists!", categoryName));
    }
}
