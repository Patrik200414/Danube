package com.danube.danube.controller;

import com.danube.danube.model.dto.product.*;
import com.danube.danube.service.ProductService;
import com.danube.danube.utility.converter.uploadproduct.ProductUploadConverter;
import com.danube.danube.utility.json.JsonUtility;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final ProductUploadConverter productUploadConverter;
    private final JsonUtility jsonUtility;

    @Autowired
    public ProductController(ProductService productService, ProductUploadConverter productUploadConverter, JsonUtility jsonUtility) {
        this.productService = productService;
        this.productUploadConverter = productUploadConverter;
        this.jsonUtility = jsonUtility;
    }

    @GetMapping()
    public PageProductDTO getProducts(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "9") int itemPerPage) throws DataFormatException, IOException {
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
    public List<MyProductInformationDTO> getMyProducts(@PathVariable UUID userId) throws DataFormatException, IOException {
        return productService.getMyProducts(userId);
    }

    @PostMapping()
    public HttpStatus saveProduct(
            @RequestParam("productDetail") String productDetail,
            @RequestParam("productInformation") String productInformation,
            @RequestParam("userId") UUID userId,
            @RequestParam("images") MultipartFile[] images
    ) throws IOException {
        ProductDetailUploadDTO productDetailUploadDTO = jsonUtility.validateJson(productDetail, ProductDetailUploadDTO.class);
        Map<String, String> convertedProductInformation = jsonUtility.validateJson(productInformation, new TypeReference<Map<String, String>>() {});

        ProductUploadDTO productUploadDTO = new ProductUploadDTO(
                productDetailUploadDTO,
                convertedProductInformation,
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
            @RequestParam("seller") UUID sellerId
    ) throws IOException {
        //jsonUtility.validateJson(updatedValue, ProductUpdateDTO.class.getName());
        ProductUpdateDTO productUpdateDTO = jsonUtility.validateJson(updatedValue, ProductUpdateDTO.class);
        productService.updateProduct(productUpdateDTO, newImages, sellerId, productId);
        return HttpStatus.CREATED;
    }

}
