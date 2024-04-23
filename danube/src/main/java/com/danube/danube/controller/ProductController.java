    package com.danube.danube.controller;

import com.danube.danube.model.dto.product.*;
import com.danube.danube.service.ProductService;
import com.danube.danube.utility.converter.Converter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

    @RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final Converter converter;

    @Autowired
    public ProductController(ProductService productService, Converter converter) {
        this.productService = productService;
        this.converter = converter;
    }

    @GetMapping()
    public Set<ProductShowSmallDTO> getProducts(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int itemPerPage){
        return productService.getProducts(pageNumber, itemPerPage);
    }

    @GetMapping("/count")
    public long getProductCount(){
        return productService.getProductCount();
    }



    @GetMapping("/category/subcategory")
    public List<CategoryAndSubCategoryDTO> getProductCategoriesAndSubCategories(){
        return productService.getCategoriesAndSubCategories();
    }

    @GetMapping("/category")
    public List<CategoryDTO> getProductCategories(){
        return productService.getCategories();
    }

    @GetMapping("/subcategory/{categoryId}")
    public List<SubcategoriesDTO> getSubCategoriesByCategory(@PathVariable long categoryId){
        return productService.getSubCategoriesByCategory(categoryId);
    }

    @GetMapping("/detail/{subcategoryId}")
    public List<DetailDTO> getDetailsBySubcategory(@PathVariable long subcategoryId){
        return productService.getDetailsBySubcategory(subcategoryId);
    }

    @GetMapping("/item/{id}")
    public ProductItemDTO getProductItems(@PathVariable long id){
        return productService.getProductItem(id);
    }
    @Async
    @Transactional
    @PostMapping()
    public HttpStatus saveProduct(
            @RequestParam("productDetail") String productDetail,
            @RequestParam("productInformation") String productInformation,
            @RequestParam("userId") long userId,
            @RequestParam("image") MultipartFile[] images
    ) throws IOException {
        ProductUploadDTO productUploadDTO = converter.convertRequestParamToProductUploadDTO(
                productDetail,
                productInformation,
                userId,
                images
        );

        productService.saveProduct(productUploadDTO);
        return HttpStatus.CREATED;
    }

}
