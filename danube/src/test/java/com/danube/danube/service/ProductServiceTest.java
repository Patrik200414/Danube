package com.danube.danube.service;

import com.danube.danube.repository.product.*;
import com.danube.danube.repository.product.connection.ProductValueRepository;
import com.danube.danube.repository.product.connection.SubcategoryDetailRepository;
import com.danube.danube.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
    ProductService productService;
    ProductRepository productRepositoryMock;
    CategoryRepository categoryRepository;
    SubcategoryRepository subcategoryRepository;
    DetailRepository detailRepository;
    UserRepository userRepository;
    ValueRepository valueRepository;
    ProductValueRepository productValueRepository;
    ImageRepository imageRepository;
    SubcategoryDetailRepository subcategoryDetailRepository;

    @BeforeEach
    void setup(){
        productRepositoryMock = mock(ProductRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        subcategoryRepository = mock(SubcategoryRepository.class);
        detailRepository = mock(DetailRepository.class);
        userRepository = mock(UserRepository.class);
        valueRepository = mock(ValueRepository.class);
        productValueRepository = mock(ProductValueRepository.class);
        imageRepository = mock(ImageRepository.class);
        subcategoryDetailRepository = mock(SubcategoryDetailRepository.class);
        //productService = new ProductService(productRepositoryMock, categoryRepository, subcategoryRepository, detailRepository, userRepository, valueRepository, productValueRepository, imageRepository, subcategoryDetailRepository, );
    }

    /*@Test
    void getProducts() {
    }

    @Test
    void getProductCount() {
    }

    @Test
    void getCategoriesAndSubCategories() {
    }

    @Test
    void getCategories() {
    }

    @Test
    void getSubCategoriesByCategory() {
    }

    @Test
    void getDetailsBySubcategory() {
    }

    @Test
    void getSimilarProducts() {
    }

    @Test
    void saveProduct() {
    }

    @Test
    void getMyProducts() {
    }

    @Test
    void getProductItem() {
    }

    @Test
    void getUpdatableProductItem() {
    }

    @Test
    void updateProduct() {
    }*/
}