package com.danube.danube.controller;

import com.danube.danube.controller.advice.Advice;
import com.danube.danube.model.product.ProductDetail;
import com.danube.danube.service.ProductService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> getProducts(){
        try{
            List<ProductDetail> products = productService.getProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e){
            return controllerAdvice.handleException(e);
        }
    }
}
