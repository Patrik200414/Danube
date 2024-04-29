package com.danube.danube.utility.converter;

import com.danube.danube.model.dto.product.*;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.image.Image;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.internal.util.collections.LinkedIdentityHashMap;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

public class ConverterImpl implements Converter {
    private final ObjectMapper objectMapper;

    public ConverterImpl() {
        this.objectMapper = new ObjectMapper();
    }

    public UserEntity convertUserRegistrationDTOToUserEntity(UserRegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder){
        UserEntity user = new UserEntity();
        user.setFirstName(userRegistrationDTO.firstName());
        user.setLastName(userRegistrationDTO.lastName());
        user.setEmail(userRegistrationDTO.email());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.password()));
        user.setRoles(Set.of(Role.ROLE_CUSTOMER));

        return user;
    }


    public List<CategoryDTO> convertCategoryToCategoryDTO(List<Category> categories){
        return categories.stream()
                .map(category -> new CategoryDTO(
                        category.getName(),
                        category.getId()
                )).toList();
    }

    public List<SubcategoriesDTO> convertSubcategoriesToSubcategoryDTOs(List<Subcategory> subcategories){
        return subcategories.stream()
                .map(subcategory -> new SubcategoriesDTO(
                        subcategory.getName(),
                        subcategory.getId()
                ))
                .toList();
    }

    @Override
    public List<DetailDTO> convertDetailsToDetailsDTO(List<Detail> details) {
        return details.stream()
                .map(detail -> new DetailDTO(
                        detail.getName(),
                        detail.getId(),
                        ""
                )).toList();
    }

    @Override
    public Product convertProductDetailUploadDTOToProduct(ProductDetailUploadDTO productDetails, UserEntity seller, Subcategory subcategory) {
        Product product = new Product();
        product.setProductName(productDetails.productName());
        product.setBrand(productDetails.brand());
        product.setDescription(productDetails.description());
        product.setPrice(productDetails.price());
        product.setQuantity(productDetails.quantity());
        product.setDeliveryTimeInDay(productDetails.deliveryTimeInDay());
        product.setRating(0);
        product.setShippingPrice(productDetails.shippingPrice());
        product.setSold(0);
        product.setSeller(seller);
        product.setSubcategory(subcategory);
        return product;
    }

    @Override
    public ProductUploadDTO convertRequestParamToProductUploadDTO(String productDetail, String productInformation, long userId, MultipartFile[] image) throws JsonProcessingException {
        ProductDetailUploadDTO convertedProductDetail = objectMapper.readValue(productDetail, ProductDetailUploadDTO.class);
        Map<String, String> convertedProductInformation = objectMapper.readValue(productInformation, new TypeReference<Map<String, String>>() {});

        return new ProductUploadDTO(
                convertedProductDetail,
                convertedProductInformation,
                userId,
                image
        );

    }

    @Override
    public List<Image> convertMultiPartFilesToListOfImages(MultipartFile[] images, Product product) {

        List<Image> convertedImages = new ArrayList<>();

        for(MultipartFile image : images){
            Image createdImage = new Image();
            createdImage.setProduct(product);
            createdImage.setFileName(image.getOriginalFilename());
            convertedImages.add(createdImage);
        }

        return convertedImages;
    }

    @Override
    public Set<ProductShowSmallDTO> convertProductToProductShowSmallDTORandomOrder(Page<Product> products){
        return products.stream()
                .map(product -> new ProductShowSmallDTO(
                        product.getProductName(),
                        product.getPrice(),
                        product.getShippingPrice(),
                        product.getDeliveryTimeInDay(),
                        product.getQuantity(),
                        product.getRating(),
                        product.getSold(),
                        product.getId(),
                        getProductImageName(product),
                        product.getSellerFullName()
                )).collect(Collectors.toSet());
    }

    private List<String> getProductImageName(Product product){
        return product.getImages().stream()
                .map(Image::getFileName)
                .toList();
    }

    @Override
    public ProductItemDTO convertProductToProductItemDTO(Product product) {
        List<DetailDTO> detailValues = product.getProductValues().stream()
                .map(productValue -> new DetailDTO(
                        productValue.getDetailName(),
                        productValue.getDetailId(),
                        productValue.getValueName()
                )).toList();


        return new ProductItemDTO(
                new ProductInformation(
                        product.getProductName(),
                        product.getPrice(),
                        product.getDeliveryTimeInDay(),
                        product.getQuantity(),
                        product.getRating(),
                        product.getShippingPrice(),
                        product.getSold(),
                        product.getBrand(),
                        product.getDescription(),
                        product.getSeller().getFullName()
                ),
                product.getImages().stream()
                        .map(Image::getFileName)
                        .toList(),
                detailValues
        );
    }

    @Override
    public List<ProductShowSmallDTO> convertProductsToProductShowSmallDTO(Collection<Product> products) {
        return products.stream()
                .map(product -> new ProductShowSmallDTO(
                        product.getProductName(),
                        product.getPrice(),
                        product.getShippingPrice(),
                        product.getDeliveryTimeInDay(),
                        product.getQuantity(),
                        product.getRating(),
                        product.getSold(),
                        product.getId(),
                        product.getImages().stream().map(Image::getFileName).toList(),
                        product.getSellerFullName()
                )).toList();
    }

    @Override
    public Map<String, String> convertProductToMyProductInformation(Product product) {
        Map<String, String> myProductInformation = new LinkedIdentityHashMap<>();
        myProductInformation.put("Product image", product.getFirstProductImage().getFileName());
        myProductInformation.put("Product name", product.getProductName());
        myProductInformation.put("Owner", product.getSellerFullName());
        myProductInformation.put("id", String.valueOf(product.getId()));
        return myProductInformation;
    }
}
