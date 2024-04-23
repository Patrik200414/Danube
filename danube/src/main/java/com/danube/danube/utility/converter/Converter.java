package com.danube.danube.utility.converter;

import com.danube.danube.model.dto.product.*;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.image.Image;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.model.user.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface Converter {
    UserEntity convertUserRegistrationDTOToUserEntity(UserRegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder);
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

    List<Image> convertMultiPartFilesToListOfImages(MultipartFile[] images, Product product);
    Set<ProductShowSmallDTO> convertProductToProductShowSmallDTORandomOrder(Page<Product> products);
    ProductItemDTO convertProductToProductItemDTO(Product product);
}
