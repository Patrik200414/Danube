package com.danube.danube.service.utility;

import com.danube.danube.model.dto.ProductShowSmallDTO;
import com.danube.danube.model.dto.product.ProductDetailUploadDTO;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.product.ProductDetail;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class Converter {

    public static UserEntity convertUserRegistrationDTOToUserEntity(UserRegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder){
        UserEntity user = new UserEntity();
        user.setFirstName(userRegistrationDTO.firstName());
        user.setLastName(userRegistrationDTO.lastName());
        user.setEmail(userRegistrationDTO.email());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.password()));
        user.setRoles(Set.of(Role.ROLE_CUSTOMER));

        return user;
    }

    public static ProductDetail convertToProductDetail(ProductDetailUploadDTO productDetailUploadDTO){
        ProductDetail productDetail = new ProductDetail();

        productDetail.setBrand(productDetailUploadDTO.brand());
        productDetail.setProductName(productDetailUploadDTO.productName());
        productDetail.setDescription(productDetailUploadDTO.description());
        productDetail.setPrice(productDetailUploadDTO.price());
        productDetail.setQuantity(productDetailUploadDTO.quantity());
        productDetail.setShippingPrice(productDetailUploadDTO.shippingPrice());
        productDetail.setDeliveryTimeInDay(productDetailUploadDTO.deliveryTimeInDay());
        productDetail.setRating(0);
        productDetail.setSold(0);

        return productDetail;
    }

    public static List<ProductShowSmallDTO> convertProductDetails(List<ProductDetail> productDetails){

        return productDetails.stream()
                .map(productDetail -> new ProductShowSmallDTO(
                        productDetail.getProductName(),
                        productDetail.getPrice(),
                        productDetail.getShippingPrice(),
                        productDetail.getDeliveryTimeInDay(),
                        productDetail.getQuantity(),
                        productDetail.getRating(),
                        productDetail.getSold()
                )).toList();
    }
}
