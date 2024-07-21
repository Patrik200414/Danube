package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.product.NonExistingProductCategoryException;
import com.danube.danube.custom_exception.product.NonExistingProductException;
import com.danube.danube.custom_exception.product.NonExistingSubcategoryException;
import com.danube.danube.custom_exception.user.InvalidUserCredentialsException;
import com.danube.danube.model.dto.product.ProductDetailUploadDTO;
import com.danube.danube.model.dto.product.ProductInformation;
import com.danube.danube.model.dto.product.ProductUpdateDTO;
import com.danube.danube.model.dto.product.ProductUploadDTO;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.connection.SubcategoryDetail;
import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.image.Image;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.product.*;
import com.danube.danube.repository.product.connection.ProductValueRepository;
import com.danube.danube.repository.product.connection.SubcategoryDetailRepository;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.utility.converter.categoriesanddetails.ProductCategoriesAndDetailsConverter;
import com.danube.danube.utility.converter.converterhelper.ConverterHelper;
import com.danube.danube.utility.converter.productview.ProductViewConverter;
import com.danube.danube.utility.converter.uploadproduct.ProductUploadConverter;
import com.danube.danube.utility.imageutility.ImageUtility;
import com.danube.danube.utility.validation.Validator;
import com.danube.danube.utility.validation.request.product.ProductRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    ProductService productService;
    ProductRepository productRepositoryMock;
    CategoryRepository categoryRepositoryMock;
    SubcategoryRepository subcategoryRepositoryMock;
    DetailRepository detailRepositoryMock;
    UserRepository userRepositoryMock;
    ValueRepository valueRepositoryMock;
    ProductValueRepository productValueRepositoryMock;
    ImageRepository imageRepositoryMock;
    SubcategoryDetailRepository subcategoryDetailRepositoryMock;
    ProductViewConverter productViewConverterMock;
    ProductCategoriesAndDetailsConverter productCategoriesAndDetailsConverterMock;
    ProductUploadConverter productUploadConverterMock;
    ImageUtility imageUtilityMock;
    ConverterHelper converterHelperMock;
    ProductRequestValidator productRequestValidatorMock;
    int SIMILAR_RECOMENDED_PRODUCTS_RESULT_COUNT = 15;

    @BeforeEach
    void setup(){
        productRepositoryMock = mock(ProductRepository.class);
        categoryRepositoryMock = mock(CategoryRepository.class);
        subcategoryRepositoryMock = mock(SubcategoryRepository.class);
        detailRepositoryMock = mock(DetailRepository.class);
        userRepositoryMock = mock(UserRepository.class);
        valueRepositoryMock = mock(ValueRepository.class);
        productValueRepositoryMock = mock(ProductValueRepository.class);
        imageRepositoryMock = mock(ImageRepository.class);
        subcategoryDetailRepositoryMock = mock(SubcategoryDetailRepository.class);
        productViewConverterMock = mock(ProductViewConverter.class);
        productCategoriesAndDetailsConverterMock = mock(ProductCategoriesAndDetailsConverter.class);
        productUploadConverterMock = mock(ProductUploadConverter.class);
        imageUtilityMock = mock(ImageUtility.class);
        converterHelperMock = mock(ConverterHelper.class);
        productRequestValidatorMock = mock(ProductRequestValidator.class);
        productService = new ProductService(
                productRepositoryMock,
                categoryRepositoryMock,
                subcategoryRepositoryMock,
                detailRepositoryMock,
                userRepositoryMock,
                valueRepositoryMock,
                productValueRepositoryMock,
                imageRepositoryMock,
                productViewConverterMock,
                productCategoriesAndDetailsConverterMock,
                productUploadConverterMock,
                imageUtilityMock,
                converterHelperMock,
                productRequestValidatorMock
        );
    }

    @Test
    void testGetSubCategoriesByCategory_WithNonExistingProduct_ThrowNonExistingProductCategoryException() {
        long expectedCategoryId = 1;
        when(categoryRepositoryMock.findById(expectedCategoryId))
                .thenReturn(Optional.empty());


        assertThrowsExactly(NonExistingProductCategoryException.class, () -> productService.getSubCategoriesByCategory(expectedCategoryId));
    }

    @Test
    void testGetSubCategoriesByCategory_WithExistingCategoryId_ShouldCallConvertSubcategoriesToSubcategoryDTOs(){
        long expectedCategoryId = 1;

        Category expectedCategory = new Category();
        expectedCategory.setId(expectedCategoryId);
        expectedCategory.setName("Electronics");
        expectedCategory.setSubcategories(List.of());


        when(categoryRepositoryMock.findById(expectedCategoryId))
                .thenReturn(Optional.of(expectedCategory));

        productService.getSubCategoriesByCategory(expectedCategoryId);

        verify(productCategoriesAndDetailsConverterMock, times(1))
                .convertSubcategoriesToSubcategoryDTOs(List.of());
    }

    @Test
    void testGetSubCategoriesByCategory_WithElectronicsCategory_ShouldCallProductCategoriesAndDetailsConverterMockConvertSubcategoriesToSubcategoryDTOs(){
        long expectedSubcategoryId = 1;
        long expectedCategoryId = 1;

        Subcategory expectedSubcategory = new Subcategory();
        expectedSubcategory.setId(expectedSubcategoryId);
        expectedSubcategory.setName("Laptop");

        List<Subcategory> expectedSubcategories = List.of(
                expectedSubcategory
        );

        Category expectedCategory = new Category();
        expectedCategory.setId(expectedCategoryId);
        expectedCategory.setName("Electronics");
        expectedCategory.setSubcategories(expectedSubcategories);


        when(categoryRepositoryMock.findById(expectedCategoryId))
                .thenReturn(Optional.of(expectedCategory));

        productService.getSubCategoriesByCategory(expectedCategoryId);

        verify(productCategoriesAndDetailsConverterMock, times(1)).convertSubcategoriesToSubcategoryDTOs(expectedSubcategories);
    }

    @Test
    void testGetDetailsBySubcategory_WithNonExistingSubcategory_ThrowsNonExistingSubcategoryException() {
        long expectedSubcategoryId = 1;

        when(subcategoryRepositoryMock.findById(expectedSubcategoryId))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingSubcategoryException.class, () -> productService.getDetailsBySubcategory(expectedSubcategoryId));
    }

    @Test
    void testGetDetailsBySubcategory_WithExpectedSubcategoryAndEmptyListOfSubcategoryDetails_ShouldCallProductCategoriesAndDetailsConverterMockConvertDetailsToDetailsDTOWithEmptyList(){
        long expectedSubcategoryId = 1;

        Subcategory expectedSubcategory = new Subcategory();
        expectedSubcategory.setId(expectedSubcategoryId);
        expectedSubcategory.setName("Laptop");
        expectedSubcategory.setSubcategoryDetails(
                List.of()
        );


        when(subcategoryRepositoryMock.findById(expectedSubcategoryId))
                .thenReturn(Optional.of(expectedSubcategory));


        productService.getDetailsBySubcategory(expectedSubcategoryId);

        verify(productCategoriesAndDetailsConverterMock, times(1)).convertDetailsToDetailsDTO(
                List.of()
        );
    }

    @Test
    void testGetDetailsBySubcategory_WithExpectedDetailsAndExpectedSubcategoryDetails_ShouldCallProductCategoriesAndDetailsConverterMockConvertDetailsToDetailsDTOWithExpectedDetails(){
        long expectedSubcategoryId = 1;

        Detail expectedDetail1 = new Detail();
        expectedDetail1.setId(1);
        expectedDetail1.setName("CPU");

        SubcategoryDetail expectedSubcategoryDetail1 = new SubcategoryDetail();
        expectedSubcategoryDetail1.setId(1);
        expectedSubcategoryDetail1.setDetail(expectedDetail1);

        Detail expectedDetail2 = new Detail();
        expectedDetail2.setId(2);
        expectedDetail2.setName("GPU");

        SubcategoryDetail expectedSubcategoryDetail2 = new SubcategoryDetail();
        expectedSubcategoryDetail2.setId(2);
        expectedSubcategoryDetail2.setDetail(expectedDetail2);

        List<SubcategoryDetail> expectedSubcategoryDetails = List.of(
                expectedSubcategoryDetail1,
                expectedSubcategoryDetail2
        );

        Subcategory expectedSubcategory = new Subcategory();
        expectedSubcategory.setId(expectedSubcategoryId);
        expectedSubcategory.setName("Laptop");
        expectedSubcategory.setSubcategoryDetails(expectedSubcategoryDetails);


        when(subcategoryRepositoryMock.findById(expectedSubcategoryId))
                .thenReturn(Optional.of(expectedSubcategory));


        productService.getDetailsBySubcategory(expectedSubcategoryId);

        verify(productCategoriesAndDetailsConverterMock, times(1)).convertDetailsToDetailsDTO(
                List.of(
                        expectedDetail1,
                        expectedDetail2
                )
        );
    }

    @Test
    void testGetSimilarProducts_WithNonExistingProductFromId_ThrowsNonExistingProductException(){
        long expectedProductFromId = 1;
        when(productRepositoryMock.findById(expectedProductFromId))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingProductException.class, () -> productService.getSimilarProducts(expectedProductFromId));
    }

    @Test
    void testGetSimilarProducts_WithExpectedSubcategoryAndExpectedSimilarProducts_ShouldCallProductRepositoryMockFindBySubcategoryAndIdNotOrderBySoldDescRatingDescAndProductViewConverterMockConvertProductsToProductShowSmallDTO() throws DataFormatException, IOException {
        long expectedProductFromId = 1;
        long expectedSubcategoryId = 1;
        PageRequest expectedPageRequest = PageRequest.of(0, SIMILAR_RECOMENDED_PRODUCTS_RESULT_COUNT);


        Subcategory expectedSubcategory = new Subcategory();
        expectedSubcategory.setId(expectedSubcategoryId);
        expectedSubcategory.setName("Test subcategory");

        Product expectedProduct = new Product();
        expectedProduct.setId(expectedProductFromId);
        expectedProduct.setProductName("Test product");
        expectedProduct.setSubcategory(expectedSubcategory);

        Product expectedSimilarProduct1 = new Product();
        expectedProduct.setId(2);
        expectedProduct.setProductName("Test similar product 1");
        expectedProduct.setSubcategory(expectedSubcategory);

        Product expectedSimilarProduct2 = new Product();
        expectedProduct.setId(3);
        expectedProduct.setProductName("Test similar product 2");
        expectedProduct.setSubcategory(expectedSubcategory);

        when(productRepositoryMock.findById(expectedProductFromId))
                .thenReturn(Optional.of(expectedProduct));

        when(productRepositoryMock.findBySubcategoryAndIdNotOrderBySoldDescRatingDesc(
                expectedSubcategory,
                expectedProductFromId,
                expectedPageRequest
        )).thenReturn(
                List.of(
                        expectedSimilarProduct1,
                        expectedSimilarProduct2
                )
        );

        productService.getSimilarProducts(expectedProductFromId);

        verify(productViewConverterMock, times(1)).convertProductsToProductShowSmallDTO(
                List.of(
                        expectedSimilarProduct1,
                        expectedSimilarProduct2
                ),
                imageUtilityMock,
                converterHelperMock
        );

        verify(productRepositoryMock, times(1)).findBySubcategoryAndIdNotOrderBySoldDescRatingDesc(
                expectedSubcategory,
                expectedProductFromId,
                expectedPageRequest
        );
    }

    @Test
    void testGetSimilarProducts_WithExpectedSubcategoryAndEmptyListOfSimilarProducts_ShouldCallProductRepositoryMockFindBySubcategoryAndIdNotOrderBySoldDescRatingDescAndProductViewConverterMockConvertProductsToProductShowSmallDTO() throws DataFormatException, IOException {
        long expectedProductFromId = 1;
        long expectedSubcategoryId = 1;
        PageRequest expectedPageRequest = PageRequest.of(0, SIMILAR_RECOMENDED_PRODUCTS_RESULT_COUNT);


        Subcategory expectedSubcategory = new Subcategory();
        expectedSubcategory.setId(expectedSubcategoryId);
        expectedSubcategory.setName("Test subcategory");

        Product expectedProduct = new Product();
        expectedProduct.setId(expectedProductFromId);
        expectedProduct.setProductName("Test product");
        expectedProduct.setSubcategory(expectedSubcategory);

        when(productRepositoryMock.findById(expectedProductFromId))
                .thenReturn(Optional.of(expectedProduct));

        when(productRepositoryMock.findBySubcategoryAndIdNotOrderBySoldDescRatingDesc(
                expectedSubcategory,
                expectedProductFromId,
                expectedPageRequest
        )).thenReturn(
                List.of()
        );

        productService.getSimilarProducts(expectedProductFromId);

        verify(productViewConverterMock, times(1)).convertProductsToProductShowSmallDTO(
                List.of(),
                imageUtilityMock,
                converterHelperMock
        );

        verify(productRepositoryMock, times(1)).findBySubcategoryAndIdNotOrderBySoldDescRatingDesc(
                expectedSubcategory,
                expectedProductFromId,
                expectedPageRequest
        );
    }

    @Test
    void testGetProductsBySubcategory_WithSubcategoryRepositoryMockReturnsEmptyOptional_ShouldThrowNonExistingSubcategoryException() throws DataFormatException, IOException {
        String searchedSubcategoryName = "Test subcategory";
        when(subcategoryRepositoryMock.findByName(searchedSubcategoryName))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingSubcategoryException.class,
                () -> productService.getProductsBySubcategory(0, 9, searchedSubcategoryName)
        );
    }

    @Test
    void testGetProductsBySubcategory_() throws DataFormatException, IOException {
        String searchedSubcategoryName = "Test subcategory";
        int expectedPageNumber = 1;
        int expectedPageSize = 5;

        Subcategory expectedSubcategory = new Subcategory();
        expectedSubcategory.setId(1);
        expectedSubcategory.setName("Test category");

        when(subcategoryRepositoryMock.findByName(searchedSubcategoryName))
                .thenReturn(Optional.of(expectedSubcategory));

        List<Product> expectedProducts = createProductList(expectedSubcategory);


        PageImpl<Product> expectedPage = new PageImpl<>(expectedProducts);
        when(productRepositoryMock.findBySubcategoryOrderByVisitNumber(expectedSubcategory, PageRequest.of(expectedPageNumber, expectedPageSize)))
                .thenReturn(expectedPage);

        productService.getProductsBySubcategory(expectedPageNumber, expectedPageSize, searchedSubcategoryName);
        verify(productViewConverterMock).convertProductToProductShowSmallDTO(expectedPage, imageUtilityMock, converterHelperMock);
    }

    @Test
    void testSaveProduct_WithUserRepositoryReturnsEmptyOptional_ShouldThrowNonExistingUserException() {
        UUID expectedUserUUID = UUID.randomUUID();

        ProductUploadDTO expectedProductUpdateDTO = getProductUploadDTO(expectedUserUUID);

        when(userRepositoryMock.findById(expectedUserUUID))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingUserException.class, () -> productService.saveProduct(expectedProductUpdateDTO));
    }

    @Test
    void testSaveProduct_WithUserRepositoryReturnsUserWithInvalidRole_ShouldThrowInvalidUserCredentialsException() throws IOException {
        UUID expectedUserUUID = UUID.randomUUID();

        ProductUploadDTO expectedProductUpdateDTO = getProductUploadDTO(expectedUserUUID);

        UserEntity expectedUser = new UserEntity();
        expectedUser.setId(expectedUserUUID);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");
        expectedUser.setRoles(
                Set.of(Role.ROLE_CUSTOMER)
        );

        when(userRepositoryMock.findById(expectedUserUUID))
                .thenReturn(Optional.of(expectedUser));

        assertThrowsExactly(InvalidUserCredentialsException.class, () -> productService.saveProduct(expectedProductUpdateDTO));
    }

    @Test
    void testSaveProduct_WithSubcategoryRepositoryReturnsEmptyOptional_ShouldThrowNonExistingSubcategoryException() throws IOException {
        UUID expectedUserUUID = UUID.randomUUID();
        long expectedSubcategoryId = 1;

        ProductUploadDTO expectedProductUpdateDTO = getProductUploadDTO(expectedUserUUID);

        UserEntity expectedUser = new UserEntity();
        expectedUser.setId(expectedUserUUID);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");
        expectedUser.setRoles(
                Set.of(
                        Role.ROLE_CUSTOMER,
                        Role.ROLE_SELLER
                )
        );

        when(userRepositoryMock.findById(expectedUserUUID))
                .thenReturn(Optional.of(expectedUser));

        when(subcategoryRepositoryMock.findById(expectedSubcategoryId))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingSubcategoryException.class, () -> productService.saveProduct(expectedProductUpdateDTO));
    }

    @Test
    void testSaveProduct_With() throws IOException {
        UUID expectedUserUUID = UUID.randomUUID();
        long expectedSubcategoryId = 1;

        ProductUploadDTO expectedProductUpdateDTO = getProductUploadDTO(expectedUserUUID);

        UserEntity expectedUser = new UserEntity();
        expectedUser.setId(expectedUserUUID);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");
        expectedUser.setRoles(
                Set.of(
                        Role.ROLE_CUSTOMER,
                        Role.ROLE_SELLER
                )
        );

        Subcategory expectedSubcategory = createBasicSubcategory(1, "Test subcategory");

        when(userRepositoryMock.findById(expectedUserUUID))
                .thenReturn(Optional.of(expectedUser));

        when(subcategoryRepositoryMock.findById(expectedSubcategoryId))
                .thenReturn(Optional.of(expectedSubcategory));

        Product expectedProduct = new Product();

        when(productUploadConverterMock.convertProductDetailUploadDTOToProduct(expectedProductUpdateDTO.productDetail(), expectedUser, expectedSubcategory))
                .thenReturn(expectedProduct);

        Image expectedImage = new Image();
        List<Image> expectedImages = List.of(expectedImage);

        when(productUploadConverterMock.convertMultiPartFilesToListOfImages(expectedProductUpdateDTO.images(), expectedProduct, imageUtilityMock))
                .thenReturn(
                        expectedImages
                );

        expectedProduct.setImages(expectedImages);

        productService.saveProduct(expectedProductUpdateDTO);

        verify(productUploadConverterMock).convertProductDetailUploadDTOToProduct(
                expectedProductUpdateDTO.productDetail(),
                expectedUser,
                expectedSubcategory
        );

        verify(productUploadConverterMock).convertMultiPartFilesToListOfImages(
                expectedProductUpdateDTO.images(),
                expectedProduct,
                imageUtilityMock
        );

        verify(imageRepositoryMock).saveAll(expectedImages);

        verify(productRepositoryMock).save(expectedProduct);

        verify(valueRepositoryMock).saveAll(List.of());

        verify(productValueRepositoryMock).saveAll(List.of());
    }

    private Subcategory createBasicSubcategory(long subcategoryId, String subcategoryName){
        Subcategory subcategory = new Subcategory();
        subcategory.setId(subcategoryId);
        subcategory.setName(subcategoryName);

        return subcategory;
    }

    private ProductUploadDTO getProductUploadDTO(UUID expectedUserUUID) {
        ProductDetailUploadDTO expectedProductDetailUploadDTO = new ProductDetailUploadDTO(
                2,
                2,
                100,
                3,
                "Test brand",
                "Test description",
                "Test product",
                1
        );
        return new ProductUploadDTO(
                expectedProductDetailUploadDTO,
                Map.of(),
                expectedUserUUID,
                new MultipartFile[10]
        );
    }

    private List<Product> createProductList(Subcategory expectedSubcategory) {
        List<Product> expectedProducts = new ArrayList<>();

        for(int i = 1; i < 4; i++){
            Product expectedProduct1 = new Product();
            expectedProduct1.setSubcategory(expectedSubcategory);
            expectedProduct1.setId(1);
            expectedProduct1.setProductName("Test product" + i);

            expectedProducts.add(expectedProduct1);
        }
        return expectedProducts;
    }

}