package com.danube.danube.service;

import com.danube.danube.model.dto.ProductShowSmallDTO;
import com.danube.danube.model.dto.product.ProductUploadDTO;
import com.danube.danube.model.product.ProductDetail;
import com.danube.danube.model.product.product_category.Category;
import com.danube.danube.model.product.product_category.SubCategory;
import com.danube.danube.model.product.product_information.ProductInformation;
import com.danube.danube.repository.product.ProductDetailRepository;
import com.danube.danube.repository.product.ProductInformationRepository;
import com.danube.danube.service.utility.converter.Converter;
import com.danube.danube.service.utility.product_information_creator.ProductInformationCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
//    public static final int DEFAULT_ITEM_AMOUNT_PAGE = 10;
    private final ProductDetailRepository productDetailRepository;
    private final ProductInformationRepository productInformationRepository;
    private final ProductInformationCreator productInformationCreator;
    private final Converter converter;

    @Autowired
    public ProductService(ProductDetailRepository productDetailRepository, ProductInformationRepository productInformationRepository, ProductInformationCreator productInformationCreator, Converter converter) {
        this.productDetailRepository = productDetailRepository;
        this.productInformationRepository = productInformationRepository;
        this.productInformationCreator = productInformationCreator;
        this.converter = converter;
    }


    public List<ProductShowSmallDTO> getProducts(int pageNumber, int itemPerPage){
        PageRequest pageRequest = PageRequest.of(pageNumber, itemPerPage);

        Page<ProductDetail> productPage = productDetailRepository.findAll(pageRequest);
        List<ProductDetail> products = productPage.getContent();

        return converter.convertProductDetails(products);
    }

    public long getProductCount(){
        return productDetailRepository.count();
    }

    public Map<Category, List<SubCategory>> getCategoriesAndSubCategories(){
        Map<Category, List<SubCategory>> categories = new LinkedHashMap<>();

        Arrays.stream(Category.values())
                .forEach(category -> categories.put(category, category.subCategories));

        return categories;
    }

    public Category[] getCategories(){
        return Category.values();
    }

    public void saveProduct(ProductUploadDTO productUploadDTO){

        ProductInformation productInformation = productInformationCreator.createProduct(productUploadDTO.productInformation());
        ProductDetail productDetail = converter.convertToProductDetail(productUploadDTO.productDetail());

        productInformationRepository.save(productInformation);
        productDetail.setProductInformation(productInformation);
        productInformation.setProductDetail(productDetail);


        productDetailRepository.save(productDetail);
        productInformationRepository.save(productInformation);
    }
}
