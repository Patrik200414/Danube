package com.danube.danube.utility.converter.uploadproduct;

import com.danube.danube.model.dto.product.DetailValueDTO;
import com.danube.danube.model.dto.product.ProductDetailUploadDTO;
import com.danube.danube.model.dto.product.ProductUpdateDTO;
import com.danube.danube.model.dto.product.ProductUploadDTO;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.image.Image;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.utility.converter.converterhelper.ConverterHelper;
import com.danube.danube.utility.converter.converterhelper.ConverterHelperImpl;
import com.danube.danube.utility.imageutility.ImageUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

@Component
public class ProductUploadConverterImpl implements ProductUploadConverter{

    private final ObjectMapper objectMapper;
    private final ConverterHelper converterHelper;

    public ProductUploadConverterImpl() {
        this.objectMapper = new ObjectMapper();
        this.converterHelper = new ConverterHelperImpl();
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
    public List<Image> convertMultiPartFilesToListOfImages(MultipartFile[] images, Product product, ImageUtility imageUtility) throws IOException {

        List<Image> convertedImages = new ArrayList<>();

        for(MultipartFile image : images){
            Image createdImage = new Image();
            createdImage.setProduct(product);
            createdImage.setFileName(image.getOriginalFilename());
            createdImage.setImageFile(imageUtility.compressImage(image.getBytes()));
            convertedImages.add(createdImage);
        }

        return convertedImages;
    }

    @Override
    public ProductUpdateDTO convertProductToProductUpdateDTO(Product product, ImageUtility imageUtility) throws DataFormatException, IOException {
        List<DetailValueDTO> detailValues = product.getProductValues().stream()
                .map(productValue -> new DetailValueDTO(
                        productValue.getDetailName(),
                        productValue.getValueName(),
                        productValue.getDetailId(),
                        productValue.getValueId()
                )).toList();

        return new ProductUpdateDTO(
                converterHelper.createProductInformation(product),
                converterHelper.getProductImages(product, imageUtility),
                detailValues
        );
    }

    @Override
    public ProductUpdateDTO convertUpdateDataToProductUpdateDTO(String updatedValue) throws JsonProcessingException {
        return objectMapper.readValue(updatedValue, ProductUpdateDTO.class);
    }
}
