package com.danube.danube.utility;

import com.danube.danube.model.dto.product.CategoryDTO;
import com.danube.danube.model.dto.product.DetailDTO;
import com.danube.danube.model.dto.product.ProductShowSmallDTO;
import com.danube.danube.model.dto.product.SubcategoriesDTO;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.model.user.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface Converter {
    UserEntity convertUserRegistrationDTOToUserEntity(UserRegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder);
    List<ProductShowSmallDTO> convertProductsToProductShowSmallDTOs(List<Product> products);
    List<CategoryDTO> convertCategoryToCategoryDTO(List<Category> categories);
    List<SubcategoriesDTO> convertSubcategoriesToSubcategoryDTOs(List<Subcategory> subcategories);
    List<DetailDTO> convertDetailsToDetailsDTO(List<Detail> details);
    //ProductDetail convertToProductDetail(ProductDetailUploadDTO productDetailUploadDTO);
    //List<ProductShowSmallDTO> convertProductDetails(List<ProductDetail> productDetails);
}
