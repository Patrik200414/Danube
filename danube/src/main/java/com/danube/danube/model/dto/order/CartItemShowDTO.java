package com.danube.danube.model.dto.order;

public record CartItemShowDTO(
        long id,
        String productName,
        double price,
        String image,
        int orderedQuantity,
        double rating
        ) {
}
