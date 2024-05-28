package com.danube.danube.model.dto.order;

public record OrderInformationDTO(
        String streetAddress,
        String city,
        String state,
        String country,
        int zip,
        long customerId
) {
}
