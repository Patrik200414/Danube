package com.danube.danube.model.dto.product;

import com.danube.danube.model.dto.image.ImageShow;

import java.util.List;

public record ProductItemDTO(
        ProductInformation productInformation,
        List<ImageShow> images,
        List<DetailDTO> detailValues
) {
}
