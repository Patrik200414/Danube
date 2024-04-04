package com.danube.danube.utility;

import com.danube.danube.model.dto.product.*;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.model.user.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface Converter {
    UserEntity convertUserRegistrationDTOToUserEntity(UserRegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder);
    List<ProductShowSmallDTO> convertProductsToProductShowSmallDTOs(List<Product> products);
    List<CategoryDTO> convertCategoryToCategoryDTO(List<Category> categories);
    List<SubcategoriesDTO> convertSubcategoriesToSubcategoryDTOs(List<Subcategory> subcategories);
    List<DetailDTO> convertDetailsToDetailsDTO(List<Detail> details);
    Product convertProductDetailUploadDTOToProduct(ProductDetailUploadDTO productDetails, UserEntity seller);
    ProductUploadDTO convertRequestParamToProductUploadDTO(
            String productDetail,
            String productInformation,
            long userId,
            MultipartFile[] image
    ) throws JsonProcessingException;
    //ProductDetail convertToProductDetail(ProductDetailUploadDTO productDetailUploadDTO);
    //List<ProductShowSmallDTO> convertProductDetails(List<ProductDetail> productDetails);
}
