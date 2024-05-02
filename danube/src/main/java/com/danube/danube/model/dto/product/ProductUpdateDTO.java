package com.danube.danube.model.dto.product;

import java.util.List;

public record ProductUpdateDTO(
        ProductInformation productInformation,
        List<String> images,
        List<DetailValueDTO> detailValues
) {
}
