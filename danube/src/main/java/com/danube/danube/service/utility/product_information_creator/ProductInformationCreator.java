package com.danube.danube.service.utility.product_information_creator;

import com.danube.danube.model.product.product_information.ProductInformation;
import org.springframework.stereotype.Component;

import java.util.Map;


public interface ProductInformationCreator {
    ProductInformation createProduct(Map<String, String> product);
}
