package com.danube.danube.model.dto.product;

public record ProductInformation(
        String productName,
        double price,
        int deliveryTimeInDay,
        int quantity,
        int rating,
        double shippingPrice,
        int sold,
        String brand,
        String description,
        String seller
) {
}
