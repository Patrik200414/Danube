package com.danube.danube.model.dto.product;

import com.danube.danube.model.dto.product.ProductDetailUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

public record ProductUploadDTO(
        ProductDetailUploadDTO productDetail,
        Map<String, String> productInformation,
        UUID userId,
        MultipartFile[] images
) {
}
