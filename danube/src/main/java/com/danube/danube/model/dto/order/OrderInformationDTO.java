package com.danube.danube.model.dto.order;

import java.util.UUID;

public record OrderInformationDTO(
        String streetAddress,
        String city,
        String state,
        String country,
        int zip,
        UUID customerId
) {
}
