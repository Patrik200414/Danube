package com.danube.danube.utility.validation.request.product;

import com.danube.danube.model.dto.product.ProductDetailUploadDTO;
import com.danube.danube.model.dto.product.ProductInformation;

public interface ProductRequestValidator {
    void validateProductDetail(ProductDetailUploadDTO productDetailUploadDTO);
    void validateProductInformation(ProductInformation productInformation);
}
