package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.product.*;
import com.danube.danube.custom_exception.user.InvalidUserCredentialsException;
import com.danube.danube.custom_exception.user.UserNotSellerException;
import com.danube.danube.model.dto.product.*;
import com.danube.danube.model.product.Product;
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
import com.danube.danube.utility.filellogger.FileLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
    /*
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
                        new Subcategory(1, "Shirt", null, List.of(), List.of()),
                        new Subcategory(2, "Jeans", null, List.of(), List.of()),
                        new Subcategory(3, "Jacket", null, List.of(), List.of())
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
                        new Subcategory(1, "Shirt", null, List.of(), List.of()),
                        new Subcategory(2, "Jeans", null, List.of(), List.of()),
                        new Subcategory(3, "Jacket", null, List.of(), List.of())
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
                        new Subcategory(1, "Shirt", null, List.of(), List.of()),
                        new Subcategory(2, "Jeans", null, List.of(), List.of()),
                        new Subcategory(3, "Jacket", null, List.of(), List.of())
                )
        );


        Category electronic = new Category(
                2,
                "Electronic",
                List.of(
                        new Subcategory(4, "TV", null, List.of(), List.of()),
                        new Subcategory(5, "Laptop", null, List.of(), List.of()),
                        new Subcategory(6, "PC", null, List.of(), List.of())
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
                List.of(),
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
                List.of(),
                List.of()
        );

        Subcategory jeans = new Subcategory(
                2,
                "Jeans",
                clothing,
                List.of(),
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
                List.of(shirtSubcategoryDetail),
                List.of()
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
                List.of(shirtSubcategoryDetail),
                List.of()
        );

        Subcategory jumper = new Subcategory(
                2,
                "Jumper",
                new Category(),
                List.of(jumperSubcategoryDetail, shirtSubcategoryDetail),
                List.of()
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
                List.of(shirtSubcategoryDetail),
                List.of()
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
                "Product",
                1
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
                "Product",
                1
        );

        ProductUploadDTO productUploadDTO = new ProductUploadDTO(
                productDetailUploadDTO,
                new HashMap<>(),
                1,
                new MultipartFile[3]
        );

        assertThrowsExactly(InvalidUserCredentialsException.class, () -> productService.saveProduct(productUploadDTO));
    }

    @Test
    void getMyProducts_ShouldReturnMapWithWithTheExpectedProductInformation(){
        UserEntity expectedSeller = new UserEntity();
        expectedSeller.setId(1);
        expectedSeller.setPassword("thispassword");
        expectedSeller.setEmail("seller@gmail.com");
        expectedSeller.setFirstName("Seller");
        expectedSeller.setLastName("Seller");
        expectedSeller.setRoles(Set.of(Role.ROLE_CUSTOMER, Role.ROLE_SELLER));


        Product expectedProduct = new Product();
        expectedProduct.setQuantity(10);
        expectedProduct.setProductName("Test");
        expectedProduct.setImages(List.of());
        expectedProduct.setBrand("Test");
        expectedProduct.setDescription("This is a test");
        expectedProduct.setPrice(5);
        expectedProduct.setShippingPrice(5);
        expectedProduct.setSeller(expectedSeller);

        expectedSeller.setProducts(
                List.of(expectedProduct)
        );

        when(userRepositoryMock.findById(1L))
                .thenReturn(
                        Optional.of(expectedSeller)
                );

        when(productRepositoryMock.findBySeller(expectedSeller))
                .thenReturn(
                        List.of(expectedProduct)
                );


        Map<String, String> expected = converter.convertProductToMyProductInformation(expectedProduct);
        List<Map<String, String>> result = productService.getMyProducts(1L);

        assertEquals(1, result.size());
        assertEquals(expected.get("Product image"), result.getFirst().get("Product image"));
        assertEquals(expected.get("Product name"), result.getFirst().get("Product name"));
        assertEquals(expected.get("Owner"), result.getFirst().get("Owner"));
        assertEquals(expected.get("id"), result.getFirst().get("id"));
    }


    @Test
    void getMyProducts_WithUserRepositoryReturnsUserWithOnlyCustomerRole_ShouldThrowUserNotSellerException(){
        UserEntity expectedSeller = new UserEntity();
        expectedSeller.setId(1);
        expectedSeller.setPassword("thispassword");
        expectedSeller.setEmail("seller@gmail.com");
        expectedSeller.setFirstName("Seller");
        expectedSeller.setLastName("Seller");
        expectedSeller.setRoles(Set.of(Role.ROLE_CUSTOMER));

        when(userRepositoryMock.findById(1L))
                .thenReturn(
                        Optional.of(expectedSeller)
                );

        assertThrowsExactly(UserNotSellerException.class, () -> productService.getMyProducts(1));
    }

    @Test
    void getMyProducts_WithUserRepositoryReturnsEmptyOptional_ShouldThrowNonExistingUserException(){
        when(userRepositoryMock.findById(1L))
                .thenReturn(
                        Optional.empty()
                );

        assertThrowsExactly(NonExistingUserException.class, () -> productService.getMyProducts(1));
    }

    @Test
    void getCategoriesAndSubCategories_ShouldReturnOneCategoryAndSubCategoryDTOWithExpectedCategoryAndSubcategoryData(){
        Subcategory subcategory1 = new Subcategory();
        Subcategory subcategory2 = new Subcategory();
        Subcategory subcategory3 = new Subcategory();

        subcategory1.setName("Shirt");
        subcategory2.setName("Jeans");
        subcategory3.setName("Jacket");

        Category expectedCategory = new Category(
                1,
                "Clothing",
                List.of(
                        subcategory1,
                        subcategory2,
                        subcategory3
                )
        );

        when(categoryRepositoryMock.findAll())
                .thenReturn(List.of(
                        expectedCategory
                ));

        CategoryAndSubCategoryDTO expected = new CategoryAndSubCategoryDTO(
                expectedCategory.getName(),
                List.of(
                        subcategory1.getName(),
                        subcategory2.getName(),
                        subcategory3.getName()
                )
        );
        List<CategoryAndSubCategoryDTO> result = productService.getCategoriesAndSubCategories();

        assertEquals(1, result.size());
        assertEquals(expected, result.getFirst());
    }

    @Test
    void getCategoriesAndSubCategories_ShouldReturnTwoCategoryAndSubCategoryDTOsWithExpectedCategoriesAndSubcategoryDatas(){
        Subcategory subcategory1 = new Subcategory();
        Subcategory subcategory2 = new Subcategory();
        Subcategory subcategory3 = new Subcategory();

        Subcategory subcategory4 = new Subcategory();
        Subcategory subcategory5 = new Subcategory();
        Subcategory subcategory6 = new Subcategory();

        subcategory1.setName("Shirt");
        subcategory2.setName("Jeans");
        subcategory3.setName("Jacket");

        subcategory4.setName("Screen size");
        subcategory5.setName("Display quality");
        subcategory6.setName("Technology");

        Category expectedCategory1 = new Category(
                1,
                "Clothing",
                List.of(
                        subcategory1,
                        subcategory2,
                        subcategory3
                )
        );


        Category expectedCategory2 = new Category(
                2,
                "Monitor",
                List.of(
                        subcategory4,
                        subcategory5,
                        subcategory6
                )
        );


        when(categoryRepositoryMock.findAll())
                .thenReturn(List.of(
                        expectedCategory1,
                        expectedCategory2
                ));

        CategoryAndSubCategoryDTO expectedCategorySubcategory1 = new CategoryAndSubCategoryDTO(
                expectedCategory1.getName(),
                List.of(
                        subcategory1.getName(),
                        subcategory2.getName(),
                        subcategory3.getName()
                )
        );
        CategoryAndSubCategoryDTO expectedCategorySubcategory2 = new CategoryAndSubCategoryDTO(
                expectedCategory2.getName(),
                List.of(
                        subcategory4.getName(),
                        subcategory5.getName(),
                        subcategory6.getName()
                )
        );

        List<CategoryAndSubCategoryDTO> expected = List.of(expectedCategorySubcategory1, expectedCategorySubcategory2);
        List<CategoryAndSubCategoryDTO> result = productService.getCategoriesAndSubCategories();


        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    void updateProduct_withUserRepositoryReturnsEmptyOptionalShouldThrowNonExistingUserException(){
        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.empty());

        UserEntity seller = new UserEntity();
        seller.setRoles(Set.of(Role.ROLE_CUSTOMER, Role.ROLE_SELLER));
        seller.setEmail("seller@gmail.com");
        seller.setId(1);
        seller.setLastName("Seller");
        seller.setFirstName("Seller");



        ProductInformation productInformation = new ProductInformation(
                "Shirt",
                10,
                10,
                10,
                3,
                5,
                100,
                "Test",
                "This is a test shirt",
                seller.getFullName(),
                1
        );
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO(
                productInformation,
                List.of("Image"),
                List.of()
        );

        MultipartFile[] multipartFiles = new MultipartFile[1];

        assertThrowsExactly(NonExistingUserException.class, () -> productService.updateProduct(productUpdateDTO, multipartFiles, seller.getId(), 1));
    }

    @Test
    void updateProduct_withUserRepositoryReturnsUserWithOnlyCustomerRoleShoulThrowInvalidUserCredentialsException(){
        UserEntity seller = new UserEntity();
        seller.setRoles(Set.of(Role.ROLE_CUSTOMER));
        seller.setEmail("seller@gmail.com");
        seller.setId(1);
        seller.setLastName("Seller");
        seller.setFirstName("Seller");

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.of(
                        seller
                ));

        ProductInformation productInformation = new ProductInformation(
                "Shirt",
                10,
                10,
                10,
                3,
                5,
                100,
                "Test",
                "This is a test shirt",
                seller.getFullName(),
                1
        );
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO(
                productInformation,
                List.of("Image"),
                List.of()
        );

        MultipartFile[] multipartFiles = new MultipartFile[1];

        assertThrowsExactly(InvalidUserCredentialsException.class, () -> productService.updateProduct(productUpdateDTO, multipartFiles, seller.getId(), 1));
    }

    @Test
    void updateProduct_withProductRepositoryReturnsEmptyOptionalShouldThrowNonExistingProductException(){
        UserEntity seller = new UserEntity();
        seller.setRoles(Set.of(Role.ROLE_CUSTOMER, Role.ROLE_SELLER));
        seller.setEmail("seller@gmail.com");
        seller.setId(1);
        seller.setLastName("Seller");
        seller.setFirstName("Seller");

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.of(
                        seller
                ));

        when(productRepositoryMock.findById(1L))
                .thenReturn(Optional.empty());


        ProductInformation productInformation = new ProductInformation(
                "Shirt",
                10,
                10,
                10,
                3,
                5,
                100,
                "Test",
                "This is a test shirt",
                seller.getFullName(),
                1
        );
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO(
                productInformation,
                List.of("Image"),
                List.of()
        );

        MultipartFile[] multipartFiles = new MultipartFile[1];

        assertThrowsExactly(NonExistingProductException.class, () -> productService.updateProduct(productUpdateDTO, multipartFiles, seller.getId(), 1));
    }

    @Test
    void updateProduct_withUserIdAndProductSellerIdNotMatchingShouldThrowIncorrectSellerException(){
        UserEntity notCorrectSeller = new UserEntity();
        notCorrectSeller.setRoles(Set.of(Role.ROLE_CUSTOMER, Role.ROLE_SELLER));
        notCorrectSeller.setEmail("seller@gmail.com");
        notCorrectSeller.setId(1);
        notCorrectSeller.setLastName("Seller");
        notCorrectSeller.setFirstName("Seller");

        UserEntity correctSeller = new UserEntity();
        correctSeller.setId(2);
        correctSeller.setLastName("Correct");
        correctSeller.setFirstName("Correct");

        Product product = new Product();
        product.setSeller(correctSeller);

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.of(
                        notCorrectSeller
                ));



        when(productRepositoryMock.findById(1L))
                .thenReturn(Optional.of(product));


        ProductInformation productInformation = new ProductInformation(
                "Shirt",
                10,
                10,
                10,
                3,
                5,
                100,
                "Test",
                "This is a test shirt",
                correctSeller.getFullName(),
                1
        );
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO(
                productInformation,
                List.of("Image"),
                List.of()
        );

        MultipartFile[] multipartFiles = new MultipartFile[1];

        assertThrowsExactly(IncorrectSellerException.class, () -> productService.updateProduct(productUpdateDTO, multipartFiles, notCorrectSeller.getId(), 1));
    }

    @Test
    void updateProduct_withNoImageUploadedShouldThrowMissingImageException(){
        UserEntity seller = new UserEntity();
        seller.setRoles(Set.of(Role.ROLE_CUSTOMER, Role.ROLE_SELLER));
        seller.setEmail("seller@gmail.com");
        seller.setId(1);
        seller.setLastName("Seller");
        seller.setFirstName("Seller");

        Product product = new Product();
        product.setId(1);
        product.setSeller(seller);

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.of(
                        seller
                ));

        when(productRepositoryMock.findById(1L))
                .thenReturn(Optional.of(
                        product
                ));


        ProductInformation productInformation = new ProductInformation(
                "Shirt",
                10,
                10,
                10,
                3,
                5,
                100,
                "Test",
                "This is a test shirt",
                seller.getFullName(),
                1
        );
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO(
                productInformation,
                List.of(),
                List.of()
        );

        MultipartFile[] multipartFiles = null;

        assertThrowsExactly(MissingImageException.class, () -> productService.updateProduct(productUpdateDTO, multipartFiles, seller.getId(), 1));
    }

     */
}