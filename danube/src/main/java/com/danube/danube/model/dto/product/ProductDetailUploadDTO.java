package com.danube.danube.model.dto.product;

import com.danube.danube.model.product.subcategory.Subcategory;

public record ProductDetailUploadDTO(
        int deliveryTimeInDay,
        double price,
        int quantity,
        double shippingPrice,
        String brand,
        String description,
        String productName,
        long subcategoryId
) {
}
