package com.danube.danube.custom_exception.order;

public class NotEnoughQuantityToOrderException extends RuntimeException{
    public NotEnoughQuantityToOrderException(int orderedQuantity, int maxQuantity) {
        super(String.format("You can't order %s amount of this product. The maximum amount you can order is %s.", orderedQuantity, maxQuantity));
    }
}
