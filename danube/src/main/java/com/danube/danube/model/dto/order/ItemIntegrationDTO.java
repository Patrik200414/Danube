package com.danube.danube.model.dto.order;

import java.util.List;

public record ItemIntegrationDTO(
        long customerId,
        List<CartItemShowDTO> products
) {
}
