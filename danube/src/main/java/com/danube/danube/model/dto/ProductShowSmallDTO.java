package com.danube.danube.model.dto;


/*TODO: Add image also*/
public record ProductShowSmallDTO(
        String productName,
        double price,
        double shippingPrice,
        int deliveryTimeInDay,
        int quantity,
        int rating,
        int sold,
        long id
) {
}
