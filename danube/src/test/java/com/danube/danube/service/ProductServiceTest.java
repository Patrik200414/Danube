package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.product.NonExistingProductCategoryException;
import com.danube.danube.custom_exception.product.NonExistingSubcategoryException;
import com.danube.danube.custom_exception.user.InvalidUserCredentialsException;
import com.danube.danube.model.dto.product.*;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.connection.SubcategoryDetail;
import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.product.*;
import com.danube.danube.repository.product.connection.ProductValueRepository;
import com.danube.danube.repository.product.connection.SubcategoryDetailRepository;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.utility.Converter;
import com.danube.danube.utility.ConverterImpl;
import com.danube.danube.utility.filellogger.FileLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

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
    void getDetailsBySubcategory_WithShirtSubCategoryAndWithOneSizeDetail_ShouldReturnExpectedDetailDTOListWithOneElement() {
        SubcategoryDetail shirtSubcategoryDetail = new SubcategoryDetail();

        Detail sizeDetail = new Detail(
                1,
                "Size",
                List.of(shirtSubcategoryDetail),
                List.of()
        );

        Subcategory shirt = new Subcategory(
                1,
                "Shirt",
                new Category(),
                List.of(shirtSubcategoryDetail)
        );

        shirtSubcategoryDetail.setId(1);
        shirtSubcategoryDetail.setSubcategory(shirt);
        shirtSubcategoryDetail.setDetail(sizeDetail);


        when(subcategoryRepositoryMock.findById(1L)).thenReturn(
                Optional.of(shirt)
        );
        when(subcategoryDetailRepositoryMock.findAllBySubcategory(shirt)).thenReturn(
                List.of(shirtSubcategoryDetail)
        );

        List<DetailDTO> result = productService.getDetailsBySubcategory(1);
        List<DetailDTO> expected = List.of(
                new DetailDTO(
                        sizeDetail.getName(),
                        sizeDetail.getId(),
                        ""
                )
        );


        assertArrayEquals(expected.toArray(), result.toArray());
    }


    @Test
    void getDetailsBySubcategory_WithShirtSubCategoryAndJumperSubcategoryAndWithSizeDetailAndHoodieDetail_ShouldReturnExpectedDetailDTOListWithBothSizeDetailAndHoodieDetail() {
        SubcategoryDetail shirtSubcategoryDetail = new SubcategoryDetail();
        SubcategoryDetail jumperSubcategoryDetail = new SubcategoryDetail();

        Detail sizeDetail = new Detail(
                1,
                "Size",
                List.of(shirtSubcategoryDetail),
                List.of()
        );

        Detail hoodieDetail = new Detail(
                2,
                "Has hoodie",
                List.of(jumperSubcategoryDetail),
                List.of()
        );

        Subcategory shirt = new Subcategory(
                1,
                "Shirt",
                new Category(),
                List.of(shirtSubcategoryDetail)
        );

        Subcategory jumper = new Subcategory(
                2,
                "Jumper",
                new Category(),
                List.of(jumperSubcategoryDetail, shirtSubcategoryDetail)
        );

        shirtSubcategoryDetail.setId(1);
        shirtSubcategoryDetail.setSubcategory(shirt);
        shirtSubcategoryDetail.setDetail(sizeDetail);

        jumperSubcategoryDetail.setId(2);
        jumperSubcategoryDetail.setSubcategory(jumper);
        jumperSubcategoryDetail.setDetail(hoodieDetail);


        when(subcategoryRepositoryMock.findById(2L)).thenReturn(
                Optional.of(jumper)
        );
        when(subcategoryDetailRepositoryMock.findAllBySubcategory(jumper)).thenReturn(
                List.of(jumperSubcategoryDetail, shirtSubcategoryDetail)
        );

        List<DetailDTO> result = productService.getDetailsBySubcategory(2);
        List<DetailDTO> expected = List.of(
                new DetailDTO(
                        hoodieDetail.getName(),
                        hoodieDetail.getId(),
                        ""
                ),
                new DetailDTO(
                        sizeDetail.getName(),
                        sizeDetail.getId(),
                        ""
                )
        );

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    void getDetailsBySubcategory_WithSubcategoryRepositoryShouldReturnEmptyOptional_ShouldThrowNonExistingSubcategoryException(){
        when(subcategoryRepositoryMock.findById(2L)).thenReturn(
                Optional.empty()
        );


        assertThrowsExactly(NonExistingSubcategoryException.class, () -> productService.getDetailsBySubcategory(2L));
    }

    @Test
    void getDetailsBySubcategory_WithSubcategoryDetailRepositoryIsNull_ShouldReturnEmptyListOfDetailDTO(){
        SubcategoryDetail shirtSubcategoryDetail = new SubcategoryDetail();

        Detail sizeDetail = new Detail(
                1,
                "Size",
                List.of(shirtSubcategoryDetail),
                List.of()
        );

        Subcategory shirt = new Subcategory(
                1,
                "Shirt",
                new Category(),
                List.of(shirtSubcategoryDetail)
        );

        shirtSubcategoryDetail.setId(1);
        shirtSubcategoryDetail.setSubcategory(shirt);
        shirtSubcategoryDetail.setDetail(sizeDetail);


        when(subcategoryRepositoryMock.findById(1L)).thenReturn(
                Optional.of(shirt)
        );

        List<DetailDTO> result = productService.getDetailsBySubcategory(1);
        assertTrue(result.isEmpty());
    }



    @Test
    void saveProduct_WithUserRepositoryReturnsEmptyOptional_ShouldThrowNonExistingUserException() {
        when(userRepositoryMock.findById(1L)).thenReturn(
                Optional.empty()
        );

        ProductDetailUploadDTO productDetailUploadDTO = new ProductDetailUploadDTO(
                1,
                10,
                10,
                10,
                "Brand",
                "This is a product",
                "Product"
        );

        ProductUploadDTO productUploadDTO = new ProductUploadDTO(
                productDetailUploadDTO,
                new HashMap<>(),
                1,
                new MultipartFile[3]
        );

        assertThrowsExactly(NonExistingUserException.class, () -> productService.saveProduct(productUploadDTO));
    }

    @Test
    void saveProduct_WithUserRepositoryReturnsUserWithOnlyCustomerRole_ShouldThrowInvalidUserCredentialsException(){
        UserEntity user = new UserEntity();
        user.setRoles(Set.of(Role.ROLE_CUSTOMER));

        when(userRepositoryMock.findById(1L)).thenReturn(
                Optional.of(user)
        );

        ProductDetailUploadDTO productDetailUploadDTO = new ProductDetailUploadDTO(
                1,
                10,
                10,
                10,
                "Brand",
                "This is a product",
                "Product"
        );

        ProductUploadDTO productUploadDTO = new ProductUploadDTO(
                productDetailUploadDTO,
                new HashMap<>(),
                1,
                new MultipartFile[3]
        );

        assertThrowsExactly(InvalidUserCredentialsException.class, () -> productService.saveProduct(productUploadDTO));
    }
}