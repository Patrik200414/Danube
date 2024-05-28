package com.danube.danube.custom_exception.payment;

public class UnableToCreatePaymentSessionException extends RuntimeException{
    public UnableToCreatePaymentSessionException() {
        super("Unable to create payment session!");
    }
}
