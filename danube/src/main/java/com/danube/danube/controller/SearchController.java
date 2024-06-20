package com.danube.danube.controller;

import com.danube.danube.model.dto.product.PageProductDTO;
import com.danube.danube.model.dto.search.SubcategorySearchNameDTO;
import com.danube.danube.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final ProductService productService;

    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product")
    public List<SubcategorySearchNameDTO> getSearchSubCategoryNames(@RequestParam("searchedProductName") String searchedProductName){
        return productService.getSearchSubCategoryNames(searchedProductName);
    }

    @GetMapping("/product/item")
    public PageProductDTO getProductsBySubcategory(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "9") int itemPerPage, @RequestParam String subcategoryName) throws DataFormatException, IOException {
        return productService.getProductsBySubcategory(pageNumber, itemPerPage, subcategoryName);
    }
}
