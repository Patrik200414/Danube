package com.danube.danube.controller;

import com.danube.danube.model.dto.product.*;
import com.danube.danube.service.ProductService;
import com.danube.danube.utility.converter.uploadproduct.ProductUploadConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final ProductUploadConverter productUploadConverter;

    @Autowired
    public ProductController(ProductService productService, ProductUploadConverter productUploadConverter) {
        this.productService = productService;
        this.productUploadConverter = productUploadConverter;
    }

    @GetMapping()
    public Set<ProductShowSmallDTO> getProducts(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int itemPerPage) throws DataFormatException, IOException {
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

    @GetMapping("/similar/{productFromId}")
    public List<ProductShowSmallDTO> getSimilarProducts(@PathVariable long productFromId) throws DataFormatException, IOException {
        return productService.getSimilarProducts(productFromId);
    }

    @GetMapping("/item/{id}")
    public ProductItemDTO getProductItems(@PathVariable long id) throws DataFormatException, IOException {
        return productService.getProductItem(id);
    }

    @GetMapping("/update/item/{id}")
    public ProductUpdateDTO getUpdatableProduct(@PathVariable long id) throws DataFormatException, IOException {
        return productService.getUpdatableProductItem(id);
    }


    @GetMapping("/myProducts/{userId}")
    public List<Map<String, String>> getMyProducts(@PathVariable long userId){
        return productService.getMyProducts(userId);
    }

    @PostMapping()
    public HttpStatus saveProduct(
            @RequestParam("productDetail") String productDetail,
            @RequestParam("productInformation") String productInformation,
            @RequestParam("userId") long userId,
            @RequestParam("images") MultipartFile[] images
    ) throws IOException {
        ProductUploadDTO productUploadDTO = productUploadConverter.convertRequestParamToProductUploadDTO(
                productDetail,
                productInformation,
                userId,
                images
        );

        productService.saveProduct(productUploadDTO);
        return HttpStatus.CREATED;
    }


    @PutMapping("/update/{productId}")
    public HttpStatus updateProduct(
            @PathVariable long productId,
            @RequestParam("updatedValues") String updatedValue,
            @RequestParam(value = "newImages", required = false) MultipartFile[] newImages,
            @RequestParam("seller") long sellerId
    ) throws IOException {
        ProductUpdateDTO productUpdateDTO = productUploadConverter.convertUpdateDataToProductUpdateDTO(updatedValue);
        productService.updateProduct(productUpdateDTO, newImages, sellerId, productId);
        return HttpStatus.CREATED;
    }

}
