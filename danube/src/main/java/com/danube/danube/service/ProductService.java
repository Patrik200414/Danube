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
import com.danube.danube.utility.converter.Converter;
import com.danube.danube.utility.filellogger.FileLogger;
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

@Service
public class ProductService {
    public static final String BASE_IMAGE_PATH = String.format("%s\\src\\main\\resources\\static\\images\\", System.getProperty("user.dir"));
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
    private final Converter converter;
    private final FileLogger fileLogger;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, DetailRepository detailRepository, UserRepository userRepository, ValueRepository valueRepository, ProductValueRepository productValueRepository, ImageRepository imageRepository, SubcategoryDetailRepository subcategoryDetailRepository, Converter converter, FileLogger fileLogger) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.detailRepository = detailRepository;
        this.userRepository = userRepository;
        this.valueRepository = valueRepository;
        this.productValueRepository = productValueRepository;
        this.imageRepository = imageRepository;
        this.subcategoryDetailRepository = subcategoryDetailRepository;
        this.converter = converter;
        this.fileLogger = fileLogger;
    }


    public Set<ProductShowSmallDTO> getProducts(int pageNumber, int itemPerPage){
        PageRequest pageRequest = PageRequest.of(pageNumber, itemPerPage);
        Page<Product> pagedProducts = productRepository.findAll(pageRequest);
        return converter.convertProductToProductShowSmallDTORandomOrder(pagedProducts);
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

    public List<DetailDTO> getDetailsBySubcategory(long id){
        Optional<Subcategory> searchedSubcategory = subcategoryRepository.findById(id);
        if(searchedSubcategory.isEmpty()){
            throw new NonExistingSubcategoryException();
        }

        Subcategory subcategory = searchedSubcategory.get();
        List<Detail> detailsBySubCategory = subcategoryDetailRepository.findAllBySubcategory(subcategory).stream()
                .map(SubcategoryDetail::getDetail)
                .toList();
        return converter.convertDetailsToDetailsDTO(detailsBySubCategory);
    }

    public List<ProductShowSmallDTO> getSimilarProducts(long productFromId){
        Product product = productRepository.findById(productFromId)
                .orElseThrow(NonExistingProductException::new);

        Pageable pageable = PageRequest.of(0, SIMILAR_RECOMENDED_PRODUCTS_RESULT_COUNT);
        List<Product> similarProducts = productRepository.findBySubcategoryAndIdNotOrderBySoldDescRatingDesc(product.getSubcategory(), productFromId, pageable);
        return converter.convertProductsToProductShowSmallDTO(similarProducts);
    }

    @Transactional
    public void saveProduct(ProductUploadDTO productUploadDTO) throws IOException {
        UserEntity seller = sellerValidator(productUploadDTO.userId());
        Subcategory subcategory = subcategoryRepository.findById(productUploadDTO.productDetail().subcategoryId())
                        .orElseThrow(NonExistingSubcategoryException::new);

        fileLogger.saveFile(productUploadDTO.images(), BASE_IMAGE_PATH);
        Product product = converter.convertProductDetailUploadDTOToProduct(
                productUploadDTO.productDetail(),
                seller,
                subcategory
        );

        List<Image> images = converter.convertMultiPartFilesToListOfImages(productUploadDTO.images(), product);
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
                .map(converter::convertProductToMyProductInformation)
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

    public ProductItemDTO getProductItem(long id){
        Product product = productRepository.findById(id).orElseThrow(NonExistingProductException::new);
        return converter.convertProductToProductItemDTO(product);
    }

    public ProductUpdateDTO getUpdatableProductItem(long id){
        Product product = productRepository.findById(id).orElseThrow(NonExistingProductException::new);
        return converter.convertProductToProductUpdateDTO(product);
    }

    @Transactional
    public void updateProduct(ProductUpdateDTO updatedProductDetails, MultipartFile[] newImages, long sellerId, long updatedProductId) throws IOException {
        sellerValidator(sellerId);
        Product updatedProduct = productRepository.findById(updatedProductId)
                .orElseThrow(NonExistingProductException::new);

        handleImageUpdate(updatedProductDetails, newImages, updatedProduct);
        List<Image> productImages = imageRepository.findAll();

        ProductInformation updatedProductInformation = updatedProductDetails.productInformation();
        updateProductInformation(updatedProduct, updatedProductInformation, productImages);

        List<Value> updatedValues = updateProductDetailValues(updatedProductDetails);
        valueRepository.saveAll(updatedValues);
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
            fileLogger.saveFile(newImages, BASE_IMAGE_PATH);
            List<Image> newUploadedImages = converter.convertMultiPartFilesToListOfImages(newImages, updatedProduct);
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
        for(Map.Entry<String, String> entry : productInformation.entrySet()){
            Detail detail = detailRepository.findByName(entry.getKey()).orElseThrow(
                    NonExistingDetailException::new
            );

            Value value = new Value();
            value.setDetail(detail);
            value.setValue(entry.getValue());

            Value savedValue = valueRepository.save(value);
            ProductValue productValue = new ProductValue();
            productValue.setProduct(product);
            productValue.setValue(savedValue);
            productValueRepository.save(productValue);
        }
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
