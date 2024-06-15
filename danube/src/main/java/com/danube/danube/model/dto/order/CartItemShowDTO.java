package com.danube.danube.model.dto.order;

import com.danube.danube.model.dto.image.ImageShow;

public record CartItemShowDTO(
        long id,
        String productName,
        double price,
        ImageShow image,
        int orderedQuantity,
        double rating
        ) {
}
