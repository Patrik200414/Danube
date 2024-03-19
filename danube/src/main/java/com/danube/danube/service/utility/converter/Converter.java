package com.danube.danube.service.utility.converter;

import com.danube.danube.model.dto.ProductShowSmallDTO;
import com.danube.danube.model.dto.product.ProductDetailUploadDTO;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.product.ProductDetail;
import com.danube.danube.model.user.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface Converter {
    UserEntity convertUserRegistrationDTOToUserEntity(UserRegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder);
    ProductDetail convertToProductDetail(ProductDetailUploadDTO productDetailUploadDTO);
    List<ProductShowSmallDTO> convertProductDetails(List<ProductDetail> productDetails);
}
