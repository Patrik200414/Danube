package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.product.*;
import com.danube.danube.custom_exception.user.InvalidUserCredentialsException;
import com.danube.danube.custom_exception.user.UserNotSellerException;
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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Service
public class ProductService {
    @org.springframework.beans.factory.annotation.Value("${PRODUCT_IMAGE_DIRECTORY_PATH}")
    public String BASE_IMAGE_PATH;
    public static final int SIMILAR_RECOMENDED_PRODUCTS_RESULT_COUNT = 15;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final DetailRepository detailRepository;
    private final UserRepository userRepository;
    private final ValueRepository valueRepository;
    private final ProductValueRepository productValueRepository;
    private final ImageRepository imageRepository;
    private final SubcategoryDetailRepository subcategoryDetailRepository;
    private final ProductViewConverter productViewConverter;
    private final ProductCategoriesAndDetailsConverter productCategoriesAndDetailsConverter;
    private final ProductUploadConverter productUploadConverter;
    private final ImageUtility imageUtility;
    private final ConverterHelper converterHelper;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, DetailRepository detailRepository, UserRepository userRepository, ValueRepository valueRepository, ProductValueRepository productValueRepository, ImageRepository imageRepository, SubcategoryDetailRepository subcategoryDetailRepository, ProductViewConverter productViewConverter, ProductCategoriesAndDetailsConverter productCategoriesAndDetailsConverter, ProductUploadConverter productUploadConverter, ImageUtility imageUtility, ConverterHelper converterHelper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.detailRepository = detailRepository;
        this.userRepository = userRepository;
        this.valueRepository = valueRepository;
        this.productValueRepository = productValueRepository;
        this.imageRepository = imageRepository;
        this.subcategoryDetailRepository = subcategoryDetailRepository;
        this.productViewConverter = productViewConverter;
        this.productCategoriesAndDetailsConverter = productCategoriesAndDetailsConverter;
        this.productUploadConverter = productUploadConverter;
        this.imageUtility = imageUtility;
        this.converterHelper = converterHelper;
    }

    @Transactional
    public Set<ProductShowSmallDTO> getProducts(int pageNumber, int itemPerPage) throws DataFormatException, IOException {
        PageRequest pageRequest = PageRequest.of(pageNumber, itemPerPage);
        Page<Product> pagedProducts = productRepository.findAll(pageRequest);
        return productViewConverter.convertProductToProductShowSmallDTORandomOrder(pagedProducts, imageUtility, converterHelper);
    }

    public long getProductCount(){
        return productRepository.count();
    }



    public List<CategoryAndSubCategoryDTO> getCategoriesAndSubCategories(){
        List<Category> categories = categoryRepository.findAll();
        return getCategoryAndSubCategories(categories);
    }




    public List<CategoryDTO> getCategories(){
        List<Category> categories = categoryRepository.findAll();
        return productCategoriesAndDetailsConverter.convertCategoryToCategoryDTO(categories);
    }

    public List<SubcategoriesDTO> getSubCategoriesByCategory(long categoryId){
        Optional<Category> searchedCategory = categoryRepository.findById(categoryId);

        if(searchedCategory.isEmpty()){
            throw new NonExistingProductCategoryException();
        }

        Category category = searchedCategory.get();
        List<Subcategory> subcategories = subcategoryRepository.findAllByCategory(category);
        return productCategoriesAndDetailsConverter.convertSubcategoriesToSubcategoryDTOs(subcategories);
    }

    public List<DetailDTO> getDetailsBySubcategory(long id){
        Optional<Subcategory> searchedSubcategory = subcategoryRepository.findById(id);
        if(searchedSubcategory.isEmpty()){
            throw new NonExistingSubcategoryException();
        }

        Subcategory subcategory = searchedSubcategory.get();
        List<Detail> detailsBySubCategory = subcategoryDetailRepository.findAllBySubcategory(subcategory).stream()
                .map(SubcategoryDetail::getDetail)
                .toList();
        return productCategoriesAndDetailsConverter.convertDetailsToDetailsDTO(detailsBySubCategory);
    }

    public List<ProductShowSmallDTO> getSimilarProducts(long productFromId) throws DataFormatException, IOException {
        Product product = productRepository.findById(productFromId)
                .orElseThrow(NonExistingProductException::new);

        Pageable pageable = PageRequest.of(0, SIMILAR_RECOMENDED_PRODUCTS_RESULT_COUNT);
        List<Product> similarProducts = productRepository.findBySubcategoryAndIdNotOrderBySoldDescRatingDesc(product.getSubcategory(), productFromId, pageable);
        return productViewConverter.convertProductsToProductShowSmallDTO(similarProducts, imageUtility, converterHelper);
    }

    @Transactional
    public void saveProduct(ProductUploadDTO productUploadDTO) throws IOException {

        UserEntity seller = sellerValidator(productUploadDTO.userId());
        Subcategory subcategory = subcategoryRepository.findById(productUploadDTO.productDetail().subcategoryId())
                        .orElseThrow(NonExistingSubcategoryException::new);

        Product product = productUploadConverter.convertProductDetailUploadDTOToProduct(
                productUploadDTO.productDetail(),
                seller,
                subcategory
        );

        List<Image> images = productUploadConverter.convertMultiPartFilesToListOfImages(productUploadDTO.images(), product, imageUtility);
        imageRepository.saveAll(images);

        product.setImages(images);
        productRepository.save(product);

        Map<String, String> productInformation = productUploadDTO.productInformation();
        saveProductValues(productInformation, product);
    }

    public List<Map<String, String>> getMyProducts(long userId){
        UserEntity seller = userRepository.findById(userId).orElseThrow(NonExistingUserException::new);
        if(!seller.getRoles().contains(Role.ROLE_SELLER)){
            throw new UserNotSellerException();
        }

        List<Product> products = productRepository.findBySeller(seller);
        return products.stream()
                .map(productViewConverter::convertProductToMyProductInformation)
                .toList();
    }

    private List<CategoryAndSubCategoryDTO> getCategoryAndSubCategories(List<Category> categories) {
        List<CategoryAndSubCategoryDTO> categoriesAndSubCategories = new ArrayList<>();
        for(Category category : categories){
            List<String> subcategories = category.getSubcategories().stream()
                    .map(Subcategory::getName)
                    .toList();
            CategoryAndSubCategoryDTO categoryAndSubCategoryDTO = new CategoryAndSubCategoryDTO(
                    category.getName(),
                    subcategories
            );

            categoriesAndSubCategories.add(categoryAndSubCategoryDTO);
        }
        return categoriesAndSubCategories;
    }

    public ProductItemDTO getProductItem(long id) throws DataFormatException, IOException {
        Product product = productRepository.findById(id).orElseThrow(NonExistingProductException::new);
        return productViewConverter.convertProductToProductItemDTO(product, imageUtility, converterHelper);
    }

    public ProductUpdateDTO getUpdatableProductItem(long id) throws DataFormatException, IOException {
        Product product = productRepository.findById(id).orElseThrow(NonExistingProductException::new);
        return productUploadConverter.convertProductToProductUpdateDTO(product, imageUtility);
    }

    @Transactional
    public void updateProduct(ProductUpdateDTO updatedProductDetails, MultipartFile[] newImages, long sellerId, long updatedProductId) throws IOException{
        UserEntity seller = sellerValidator(sellerId);
        Product updatedProduct = productRepository.findById(updatedProductId)
                .orElseThrow(NonExistingProductException::new);

        productSellerValidation(updatedProduct, seller);

        handleImageUpdate(updatedProductDetails, newImages, updatedProduct);
        List<Image> productImages = imageRepository.findAll();

        ProductInformation updatedProductInformation = updatedProductDetails.productInformation();
        updateProductInformation(updatedProduct, updatedProductInformation, productImages);

        List<Value> updatedValues = updateProductDetailValues(updatedProductDetails);
        valueRepository.saveAll(updatedValues);
    }

    private void productSellerValidation(Product product, UserEntity seller) {
        if(product.getSeller().getId() != seller.getId()){
            throw new IncorrectSellerException();
        }
    }

    private List<Value> updateProductDetailValues(ProductUpdateDTO updatedProductDetails) {
        Map<Long, DetailValueDTO> detailsByValueIds = updatedProductDetails.detailValues().stream()
                .collect(Collectors.toMap(DetailValueDTO::valueId,
                        value -> value,
                        (existing, replacement) -> existing));

        List<Long> valueIds = updatedProductDetails.detailValues().stream()
                .map(DetailValueDTO::valueId)
                .toList();

        List<Value> valuesById = valueRepository.findAllById(valueIds);

        return getUpdatedValues(valuesById, detailsByValueIds);
    }

    private static List<Value> getUpdatedValues(List<Value> valuesById, Map<Long, DetailValueDTO> detailsByValueIds) {
        List<Value> updatedValues = new ArrayList<>();
        for(Value oldValue : valuesById){
            DetailValueDTO newValue = detailsByValueIds.get(oldValue.getId());
            oldValue.setValue(newValue.value());

            updatedValues.add(oldValue);
        }
        return updatedValues;
    }

    private void updateProductInformation(Product updatedProduct, ProductInformation updatedProductInformation, List<Image> productImages) {
        updatedProduct.setProductName(updatedProductInformation.productName());
        updatedProduct.setPrice(updatedProductInformation.price());
        updatedProduct.setDeliveryTimeInDay(updatedProductInformation.deliveryTimeInDay());
        updatedProduct.setQuantity(updatedProductInformation.quantity());
        updatedProduct.setShippingPrice(updatedProductInformation.shippingPrice());
        updatedProduct.setBrand(updatedProductInformation.brand());
        updatedProduct.setDescription(updatedProductInformation.description());
        updatedProduct.setImages(productImages);
    }

    private void handleImageUpdate(ProductUpdateDTO updatedProductDetails, MultipartFile[] newImages, Product updatedProduct) throws IOException {
        if(newImages != null || !updatedProductDetails.images().isEmpty()){
            removeImage(updatedProductDetails, updatedProduct);
            addNewImage(newImages, updatedProduct);
        } else {
            throw new MissingImageException();
        }
    }

    private void addNewImage(MultipartFile[] newImages, Product updatedProduct) throws IOException {
        if(newImages != null){
            List<Image> newUploadedImages = productUploadConverter.convertMultiPartFilesToListOfImages(newImages, updatedProduct, imageUtility);
            imageRepository.saveAll(newUploadedImages);
        }
    }

    private void removeImage(ProductUpdateDTO updatedProductDetails, Product updatedProduct) {
        List<String> deletedImagesName = updatedProduct.getImages().stream()
                .map(Image::getFileName)
                .filter(fileName -> !updatedProductDetails.images().contains(fileName))
                .toList();

        if(!deletedImagesName.isEmpty()){
            imageRepository.deleteImagesByProductAndFileNameIn(updatedProduct, deletedImagesName);
        }
    }


    private void saveProductValues(Map<String, String> productInformation, Product product){
        List<Value> savedValues = new ArrayList<>();
        List<ProductValue> savedProductValue = new ArrayList<>();
        for(Map.Entry<String, String> entry : productInformation.entrySet()){
            Detail detail = detailRepository.findByName(entry.getKey()).orElseThrow(
                    NonExistingDetailException::new
            );

            Value value = new Value();
            value.setDetail(detail);
            value.setValue(entry.getValue());
            savedValues.add(value);

            ProductValue productValue = new ProductValue();
            productValue.setProduct(product);
            productValue.setValue(value);
            savedProductValue.add(productValue);

        }
        valueRepository.saveAll(savedValues);
        productValueRepository.saveAll(savedProductValue);
    }

    private UserEntity sellerValidator(long userId){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(NonExistingUserException::new);

        if(!user.getRoles().contains(Role.ROLE_SELLER)){
            throw new InvalidUserCredentialsException();
        }

        return user;
    }
}
