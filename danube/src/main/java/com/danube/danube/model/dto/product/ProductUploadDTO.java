package com.danube.danube.model.dto.product;

import com.danube.danube.model.dto.product.ProductDetailUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public record ProductUploadDTO(
        ProductDetailUploadDTO productDetail,
        Map<String, String> productInformation,
        long userId,
        MultipartFile[] images
) {
}
