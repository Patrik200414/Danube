package com.danube.danube.utility.converter.converterhelper;

import com.danube.danube.model.dto.product.ProductInformation;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.image.Image;
import org.springframework.stereotype.Component;

import java.util.List;

public class ConverterHelperImpl implements ConverterHelper{
    @Override
    public ProductInformation createProductInformation(Product product) {
        return new ProductInformation(
                product.getProductName(),
                product.getPrice(),
                product.getDeliveryTimeInDay(),
                product.getQuantity(),
                product.getRating(),
                product.getShippingPrice(),
                product.getSold(),
                product.getBrand(),
                product.getDescription(),
                product.getSeller().getFullName(),
                product.getSubcategory().getId()
        );
    }

    @Override
    public List<String> getProductImages(Product product) {
        return product.getImages().stream()
                .map(Image::getFileName)
                .toList();
    }
}
