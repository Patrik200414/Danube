package com.danube.danube.service;

import com.danube.danube.model.dto.ProductUploadDTO;
import com.danube.danube.model.product.ProductDetail;
import com.danube.danube.model.product.product_category.Category;
import com.danube.danube.model.product.product_category.SubCategory;
import com.danube.danube.model.product.product_information.ProductInformation;
import com.danube.danube.repository.product.ProductDetailRepository;
import com.danube.danube.repository.product.ProductInformationRepository;
import com.danube.danube.service.utility.Converter;
import com.danube.danube.service.utility.ProductInformationCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductInformationRepository productInformationRepository;

    @Autowired
    public ProductService(ProductDetailRepository productDetailRepository, ProductInformationRepository productInformationRepository) {
        this.productDetailRepository = productDetailRepository;
        this.productInformationRepository = productInformationRepository;
    }


    public List<ProductDetail> getProducts(){
        return productDetailRepository.findAll();
    }

    public Map<Category, List<SubCategory>> getCategories(){
        Map<Category, List<SubCategory>> categories = new LinkedHashMap<>();

        Arrays.stream(Category.values())
                .forEach(category -> categories.put(category, category.subCategories));

        return categories;
    }

    public void saveProduct(ProductUploadDTO productUploadDTO){

        ProductInformation productInformation = ProductInformationCreator.createProduct(productUploadDTO.productInformation());
        ProductDetail productDetail = Converter.convertToProductDetail(productUploadDTO.productDetail());

        productInformationRepository.save(productInformation);
        productDetail.setProductInformation(productInformation);
        productInformation.setProductDetail(productDetail);


        productDetailRepository.save(productDetail);
        productInformationRepository.save(productInformation);
    }
}
