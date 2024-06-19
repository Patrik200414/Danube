package com.danube.danube.model.dto.order;

import java.util.List;
import java.util.UUID;

public record ItemIntegrationDTO(
        UUID customerId,
        List<CartItemShowDTO> products
) {
}
