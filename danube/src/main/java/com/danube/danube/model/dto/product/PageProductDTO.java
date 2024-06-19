package com.danube.danube.model.dto.product;

import java.util.Set;

public record PageProductDTO(
        Set<ProductShowSmallDTO> products,
        int pageNumber,
        long productCount
) {

}
