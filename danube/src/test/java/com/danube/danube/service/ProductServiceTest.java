package com.danube.danube.service;

import com.danube.danube.custom_exception.product.NonExistingProductCategoryException;
import com.danube.danube.custom_exception.product.NonExistingProductException;
import com.danube.danube.custom_exception.product.NonExistingSubcategoryException;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.connection.SubcategoryDetail;
import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.repository.product.*;
import com.danube.danube.repository.product.connection.ProductValueRepository;
import com.danube.danube.repository.product.connection.SubcategoryDetailRepository;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.utility.converter.categoriesanddetails.ProductCategoriesAndDetailsConverter;
import com.danube.danube.utility.converter.converterhelper.ConverterHelper;
import com.danube.danube.utility.converter.productview.ProductViewConverter;
import com.danube.danube.utility.converter.uploadproduct.ProductUploadConverter;
import com.danube.danube.utility.imageutility.ImageUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
                productViewConverterMock,
                productCategoriesAndDetailsConverterMock,
                productUploadConverterMock,
                imageUtilityMock,
                converterHelperMock
        );
    }

    /*@Test
    void getProducts() {

    }*/

    /*@Test
    void getProductCount() {

    }*/

    /*@Test
    void getCategoriesAndSubCategories() {

    }*/

    /*@Test
    void getCategories() {
        productService.getCategories()
    }*/

    @Test
    void testGetSubCategoriesByCategory_WithNonExistingProduct_ThrowNonExistingProductCategoryException() {
        long expectedCategoryId = 1;
        when(categoryRepositoryMock.findById(expectedCategoryId))
                .thenReturn(Optional.empty());


        assertThrowsExactly(NonExistingProductCategoryException.class, () -> productService.getSubCategoriesByCategory(expectedCategoryId));
    }

    @Test
    void testGetSubCategoriesByCategory_(){
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

    /*@Test
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