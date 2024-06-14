package com.danube.danube.utility.converter.uploadproduct;

import com.danube.danube.model.dto.product.ProductDetailUploadDTO;
import com.danube.danube.model.dto.product.ProductUpdateDTO;
import com.danube.danube.model.dto.product.ProductUploadDTO;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.image.Image;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.utility.imageutility.ImageUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductUploadConverter {
    Product convertProductDetailUploadDTOToProduct(ProductDetailUploadDTO productDetails, UserEntity seller, Subcategory subcategory);
    ProductUploadDTO convertRequestParamToProductUploadDTO(
            String productDetail,
            String productInformation,
            long userId,
            MultipartFile[] image
    ) throws JsonProcessingException;
    List<Image> convertMultiPartFilesToListOfImages(MultipartFile[] images, Product product, ImageUtility imageUtility) throws IOException;
    ProductUpdateDTO convertProductToProductUpdateDTO(Product product);
    ProductUpdateDTO convertUpdateDataToProductUpdateDTO(String updatedValue) throws JsonProcessingException;
}
