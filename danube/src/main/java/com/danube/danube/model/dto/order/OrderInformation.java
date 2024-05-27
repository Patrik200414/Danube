package com.danube.danube.model.dto.order;

public record LocationInformation(
        String streetAddess,
        String city,
        String state,
        String country,
        int zip
) {
}
