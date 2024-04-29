package com.danube.danube.model.dto.product;

import java.util.List;

public record ProductItemDTO(
        ProductInformation productInformation,
        List<String> images,
        List<DetailDTO> detailValues
) {
}
