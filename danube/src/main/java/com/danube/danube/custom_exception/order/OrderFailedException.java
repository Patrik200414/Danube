package com.danube.danube.custom_exception.order;

public class OrderFailedException extends RuntimeException{
    public OrderFailedException() {
        super("Order failed exception!");
    }
}
