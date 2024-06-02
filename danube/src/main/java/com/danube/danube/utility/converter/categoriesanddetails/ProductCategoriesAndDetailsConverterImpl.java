package com.danube.danube.utility.converter.categoriesanddetails;

import com.danube.danube.model.dto.product.CategoryDTO;
import com.danube.danube.model.dto.product.DetailDTO;
import com.danube.danube.model.dto.product.SubcategoriesDTO;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.subcategory.Subcategory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductCategoriesAndDetailsConverterImpl implements ProductCategoriesAndDetailsConverter{
    @Override
    public List<CategoryDTO> convertCategoryToCategoryDTO(List<Category> categories){
        return categories.stream()
                .map(category -> new CategoryDTO(
                        category.getName(),
                        category.getId()
                )).toList();
    }

    @Override
    public List<SubcategoriesDTO> convertSubcategoriesToSubcategoryDTOs(List<Subcategory> subcategories){
        return subcategories.stream()
                .map(subcategory -> new SubcategoriesDTO(
                        subcategory.getName(),
                        subcategory.getId()
                ))
                .toList();
    }

    @Override
    public List<DetailDTO> convertDetailsToDetailsDTO(List<Detail> details) {
        return details.stream()
                .map(detail -> new DetailDTO(
                        detail.getName(),
                        detail.getId(),
                        ""
                )).toList();
    }
}
