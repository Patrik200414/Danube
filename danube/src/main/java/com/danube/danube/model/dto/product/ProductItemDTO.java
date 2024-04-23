package com.danube.danube.model.dto.product;

import java.util.List;

public record ProductItemDTO(
        String productName,
        double price,
        int deliveryTimeInDay,
        int quantity,
        int rating,
        double shippingPrice,
        int sold,
        String brand,
        String description,
        String seller,
        List<String> images,
        List<DetailValueDTO> detailValues
) {
}
