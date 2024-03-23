package com.danube.danube.service;

import com.danube.danube.custom_exception.product.NonExistingProductCategoryException;
import com.danube.danube.model.dto.product.*;
/*import com.danube.danube.model.product.ProductDetail;
import com.danube.danube.model.product.product_category.Category;
import com.danube.danube.model.product.product_category.SubCategory;
import com.danube.danube.model.product.product_information.ProductInformation;
import com.danube.danube.repository.product.ProductDetailRepository;
import com.danube.danube.repository.product.ProductInformationRepository;*/
import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.repository.product.CategoryRepository;
import com.danube.danube.repository.product.ProductRepository;
import com.danube.danube.repository.product.SubcategoryRepository;
import com.danube.danube.utility.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    public static final int DEFAULT_ITEM_AMOUNT_PAGE = 10;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final Converter converter;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, Converter converter) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.converter = converter;
    }


    public List<ProductShowSmallDTO> getProducts(int pageNumber, int itemPerPage){
        PageRequest pageRequest = PageRequest.of(pageNumber, itemPerPage);
        List<Product> products = productRepository.findAll();
        return converter.convertProductsToProductShowSmallDTOs(products);
    }

    public long getProductCount(){
        return productRepository.count();
    }



    public List<CategoryAndSubCategoryDTO> getCategoriesAndSubCategories(){
        List<Category> categories = categoryRepository.findAll();

        List<CategoryAndSubCategoryDTO> categoriesAndSubCategories = new ArrayList<>();
        for(Category category : categories){
            List<String> subcategories = new ArrayList<>();
            for(Subcategory subcategory : category.getSubcategories()){
                subcategories.add(subcategory.getName());
            }
            CategoryAndSubCategoryDTO categoryAndSubCategoryDTO = new CategoryAndSubCategoryDTO(
                    category.getName(),
                    subcategories
            );

            categoriesAndSubCategories.add(categoryAndSubCategoryDTO);
        }

        return categoriesAndSubCategories;
    }


    public List<CategoryDTO> getCategories(){
        List<Category> categories = categoryRepository.findAll();
        return converter.convertCategoryToCategoryDTO(categories);
    }

    public List<SubcategoriesDTO> getSubCategoriesByCategory(long categoryId){
        Optional<Category> searchedCategory = categoryRepository.findById(categoryId);

        if(searchedCategory.isEmpty()){
            throw new NonExistingProductCategoryException();
        }

        Category category = searchedCategory.get();
        List<Subcategory> subcategories = subcategoryRepository.findAllByCategory(category);
        return converter.convertSubcategoriesToSubcategoryDTOs(subcategories);
    }

    public void saveProduct(ProductUploadDTO productUploadDTO){
        /*
        ProductInformation productInformation = productInformationCreator.createProduct(productUploadDTO.productInformation());
        ProductDetail productDetail = converter.convertToProductDetail(productUploadDTO.productDetail());

        productInformationRepository.save(productInformation);
        productDetail.setProductInformation(productInformation);
        productInformation.setProductDetail(productDetail);


        productDetailRepository.save(productDetail);
        productInformationRepository.save(productInformation);*/
    }
}
