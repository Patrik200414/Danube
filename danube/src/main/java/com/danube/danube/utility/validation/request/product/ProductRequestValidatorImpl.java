package com.danube.danube.utility.validation.request.product;

import com.danube.danube.model.dto.product.ProductDetailUploadDTO;
import com.danube.danube.model.dto.product.ProductInformation;
import com.danube.danube.utility.validation.Validator;

public class ProductRequestValidatorImpl implements ProductRequestValidator{


    public static final int MAX_DELIVERY_TIME_IN_DAY = 365;
    public static final int MAX_PRODUCT_PRICE = 10000;
    public static final int MAX_PRODUCT_QUANTITY = 100000;
    public static final int MAX_PRODUCT_SHIPPING_PRICE = 1000;
    public static final int MAX_PRODUCT_RATING = 5;
    public static final int MIN_DELIVERY_TIME_IN_DAY = 1;
    public static final int MIN_PRODUCT_PRICE = 0;
    public static final int MIN_PRODUCT_QUANTITY = 0;
    public static final int MIN_PRODUCT_SHIPPING_PRICE = 0;
    public static final int MIN_PRODUCT_RATING = 0;
    public static final int MIN_PRODUCT_SOLD = 0;
    private final Validator validator;

    public ProductRequestValidatorImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void validateProductDetail(ProductDetailUploadDTO productDetailUploadDTO){
        validator.validateNumericValue(MIN_DELIVERY_TIME_IN_DAY, MAX_DELIVERY_TIME_IN_DAY, productDetailUploadDTO.deliveryTimeInDay());
        validator.validateNumericValue(MIN_PRODUCT_PRICE, MAX_PRODUCT_PRICE, productDetailUploadDTO.price());
        validator.validateNumericValue(MIN_PRODUCT_QUANTITY, MAX_PRODUCT_QUANTITY, productDetailUploadDTO.quantity());
        validator.validateNumericValue(MIN_PRODUCT_SHIPPING_PRICE, MAX_PRODUCT_SHIPPING_PRICE, productDetailUploadDTO.shippingPrice());
        validator.validateTextInputIsNotEmpty(productDetailUploadDTO.brand());
        validator.validateTextInputIsNotEmpty(productDetailUploadDTO.description());
        validator.validateTextInputIsNotEmpty(productDetailUploadDTO.productName());

    }

    @Override
    public void validateProductInformation(ProductInformation productInformation){
        validator.validateTextInputIsNotEmpty(productInformation.productName());
        validator.validateNumericValue(MIN_PRODUCT_PRICE, MAX_PRODUCT_PRICE, productInformation.price());
        validator.validateNumericValue(MIN_DELIVERY_TIME_IN_DAY, MAX_DELIVERY_TIME_IN_DAY, productInformation.deliveryTimeInDay());
        validator.validateNumericValue(MIN_PRODUCT_QUANTITY, MAX_PRODUCT_QUANTITY, productInformation.quantity());
        validator.validateNumericValue(MIN_PRODUCT_RATING, MAX_PRODUCT_RATING, productInformation.rating());
        validator.validateNumericValue(MIN_PRODUCT_SHIPPING_PRICE, MAX_PRODUCT_SHIPPING_PRICE, productInformation.shippingPrice());
        validator.validateNumericValue(MIN_PRODUCT_SOLD, productInformation.sold());
        validator.validateTextInputIsNotEmpty(productInformation.brand());
        validator.validateTextInputIsNotEmpty(productInformation.description());
        validator.validateTextInputIsNotEmpty(productInformation.seller());
    }
}
