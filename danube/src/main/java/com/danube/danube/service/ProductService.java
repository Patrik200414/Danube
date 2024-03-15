package com.danube.danube.service;

import com.danube.danube.model.product.ProductDetail;
import com.danube.danube.model.product.product_category.Category;
import com.danube.danube.model.product.product_category.SubCategory;
import com.danube.danube.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<ProductDetail> getProducts(){
        return productRepository.findAll();
    }

    public Map<Category, List<SubCategory>> getCategories(){
        Map<Category, List<SubCategory>> categories = new LinkedHashMap<>();

        Arrays.stream(Category.values())
                .forEach(category -> categories.put(category, category.subCategories));

        return categories;
    }
}
