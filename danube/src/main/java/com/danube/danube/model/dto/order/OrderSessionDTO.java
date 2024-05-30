package com.danube.danube.model.dto.order;

import java.util.List;

public record OrderSessionDTO(
        String paymentSessionId,
        long userId
) {
}
