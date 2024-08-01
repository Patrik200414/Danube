package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.product.*;
import com.danube.danube.custom_exception.user.InvalidUserCredentialsException;
import com.danube.danube.custom_exception.user.UserNotSellerException;
import com.danube.danube.model.dto.image.ImageShow;
import com.danube.danube.model.dto.product.*;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.connection.ProductValue;
import com.danube.danube.model.product.connection.SubcategoryDetail;
import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.image.Image;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.model.product.value.Value;
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
import com.danube.danube.utility.validation.request.product.ProductRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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
        Category expectedCategory = getCategory(expectedCategoryId, "Electronics", List.of());

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

        Subcategory expectedSubcategory = getSubcategory(expectedSubcategoryId, "Laptop");
        List<Subcategory> expectedSubcategories = List.of(
                expectedSubcategory
        );

        Category expectedCategory = getCategory(expectedCategoryId, "Electronics", expectedSubcategories);


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

        Subcategory expectedSubcategory = getSubcategory(expectedSubcategoryId, "Laptop");
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

        Detail expectedDetail1 = getDetail(1, "CPU");
        SubcategoryDetail expectedSubcategoryDetail1 = getSubcategoryDetail(1, expectedDetail1);

        Detail expectedDetail2 = getDetail(2, "GPU");
        SubcategoryDetail expectedSubcategoryDetail2 = getSubcategoryDetail(2, expectedDetail2);


        List<SubcategoryDetail> expectedSubcategoryDetails = List.of(
                expectedSubcategoryDetail1,
                expectedSubcategoryDetail2
        );

        Subcategory expectedSubcategory = getSubcategory(expectedSubcategoryId, "Laptop");
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

        Subcategory expectedSubcategory = getSubcategory(expectedSubcategoryId, "Test subcategory");


        Product expectedProduct = getProduct(expectedProductFromId, "Test product",expectedSubcategory);

        Product expectedSimilarProduct1 = getProduct(
                2,
                "Test similar product 1",
                expectedSubcategory
        );

        Product expectedSimilarProduct2 = getProduct(
                3,
                "Test similar product 2",
                expectedSubcategory
        );

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

        Subcategory expectedSubcategory = getSubcategory(
                expectedSubcategoryId,
                "Test subcategory"
        );

        Product expectedProduct = getProduct(
                expectedProductFromId,
                "Test product",
                expectedSubcategory
        );

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

        Subcategory expectedSubcategory = getSubcategory(1, "Test category");

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

        ProductUploadDTO expectedProductUploadDTO = getProductUploadDTO(expectedUserUUID, Map.of());

        when(userRepositoryMock.findById(expectedUserUUID))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingUserException.class, () -> productService.saveProduct(expectedProductUploadDTO));
    }

    @Test
    void testSaveProduct_WithUserRepositoryReturnsUserWithInvalidRole_ShouldThrowInvalidUserCredentialsException() throws IOException {
        UUID expectedUserUUID = UUID.randomUUID();

        ProductUploadDTO expectedProductUploadDTO = getProductUploadDTO(expectedUserUUID, Map.of());

        UserEntity expectedUser = getExpectedUserEntity(expectedUserUUID);

        when(userRepositoryMock.findById(expectedUserUUID))
                .thenReturn(Optional.of(expectedUser));

        assertThrowsExactly(InvalidUserCredentialsException.class, () -> productService.saveProduct(expectedProductUploadDTO));
    }

    @Test
    void testSaveProduct_WithSubcategoryRepositoryReturnsEmptyOptional_ShouldThrowNonExistingSubcategoryException() throws IOException {
        UUID expectedUserUUID = UUID.randomUUID();
        long expectedSubcategoryId = 1;

        ProductUploadDTO expectedProductUploadDTO = getProductUploadDTO(expectedUserUUID, Map.of());

        UserEntity expectedUser = getExpectedUserEntity(
                expectedUserUUID,
                "Test",
                "User",
                Set.of(
                        Role.ROLE_CUSTOMER,
                        Role.ROLE_SELLER
                )
        );

        when(userRepositoryMock.findById(expectedUserUUID))
                .thenReturn(Optional.of(expectedUser));

        when(subcategoryRepositoryMock.findById(expectedSubcategoryId))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingSubcategoryException.class, () -> productService.saveProduct(expectedProductUploadDTO));
    }

    @Test
    void testSaveProduct_With() throws IOException {
        UUID expectedUserUUID = UUID.randomUUID();
        Map<String, String> expectedProductInformation = Map.of("Test", "Information");
        long expectedSubcategoryId = 1;

        ProductUploadDTO expectedProductUploadDTO = getProductUploadDTO(expectedUserUUID, expectedProductInformation);
        UserEntity expectedUser = getExpectedUserEntity(
                expectedUserUUID,
                "Test",
                "User",
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

        Product expectedProduct = productUploadDTOConverterToProduct(expectedProductUploadDTO);

        when(productUploadConverterMock.convertProductDetailUploadDTOToProduct(expectedProductUploadDTO.productDetail(), expectedUser, expectedSubcategory))
                .thenReturn(expectedProduct);

        Image expectedImage = new Image();
        List<Image> expectedImages = List.of(expectedImage);

        when(productUploadConverterMock.convertMultiPartFilesToListOfImages(expectedProductUploadDTO.images(), expectedProduct, imageUtilityMock))
                .thenReturn(
                        expectedImages
                );

        expectedProduct.setImages(expectedImages);

        Detail expectedDetail = getDetail(1, "Test Detail");

        when(detailRepositoryMock.findAllByNameIn(expectedProductInformation.keySet()))
                .thenReturn(List.of(
                        expectedDetail
                ));

        String expectedDetailValueName = expectedProductUploadDTO.productInformation().values().stream().toList().getFirst();
        Value expectedValue = getValue(expectedDetailValueName, expectedDetail);

        ProductValue expectedproductValue = getProductValue(expectedProduct, expectedValue);

        productService.saveProduct(expectedProductUploadDTO);

        verify(productUploadConverterMock).convertProductDetailUploadDTOToProduct(
                expectedProductUploadDTO.productDetail(),
                expectedUser,
                expectedSubcategory
        );

        verify(productUploadConverterMock).convertMultiPartFilesToListOfImages(
                expectedProductUploadDTO.images(),
                expectedProduct,
                imageUtilityMock
        );

        verify(imageRepositoryMock).saveAll(expectedImages);

        verify(productRepositoryMock).save(expectedProduct);

        verify(valueRepositoryMock).saveAll(List.of(
                expectedValue
        ));

        verify(productValueRepositoryMock).saveAll(List.of(
                expectedproductValue
        ));
    }

    @Test
    void testGetMyProducts_WithNonExistingUser_ShouldThrowNonExistingUserException(){
        UUID expectedUserId = UUID.randomUUID();
        when(userRepositoryMock.findById(expectedUserId))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingUserException.class, () -> productService.getMyProducts(expectedUserId));
    }

    @Test
    void testGetMyProducts_UserWithOnlyCustomerRole_ShouldThrowUserNotSellerException(){
        UUID expectedUserId = UUID.randomUUID();

        UserEntity expectedUser = getExpectedUserEntity(
                expectedUserId,
                "Test",
                "User",
                Set.of(Role.ROLE_CUSTOMER)
        );
        when(userRepositoryMock.findById(expectedUserId))
                .thenReturn(Optional.of(expectedUser));

        assertThrowsExactly(UserNotSellerException.class, () -> productService.getMyProducts(expectedUserId));
    }

    @Test
    void testGetMyProducts_WithCorrectValues_ShouldCallProductRepositoryFindBySellerAndProductViewConverterConvertProductToMyProductInformation() throws DataFormatException, IOException {
        UUID expectedUserId = UUID.randomUUID();

        UserEntity expectedUser = getExpectedUserEntity(
                expectedUserId,
                "Test",
                "User",
                Set.of(
                        Role.ROLE_SELLER
                )
        );
        when(userRepositoryMock.findById(expectedUserId))
                .thenReturn(Optional.of(expectedUser));

        Subcategory expectedSubcategory = getSubcategory(1, "Test subcategory");
        List<Product> expectedProducts = List.of(
                getProduct(1, "Test 1", expectedSubcategory),
                getProduct(2, "Test 2", expectedSubcategory)
        );
        when(productRepositoryMock.findBySeller(expectedUser))
                .thenReturn(expectedProducts);

        productService.getMyProducts(expectedUserId);

        verify(productRepositoryMock).findBySeller(expectedUser);
        for(Product expectedProduct : expectedProducts){
            verify(productViewConverterMock).convertProductToMyProductInformation(expectedProduct, imageUtilityMock);
        }
    }

    @Test
    void testGetProductItem_WithNonExistingProduct_ShouldThrowNonExistingProductException(){
        long expectedProductId = 1;
        when(productRepositoryMock.findById(expectedProductId))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingProductException.class, () -> productService.getProductItem(expectedProductId));
    }

    @Test
    void testGetProductItem_WithExistingProduct_ShouldIncreaseVisitNumberAndSaveProduct() throws DataFormatException, IOException {
        long expectedProductId = 1;
        Subcategory expectedSubcategory = getSubcategory(1, "Test Subcategory");
        Product expectedProduct = getProduct(expectedProductId, "Test", expectedSubcategory);
        when(productRepositoryMock.findById(expectedProductId))
                .thenReturn(Optional.of(expectedProduct));

        expectedProduct.setVisitNumber(1);
        productService.getProductItem(expectedProductId);

        verify(productRepositoryMock).save(expectedProduct);
        verify(productViewConverterMock).convertProductToProductItemDTO(expectedProduct, imageUtilityMock, converterHelperMock);
    }

    @Test
    void testUpdateProduct_WithNonExistingUser_ShouldThrowNonExistingUserException(){
        UUID expectedUserId = UUID.randomUUID();
        long updatableProductId = 1;

        when(userRepositoryMock.findById(expectedUserId))
                .thenReturn(Optional.empty());

        ProductUpdateDTO expectedProductUpdateDTO = getProductUpdateDTO("Test");
        assertThrowsExactly(NonExistingUserException.class,
                () -> productService.updateProduct(
                        expectedProductUpdateDTO,
                        new MultipartFile[1],
                        expectedUserId,
                        updatableProductId
                )
        );
    }

    @Test
    void testUpdateProduct_UserOnlyHasCustomerRole_ShouldThrowInvalidUserCredentialsException(){
        UUID expectedUserId = UUID.randomUUID();
        long expectedUpdatableProductId = 1;

        UserEntity expectedUser = getExpectedUserEntity(expectedUserId);

        ProductUpdateDTO expectedProductUpdateDTO = getProductUpdateDTO(expectedUser.getFullName());

        when(userRepositoryMock.findById(expectedUserId))
                .thenReturn(Optional.of(expectedUser));

        assertThrowsExactly(InvalidUserCredentialsException.class,
                () -> productService.updateProduct(
                        expectedProductUpdateDTO,
                        new MultipartFile[1],
                        expectedUserId,
                        expectedUpdatableProductId
                )
        );
    }

    @Test
    void testUpdateProduct_WithNonExistingProduct_ShouldThrowNonExistingProductException(){
        UUID expectedUserId = UUID.randomUUID();
        long expectedUpdatableProductId = 1;

        UserEntity expectedUser = getExpectedUserEntity(
                expectedUserId,
                "Test",
                "User",
                Set.of(
                        Role.ROLE_SELLER
                )
        );
        ProductUpdateDTO expectedProductUpdateDTO = getProductUpdateDTO(expectedUser.getFullName());

        when(userRepositoryMock.findById(expectedUserId))
                .thenReturn(Optional.of(expectedUser));

        when(productRepositoryMock.findById(expectedUpdatableProductId))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingProductException.class,
                () -> productService.updateProduct(
                        expectedProductUpdateDTO,
                        new MultipartFile[1],
                        expectedUserId,
                        expectedUpdatableProductId
                )
        );
    }

    @Test
    void testUpdateProduct_WithUserIdAndProductSellerIdNotTheSame_ShouldThrowIncorrectSellerException(){
        UUID expectedUserId = UUID.randomUUID();
        long expectedUpdatableProductId = 1;

        UserEntity expectedUser = getExpectedUserEntity(
                expectedUserId,
                "Test",
                "User",
                Set.of(
                        Role.ROLE_SELLER
                )
        );
        ProductUpdateDTO expectedProductUpdateDTO = getProductUpdateDTO(expectedUser.getFullName());

        when(userRepositoryMock.findById(expectedUserId))
                .thenReturn(Optional.of(expectedUser));


        Subcategory expectedSubcategory = getSubcategory(1, "Test Subcategory");
        UserEntity otherSeller = getExpectedUserEntity(UUID.randomUUID());
        Product expectedProduct = getProduct(1, "Test", expectedSubcategory);


        expectedProduct.setSeller(otherSeller);
        when(productRepositoryMock.findById(expectedUpdatableProductId))
                .thenReturn(Optional.of(expectedProduct));

        assertThrowsExactly(IncorrectSellerException.class,
                () -> productService.updateProduct(
                        expectedProductUpdateDTO,
                        new MultipartFile[1],
                        expectedUserId,
                        expectedUpdatableProductId
                )
        );
    }

    @Test
    void testUpdateProduct_WithNewImagesNullAndPreviousImagesAreEmpty_ShouldThrowMissingImageException(){
        UUID expectedUserId = UUID.randomUUID();
        long expectedUpdatableProductId = 1;

        UserEntity expectedUser = getExpectedUserEntity(
                expectedUserId,
                "Test",
                "User",
                Set.of(
                        Role.ROLE_SELLER
                )
        );
        ProductUpdateDTO expectedProductUpdateDTO = getProductUpdateDTO(expectedUser.getFullName());

        when(userRepositoryMock.findById(expectedUserId))
                .thenReturn(Optional.of(expectedUser));


        Subcategory expectedSubcategory = getSubcategory(1, "Test Subcategory");
        Product expectedProduct = getProduct(1, "Test", expectedSubcategory);
        expectedProduct.setSeller(expectedUser);
        expectedProduct.setImages(List.of());

        when(productRepositoryMock.findById(expectedUpdatableProductId))
                .thenReturn(Optional.of(expectedProduct));

        assertThrowsExactly(MissingImageException.class, () -> productService.updateProduct(
                expectedProductUpdateDTO,
                null,
                expectedUserId,
                expectedUpdatableProductId
        ));
    }

    @Test
    void testUpdateProduct_WithImageNameMissingFromUpdateDTO_ShouldRemoveImageAndShouldCallDeleteImagesByProductAndFileNameIn() throws IOException {
        UUID expectedUserId = UUID.randomUUID();
        long expectedUpdatableProductId = 1;

        UserEntity expectedUser = getExpectedUserEntity(
                expectedUserId,
                "Test",
                "User",
                Set.of(
                        Role.ROLE_SELLER
                )
        );

        Image imageForProductAndProductUpdateDTO = new Image();
        imageForProductAndProductUpdateDTO.setFileName("Test Image 1");
        imageForProductAndProductUpdateDTO.setImageFile(new byte[0]);

        ImageShow imageShowForExpectedProductUpdateDTO = new ImageShow(
                imageForProductAndProductUpdateDTO.getFileName(),
                imageForProductAndProductUpdateDTO.getImageFile()
        );

        ProductUpdateDTO expectedProductUpdateDTO = getProductUpdateDTO(
                expectedUser.getFullName(),
                List.of(imageShowForExpectedProductUpdateDTO),
                List.of()
        );

        when(userRepositoryMock.findById(expectedUserId))
                .thenReturn(Optional.of(expectedUser));


        Subcategory expectedSubcategory = getSubcategory(1, "Test Subcategory");

        Image expectedProductImage = new Image();
        expectedProductImage.setFileName("Expected product's image");

        Product expectedProduct = getProduct(1, "Test", expectedSubcategory);
        expectedProduct.setSeller(expectedUser);
        expectedProduct.setImages(List.of(
                imageForProductAndProductUpdateDTO,
                expectedProductImage
        ));

        when(productRepositoryMock.findById(expectedUpdatableProductId))
                .thenReturn(Optional.of(expectedProduct));

        productService.updateProduct(
                expectedProductUpdateDTO,
                new MultipartFile[0],
                expectedUserId,
                expectedUpdatableProductId
        );

        verify(imageRepositoryMock).deleteImagesByProductAndFileNameIn(expectedProduct, List.of(expectedProductImage.getFileName()));
    }

    @Test
    void testUpdateProduct_WithNewImage_ShouldAddNewImageToTheListAndCallSaveAllAndFindAll() throws IOException {
        UUID expectedUserId = UUID.randomUUID();
        long expectedUpdatableProductId = 1;

        UserEntity expectedUser = getExpectedUserEntity(
                expectedUserId,
                "Test",
                "User",
                Set.of(
                        Role.ROLE_SELLER
                )
        );

        Image imageForProductAndProductUpdateDTO = new Image();
        imageForProductAndProductUpdateDTO.setFileName("Test Image 1");
        imageForProductAndProductUpdateDTO.setImageFile(new byte[0]);


        ProductUpdateDTO expectedProductUpdateDTO = getProductUpdateDTO(expectedUser.getFullName());

        when(userRepositoryMock.findById(expectedUserId))
                .thenReturn(Optional.of(expectedUser));


        Subcategory expectedSubcategory = getSubcategory(1, "Test Subcategory");

        Image newImage = new Image();
        newImage.setFileName("This is a new Image");
        newImage.setImageFile(new byte[0]);

        Product expectedProduct = getProduct(1, "Test", expectedSubcategory);
        expectedProduct.setSeller(expectedUser);
        expectedProduct.setImages(List.of(
                imageForProductAndProductUpdateDTO
        ));

        when(productRepositoryMock.findById(expectedUpdatableProductId))
                .thenReturn(Optional.of(expectedProduct));

        MultipartFile[] newImageUploadMultipartFile = new MultipartFile[]{
                new MockMultipartFile(newImage.getFileName(), newImage.getImageFile())
        };

        List<Image> expectedConvertedImages = List.of(newImage);

        when(productUploadConverterMock.convertMultiPartFilesToListOfImages(
                newImageUploadMultipartFile,
                expectedProduct,
                imageUtilityMock)
        )
                .thenReturn(expectedConvertedImages);

        productService.updateProduct(
                expectedProductUpdateDTO,
                newImageUploadMultipartFile,
                expectedUserId,
                expectedUpdatableProductId
        );

        verify(productUploadConverterMock).convertMultiPartFilesToListOfImages(
                newImageUploadMultipartFile,
                expectedProduct,
                imageUtilityMock
        );

        verify(imageRepositoryMock).saveAll(expectedConvertedImages);
    }


    private Subcategory createBasicSubcategory(long subcategoryId, String subcategoryName){
        Subcategory subcategory = new Subcategory();
        subcategory.setId(subcategoryId);
        subcategory.setName(subcategoryName);

        return subcategory;
    }

    private ProductUploadDTO getProductUploadDTO(UUID expectedUserUUID, Map<String, String> productInformation) {
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
                productInformation,
                expectedUserUUID,
                new MultipartFile[10]
        );
    }

    private  ProductUpdateDTO getProductUpdateDTO(String sellerName){
        ProductInformation productInformation = new ProductInformation(
                "Test Product",
                10,
                7,
                100,
                4,
                1.5,
                5,
                "Test Brand",
                "This is a test",
                sellerName,
                1
        );
        return new ProductUpdateDTO(productInformation, List.of(), List.of());
    }

    private  ProductUpdateDTO getProductUpdateDTO(String sellerName, List<ImageShow> images, List<DetailValueDTO> detailValues){
        ProductInformation productInformation = new ProductInformation(
                "Test Product",
                10,
                7,
                100,
                4,
                1.5,
                5,
                "Test Brand",
                "This is a test",
                sellerName,
                1
        );
        return new ProductUpdateDTO(productInformation, images, detailValues);
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

    private Category getCategory(
            long expectedCategoryId,
            String categoryName,
            List<Subcategory> subcategories
    ) {
        Category expectedCategory = new Category();
        expectedCategory.setId(expectedCategoryId);
        expectedCategory.setName(categoryName);
        expectedCategory.setSubcategories(subcategories);
        return expectedCategory;
    }

    private Subcategory getSubcategory(long expectedSubcategoryId, String subcategoryName) {
        Subcategory expectedSubcategory = new Subcategory();
        expectedSubcategory.setId(expectedSubcategoryId);
        expectedSubcategory.setName(subcategoryName);
        return expectedSubcategory;
    }

    private Detail getDetail(long detailId, String detailName) {
        Detail expectedDetail = new Detail();
        expectedDetail.setId(detailId);
        expectedDetail.setName(detailName);
        return expectedDetail;
    }

    private SubcategoryDetail getSubcategoryDetail(long subcategoryDetailId, Detail expectedDetail) {
        SubcategoryDetail expectedSubcategoryDetail1 = new SubcategoryDetail();
        expectedSubcategoryDetail1.setId(subcategoryDetailId);
        expectedSubcategoryDetail1.setDetail(expectedDetail);
        return expectedSubcategoryDetail1;
    }

    private Product getProduct(long expectedProductFromId, String productName, Subcategory expectedSubcategory) {
        Product expectedProduct = new Product();
        expectedProduct.setId(expectedProductFromId);
        expectedProduct.setProductName(productName);
        expectedProduct.setSubcategory(expectedSubcategory);
        return expectedProduct;
    }

    private UserEntity getExpectedUserEntity(UUID expectedUserUUID){
        UserEntity expectedUser = new UserEntity();
        expectedUser.setId(expectedUserUUID);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");
        expectedUser.setRoles(
                Set.of(Role.ROLE_CUSTOMER)
        );

        return expectedUser;
    }

    private UserEntity getExpectedUserEntity(
            UUID expectedUserUUID,
            String firstName,
            String lastName,
            Set<Role> roles
    ){
        UserEntity expectedUser = new UserEntity();
        expectedUser.setId(expectedUserUUID);
        expectedUser.setFirstName(firstName);
        expectedUser.setLastName(lastName);
        expectedUser.setRoles(roles);

        return expectedUser;
    }

    private Product productUploadDTOConverterToProduct(ProductUploadDTO productUploadDTO){
        Product product = new Product();
        product.setProductName(productUploadDTO.productDetail().productName());
        product.setQuantity(productUploadDTO.productDetail().quantity());
        product.setBrand(productUploadDTO.productDetail().brand());
        product.setDeliveryTimeInDay(productUploadDTO.productDetail().deliveryTimeInDay());
        product.setDescription(productUploadDTO.productDetail().description());
        product.setPrice(productUploadDTO.productDetail().price());
        product.setShippingPrice(productUploadDTO.productDetail().shippingPrice());

        return product;
    }

    private Value getValue(String valueName, Detail detail){
        Value value = new Value();
        value.setValue(valueName);
        value.setDetail(detail);

        return value;
    }

    private ProductValue getProductValue(Product product, Value value){
        ProductValue productValue = new ProductValue();
        productValue.setProduct(product);
        productValue.setValue(value);

        return productValue;
    }

}