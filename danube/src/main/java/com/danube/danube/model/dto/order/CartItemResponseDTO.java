package com.danube.danube.model.dto.order;

import java.util.List;

public record CartItemResponseDTO(
        List<CartItemShowDTO> cartItems
) {
}
