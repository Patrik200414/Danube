package com.danube.danube.service.utility;

import com.danube.danube.custom_exception.IncorrectProductObjectFormException;
import com.danube.danube.model.product.product_category.Category;
import com.danube.danube.model.product.product_category.SubCategory;
import com.danube.danube.model.product.product_enums.shirt.Gender;
import com.danube.danube.model.product.product_enums.shirt.NeckLine;
import com.danube.danube.model.product.product_enums.shirt.ShirtSize;
import com.danube.danube.model.product.product_enums.shirt.SleeveLength;
import com.danube.danube.model.product.product_information.ProductInformation;
import com.danube.danube.model.product.product_information.Shirt;

import java.util.Map;

public class ProductInformationCreator {
    public static ProductInformation createProduct(Map<String, String> product){
        if(product.get("subCategory").equals(SubCategory.SHIRT.name())){
            Shirt shirt = new Shirt();
            shirt.setColors(product.get("color"));
            shirt.setFabric(product.get("fabric"));
            shirt.setGender(Gender.valueOf(product.get("gender")));
            shirt.setSize(ShirtSize.valueOf(product.get("size")));
            shirt.setStyle(product.get("style"));
            shirt.setNeckLine(NeckLine.valueOf(product.get("neckLine")));
            shirt.setSleeveLength(SleeveLength.valueOf(product.get("sleeveLength")));
            shirt.setProductCategory(Category.valueOf(product.get("category")));

            return shirt;
        }

        throw new IncorrectProductObjectFormException();
    }
}
