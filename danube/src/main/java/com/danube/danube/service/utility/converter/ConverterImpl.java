package com.danube.danube.service.utility.converter;

import com.danube.danube.model.dto.ProductShowSmallDTO;
import com.danube.danube.model.dto.product.ProductDetailUploadDTO;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.product.ProductDetail;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.service.utility.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

public class ConverterImpl implements Converter {

    public UserEntity convertUserRegistrationDTOToUserEntity(UserRegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder){
        UserEntity user = new UserEntity();
        user.setFirstName(userRegistrationDTO.firstName());
        user.setLastName(userRegistrationDTO.lastName());
        user.setEmail(userRegistrationDTO.email());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.password()));
        user.setRoles(Set.of(Role.ROLE_CUSTOMER));

        return user;
    }

    public ProductDetail convertToProductDetail(ProductDetailUploadDTO productDetailUploadDTO){
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

    public List<ProductShowSmallDTO> convertProductDetails(List<ProductDetail> productDetails){

        return productDetails.stream()
                .map(productDetail -> new ProductShowSmallDTO(
                        productDetail.getProductName(),
                        productDetail.getPrice(),
                        productDetail.getShippingPrice(),
                        productDetail.getDeliveryTimeInDay(),
                        productDetail.getQuantity(),
                        productDetail.getRating(),
                        productDetail.getSold(),
                        productDetail.getId()
                )).toList();
    }
}
