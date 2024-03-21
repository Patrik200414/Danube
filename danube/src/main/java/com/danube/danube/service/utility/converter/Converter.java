package com.danube.danube.service.utility.converter;

import com.danube.danube.model.dto.product.ProductShowSmallDTO;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.user.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface Converter {
    UserEntity convertUserRegistrationDTOToUserEntity(UserRegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder);
    List<ProductShowSmallDTO> convertProductsToProductShowSmallDTOs(List<Product> products);
    //ProductDetail convertToProductDetail(ProductDetailUploadDTO productDetailUploadDTO);
    //List<ProductShowSmallDTO> convertProductDetails(List<ProductDetail> productDetails);
}
