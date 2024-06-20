package com.danube.danube.model.dto.product;

import java.util.Collection;
import java.util.Set;

public record PageProductDTO(
        Collection<ProductShowSmallDTO> products,
        int pageNumber,
        long productCount
) {

}
