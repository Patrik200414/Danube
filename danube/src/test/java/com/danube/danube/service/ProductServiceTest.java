package com.danube.danube.service;

import com.danube.danube.custom_exception.product.NonExistingProductCategoryException;
import com.danube.danube.model.dto.product.CategoryAndSubCategoryDTO;
import com.danube.danube.model.dto.product.ProductShowSmallDTO;
import com.danube.danube.model.dto.product.SubcategoriesDTO;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.repository.product.*;
import com.danube.danube.repository.product.connection.ProductValueRepository;
import com.danube.danube.repository.product.connection.SubcategoryDetailRepository;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.utility.Converter;
import com.danube.danube.utility.ConverterImpl;
import com.danube.danube.utility.filellogger.FileLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.BeforeEach.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
    private ProductRepository productRepositoryMock;
    private CategoryRepository categoryRepositoryMock;
    private SubcategoryRepository subcategoryRepositoryMock;
    private DetailRepository detailRepositoryMock;
    private UserRepository userRepositoryMock;
    private ValueRepository valueRepositoryMock;
    private ProductValueRepository productValueRepositoryMock;
    private ImageRepository imageRepositoryMock;
    private SubcategoryDetailRepository subcategoryDetailRepositoryMock;
    private Converter converter;
    private FileLogger fileLoggerMock;

    private ProductService productService;
    @BeforeEach
    void setUp(){
        productRepositoryMock = mock(ProductRepository.class);
        categoryRepositoryMock = mock(CategoryRepository.class);
        subcategoryRepositoryMock = mock(SubcategoryRepository.class);
        detailRepositoryMock = mock(DetailRepository.class);
        userRepositoryMock = mock(UserRepository.class);
        valueRepositoryMock = mock(ValueRepository.class);
        productValueRepositoryMock = mock(ProductValueRepository.class);
        imageRepositoryMock = mock(ImageRepository.class);
        subcategoryDetailRepositoryMock = mock(SubcategoryDetailRepository.class);
        converter = new ConverterImpl();
        fileLoggerMock = mock(FileLogger.class);

        productService = new ProductService(
                productRepositoryMock,
                categoryRepositoryMock,
                subcategoryRepositoryMock,
                detailRepositoryMock,
                userRepositoryMock,
                valueRepositoryMock,
                productValueRepositoryMock,
                imageRepositoryMock,
                subcategoryDetailRepositoryMock,
                converter,
                fileLoggerMock
        );
    }



    @Test
    void getCategoriesAndSubCategories_ShouldReturnOneCategoryAndSubCategoryDTO() {

        Category clothing = new Category(
                1,
                "Clothing",
                List.of(
                        new Subcategory(1, "Shirt", null, List.of()),
                        new Subcategory(2, "Jeans", null, List.of()),
                        new Subcategory(3, "Jacket", null, List.of())
                )
        );


        when(categoryRepositoryMock.findAll()).thenReturn(List.of(
                clothing
        ));


        List<CategoryAndSubCategoryDTO> result = productService.getCategoriesAndSubCategories();
        List<CategoryAndSubCategoryDTO> expected = List.of(
                new CategoryAndSubCategoryDTO(clothing.getName(), List.of(
                        "Shirt",
                        "Jeans",
                        "Jacket"
                ))
        );


        assertArrayEquals(expected.toArray(), result.toArray());
    }


    @Test
    void getCategoriesAndSubCategories_ShouldReturnTheExpectedSubCategoryNames() {
        Category clothing = new Category(
                1,
                "Clothing",
                List.of(
                        new Subcategory(1, "Shirt", null, List.of()),
                        new Subcategory(2, "Jeans", null, List.of()),
                        new Subcategory(3, "Jacket", null, List.of())
                )
        );


        when(categoryRepositoryMock.findAll()).thenReturn(List.of(
                clothing
        ));


        CategoryAndSubCategoryDTO result = productService.getCategoriesAndSubCategories().getFirst();

        List<String> expectedSubcategoryNames = List.of(
                "Shirt",
                "Jeans",
                "Jacket"
        );


        assertArrayEquals(expectedSubcategoryNames.toArray(), result.subcategories().toArray());
    }


    @Test
    void getCategoriesAndSubCategories_ShouldReturnTwoCategoryAndSubCategoryDTO() {

        Category clothing = new Category(
                1,
                "Clothing",
                List.of(
                        new Subcategory(1, "Shirt", null, List.of()),
                        new Subcategory(2, "Jeans", null, List.of()),
                        new Subcategory(3, "Jacket", null, List.of())
                )
        );


        Category electronic = new Category(
                2,
                "Electronic",
                List.of(
                        new Subcategory(4, "TV", null, List.of()),
                        new Subcategory(5, "Laptop", null, List.of()),
                        new Subcategory(6, "PC", null, List.of())
                )
        );

        when(categoryRepositoryMock.findAll()).thenReturn(List.of(
                clothing,
                electronic
        ));


        List<CategoryAndSubCategoryDTO> result = productService.getCategoriesAndSubCategories();
        List<CategoryAndSubCategoryDTO> expected = List.of(
                new CategoryAndSubCategoryDTO(clothing.getName(), List.of(
                        "Shirt",
                        "Jeans",
                        "Jacket"
                )),
                new CategoryAndSubCategoryDTO(electronic.getName(), List.of(
                        "TV",
                        "Laptop",
                        "PC"
                ))
        );


        assertArrayEquals(expected.toArray(), result.toArray());
    }


    @Test
    void getCategoriesAndSubCategories_ShouldReturnAnEmptyList() {
        when(categoryRepositoryMock.findAll()).thenReturn(List.of());
        List<CategoryAndSubCategoryDTO> result = productService.getCategoriesAndSubCategories();

        assertEquals(0, result.size());
    }


    @Test
    void getSubCategoriesByCategory_ShouldReturnAListWithOneElementWithShirtSubcategory() {
        Category clothing = new Category();
        clothing.setId(1);
        clothing.setName("Clothing");


        Subcategory shirt = new Subcategory(
                1,
                "Shirt",
                clothing,
                List.of()
        );

        clothing.setSubcategories(List.of(shirt));


        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(
                clothing
        ));
        when(subcategoryRepositoryMock.findAllByCategory(clothing)).thenReturn(List.of(
                shirt
        ));


        List<SubcategoriesDTO> result = productService.getSubCategoriesByCategory(1);
        List<SubcategoriesDTO> expected = List.of(
                new SubcategoriesDTO("Shirt", 1)
        );

        assertArrayEquals(expected.toArray(), result.toArray());
    }


    @Test
    void getSubCategoriesByCategory_ShouldReturnAListWithTwoElementWithShirtAndJeansSubcategory() {
        Category clothing = new Category();
        clothing.setId(1);
        clothing.setName("Clothing");


        Subcategory shirt = new Subcategory(
                1,
                "Shirt",
                clothing,
                List.of()
        );

        Subcategory jeans = new Subcategory(
                2,
                "Jeans",
                clothing,
                List.of()
        );

        clothing.setSubcategories(List.of(
                shirt,
                jeans
        ));


        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(
                clothing
        ));
        when(subcategoryRepositoryMock.findAllByCategory(clothing)).thenReturn(List.of(
                shirt,
                jeans
        ));


        List<SubcategoriesDTO> result = productService.getSubCategoriesByCategory(1);
        List<SubcategoriesDTO> expected = List.of(
                new SubcategoriesDTO("Shirt", 1),
                new SubcategoriesDTO("Jeans", 2)
        );

        assertArrayEquals(expected.toArray(), result.toArray());
    }


    @Test
    void getSubCategoriesByCategory_ShouldThrowExceptionIfCategoryNotExists() {
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        assertThrowsExactly(NonExistingProductCategoryException.class, () -> productService.getSubCategoriesByCategory(1));
    }

    @Test
    void getDetailsBySubcategory() {
    }

    @Test
    void saveProduct() {
    }
}