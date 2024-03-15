package com.danube.danube.model.dto;

public record ProductDetailUploadDTO(
        int deliveryTimeInDay,
        double price,
        int quantity,
        double shippingPrice,
        String brand,
        String description,
        String productName
) {
}
