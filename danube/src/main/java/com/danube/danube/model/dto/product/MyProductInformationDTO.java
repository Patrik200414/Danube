package com.danube.danube.model.dto.product;

import com.danube.danube.model.dto.image.ImageShow;

public record MyProductInformationDTO(
        ImageShow image,
        String productName,
        String owner,
        long id
) {
}
