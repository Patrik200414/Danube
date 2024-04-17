    package com.danube.danube.controller;

import com.danube.danube.model.dto.product.*;
/*import com.danube.danube.model.product.ProductDetail;
import com.danube.danube.model.product.product_category.Category;
import com.danube.danube.model.product.product_category.SubCategory;*/
import com.danube.danube.service.ProductService;
import com.danube.danube.utility.Converter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getProducts(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int itemPerPage){
        Set<ProductShowSmallDTO> products = productService.getProducts(pageNumber, itemPerPage);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/count")
    public ResponseEntity<?> getProductCount(){
        long productCount = productService.getProductCount();
        return ResponseEntity.ok(productCount);
    }



    @GetMapping("/category/subcategory")
    public ResponseEntity<?> getProductCategoriesAndSubCategories(){
        List<CategoryAndSubCategoryDTO> categories = productService.getCategoriesAndSubCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/category")
    public ResponseEntity<?> getProductCategories(){
        List<CategoryDTO> categories = productService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/subcategory/{categoryId}")
    public ResponseEntity<?> getSubCategoriesByCategory(@PathVariable long categoryId){
        List<SubcategoriesDTO> subCategories = productService.getSubCategoriesByCategory(categoryId);
        return ResponseEntity.ok(subCategories);
    }

    @GetMapping("/detail/{subcategoryId}")
    public ResponseEntity<?> getDetailsBySubcategory(@PathVariable long subcategoryId){
        List<DetailDTO> details = productService.getDetailsBySubcategory(subcategoryId);
        return ResponseEntity.ok(details);
    }


    @Async
    @Transactional
    @PostMapping()
    public ResponseEntity<?> saveProduct(
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
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
