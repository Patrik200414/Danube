package com.danube.danube.model.dto.order;

public record CartItemShowDTO(
        String productName,
        double price,
        String image,
        int orderedQuantity,
        double rating
        ) {
}
