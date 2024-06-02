package com.danube.danube.utility.converter.categoriesanddetails;

import com.danube.danube.model.dto.product.CategoryDTO;
import com.danube.danube.model.dto.product.DetailDTO;
import com.danube.danube.model.dto.product.SubcategoriesDTO;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.subcategory.Subcategory;

import java.util.List;

public interface ProductCategoriesAndDetailsConverter {
    List<CategoryDTO> convertCategoryToCategoryDTO(List<Category> categories);
    List<SubcategoriesDTO> convertSubcategoriesToSubcategoryDTOs(List<Subcategory> subcategories);
    List<DetailDTO> convertDetailsToDetailsDTO(List<Detail> details);
}
