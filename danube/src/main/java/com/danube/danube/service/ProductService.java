package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.product.NonExistingDetailException;
import com.danube.danube.custom_exception.product.NonExistingProductCategoryException;
import com.danube.danube.custom_exception.product.NonExistingSubcategoryException;
import com.danube.danube.custom_exception.user.InvalidUserCredentialsException;
import com.danube.danube.model.dto.product.*;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.connection.ProductValue;
import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.model.product.value.Value;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.product.*;
import com.danube.danube.repository.product.connection.ProductValueRepository;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.utility.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    public static final int DEFAULT_ITEM_AMOUNT_PAGE = 10;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final DetailRepository detailRepository;
    private final UserRepository userRepository;
    private final ValueRepository valueRepository;
    private final ProductValueRepository productValueRepository;
    private final Converter converter;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, DetailRepository detailRepository, UserRepository userRepository, ValueRepository valueRepository, ProductValueRepository productValueRepository, Converter converter) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.detailRepository = detailRepository;
        this.userRepository = userRepository;
        this.valueRepository = valueRepository;
        this.productValueRepository = productValueRepository;
        this.converter = converter;
    }


    public List<ProductShowSmallDTO> getProducts(int pageNumber, int itemPerPage){
        PageRequest pageRequest = PageRequest.of(pageNumber, itemPerPage);
        List<Product> products = productRepository.findAll();
        return converter.convertProductsToProductShowSmallDTOs(products);
    }

    public long getProductCount(){
        return productRepository.count();
    }



    public List<CategoryAndSubCategoryDTO> getCategoriesAndSubCategories(){
        List<Category> categories = categoryRepository.findAll();

        List<CategoryAndSubCategoryDTO> categoriesAndSubCategories = new ArrayList<>();
        for(Category category : categories){
            List<String> subcategories = new ArrayList<>();
            for(Subcategory subcategory : category.getSubcategories()){
                subcategories.add(subcategory.getName());
            }
            CategoryAndSubCategoryDTO categoryAndSubCategoryDTO = new CategoryAndSubCategoryDTO(
                    category.getName(),
                    subcategories
            );

            categoriesAndSubCategories.add(categoryAndSubCategoryDTO);
        }

        return categoriesAndSubCategories;
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
        List<Detail> details = detailRepository.findBySubcategory(subcategory);
        return converter.convertDetailsToDetailsDTO(details);
    }

    public void saveProduct(ProductUploadDTO productUploadDTO){
        UserEntity seller = userValidator(productUploadDTO.userId());
        Product product = converter.convertProductDetailUploadDTOToProduct(
                productUploadDTO.productDetail(), seller
        );

        productRepository.save(product);
        Map<String, String> productInformation = productUploadDTO.productInformation();
        saveProductValues(productInformation, product);
    }

    private void saveProductValues(Map<String, String> productInformation, Product product){
        for(Map.Entry<String, String> entry : productInformation.entrySet()){
            Detail detail = detailRepository.findByName(entry.getKey()).orElseThrow(
                    () -> new NonExistingDetailException()
            );

            Value value = new Value();
            value.setDetail(detail);
            value.setValue(entry.getValue());

            Value savedValue = valueRepository.save(value);
            ProductValue productValue = new ProductValue();
            productValue.setProduct(product);
            productValue.setValue(savedValue);
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
