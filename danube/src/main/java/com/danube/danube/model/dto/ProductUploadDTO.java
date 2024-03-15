package com.danube.danube.model.dto;

import java.util.Map;

public record ProductUploadDTO(
        ProductDetailUploadDTO productDetail,
        Map<String, String> productInformation
) {
}
