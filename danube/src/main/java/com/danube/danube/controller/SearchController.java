package com.danube.danube.controller;

import com.danube.danube.model.dto.search.ProductSearchNameDTO;
import com.danube.danube.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final ProductService productService;

    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product")
    public List<ProductSearchNameDTO> getSearchRecomendationForProducts(@RequestParam("searchedProductName") String searchedProductName){
        return productService.getProductsByNameLike(searchedProductName);
    }
}
