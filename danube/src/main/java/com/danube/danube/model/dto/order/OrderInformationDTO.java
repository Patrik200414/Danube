package com.danube.danube.model.dto.order;

public record OrderInformationDTO(
        String streetAddess,
        String city,
        String state,
        String country,
        int zip,
        long userId
) {
}
