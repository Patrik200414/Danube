package com.danube.danube.model.dto.order;

import java.util.UUID;

public record AddToCartDTO(UUID customerId, long productId, int quantity) {
}
