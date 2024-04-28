package com.danube.danube.model.dto.product;


import java.util.List;

public record ProductShowSmallDTO(
        String productName,
        double price,
        double shippingPrice,
        int deliveryTimeInDay,
        int quantity,
        int rating,
        int sold,
        long id,
        List<String> images,
        String sellerName
) {
}
