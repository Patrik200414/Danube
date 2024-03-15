package com.danube.danube.controller;

import com.danube.danube.controller.advice.Advice;

import com.danube.danube.model.product.ProductDetail;
import com.danube.danube.model.product.product_category.Category;
import com.danube.danube.model.product.product_category.SubCategory;
import com.danube.danube.service.ProductService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final Advice controllerAdvice;
    private final ProductService productService;

    @Autowired
    public ProductController(Advice controllerAdvice, ProductService productService) {
        this.controllerAdvice = controllerAdvice;
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<?> getProducts(){
        try{
            List<ProductDetail> products = productService.getProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }


    @GetMapping("/category")
    public ResponseEntity<?> getProductCategories(){
        try{
            Map<Category, List<SubCategory>> categories = productService.getCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }
    /*
    @PostMapping()
    public ResponseEntity<?> saveProduct(@RequestBody UploadProductDTO product){
        try{

            Map<Category, List<String>> products = new HashMap<>();
            Arrays.stream(Category.values()).forEach(category -> products.put(category, category.subCategories));
            return ResponseEntity.ok(new ProductCategoryDTO(products));
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }*/
}
