package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.product.NonExistingDetailException;
import com.danube.danube.custom_exception.product.NonExistingProductCategoryException;
import com.danube.danube.custom_exception.product.NonExistingProductException;
import com.danube.danube.custom_exception.product.NonExistingSubcategoryException;
import com.danube.danube.custom_exception.user.InvalidUserCredentialsException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ProductService {
    public static final String BASE_IMAGE_PATH = String.format("%s\\src\\main\\resources\\static\\images\\", System.getProperty("user.dir"));
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

    public void saveProduct(ProductUploadDTO productUploadDTO) throws IOException {
        UserEntity seller = userValidator(productUploadDTO.userId());

        fileLogger.saveFile(productUploadDTO.images(), BASE_IMAGE_PATH);
        Product product = converter.convertProductDetailUploadDTOToProduct(
                productUploadDTO.productDetail(), seller
        );

        List<Image> images = converter.convertMultiPartFilesToListOfImages(productUploadDTO.images(), product);
        imageRepository.saveAll(images);

        product.setImages(images);
        productRepository.save(product);

        Map<String, String> productInformation = productUploadDTO.productInformation();
        saveProductValues(productInformation, product);
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

    private UserEntity userValidator(long userId){
        Optional<UserEntity> searchedSeller = userRepository.findById(userId);

        if(searchedSeller.isEmpty()){
            throw new NonExistingUserException();
        }

        UserEntity user = searchedSeller.get();
        if(!user.getRoles().contains(Role.ROLE_SELLER)){
            throw new InvalidUserCredentialsException();
        }

        return user;
    }
}
