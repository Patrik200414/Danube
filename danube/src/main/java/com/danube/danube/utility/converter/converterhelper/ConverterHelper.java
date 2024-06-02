package com.danube.danube.utility.converter.converterhelper;

import com.danube.danube.model.dto.product.ProductInformation;
import com.danube.danube.model.product.Product;

import java.util.List;

public interface ConverterHelper {
    public ProductInformation createProductInformation(Product product);
    public List<String> getProductImages(Product product);
}
