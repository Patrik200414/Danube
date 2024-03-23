package com.danube.danube.controller;

import com.danube.danube.controller.advice.Advice;

import com.danube.danube.model.dto.product.*;
/*import com.danube.danube.model.product.ProductDetail;
import com.danube.danube.model.product.product_category.Category;
import com.danube.danube.model.product.product_category.SubCategory;*/
import com.danube.danube.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> getProducts(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int itemPerPage){
        try{
            List<ProductShowSmallDTO> products = productService.getProducts(pageNumber, itemPerPage);
            return ResponseEntity.ok(products);
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getProductCount(){
        try{
            long productCount = productService.getProductCount();
            return ResponseEntity.ok(productCount);
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }



    @GetMapping("/category/subcategory")
    public ResponseEntity<?> getProductCategoriesAndSubCategories(){
        try{
            List<CategoryAndSubCategoryDTO> categories = productService.getCategoriesAndSubCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }

    @GetMapping("/category")
    public ResponseEntity<?> getProductCategories(){
        try{
            List<CategoryDTO> categories = productService.getCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }

    @GetMapping("/subcategory/{categoryId}")
    public ResponseEntity<?> getSubCategoriesByCategory(@PathVariable long categoryId){
        try{
            List<SubcategoriesDTO> subCategories = productService.getSubCategoriesByCategory(categoryId);
            return ResponseEntity.ok(subCategories);
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }


    @PostMapping()
    public ResponseEntity<?> saveProduct(@RequestBody ProductUploadDTO productUploadDTO){
        try{
            productService.saveProduct(productUploadDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }

}
