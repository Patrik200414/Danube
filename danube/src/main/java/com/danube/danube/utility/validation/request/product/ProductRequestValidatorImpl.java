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
    public static final int MIN_LENGTH_NAME_INPUT = 2;
    public static final int MAX_LENGTH_NAME_INPUT = 255;
    public static final int MIN_LENGTH_DESCRIPTION = 3;
    public static final int MAX_LENGTH_DESCRIPTION = 1000;
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
        validator.validateTextInput(productDetailUploadDTO.brand(), MIN_LENGTH_NAME_INPUT, MAX_LENGTH_NAME_INPUT);
        validator.validateTextInput(productDetailUploadDTO.description(), MIN_LENGTH_DESCRIPTION, MAX_LENGTH_DESCRIPTION);
        validator.validateTextInput(productDetailUploadDTO.productName(), MIN_LENGTH_NAME_INPUT, MAX_LENGTH_NAME_INPUT);

    }

    @Override
    public void validateProductInformation(ProductInformation productInformation){
        validator.validateTextInput(productInformation.productName(), MIN_LENGTH_NAME_INPUT, MAX_LENGTH_NAME_INPUT);
        validator.validateNumericValue(MIN_PRODUCT_PRICE, MAX_PRODUCT_PRICE, productInformation.price());
        validator.validateNumericValue(MIN_DELIVERY_TIME_IN_DAY, MAX_DELIVERY_TIME_IN_DAY, productInformation.deliveryTimeInDay());
        validator.validateNumericValue(MIN_PRODUCT_QUANTITY, MAX_PRODUCT_QUANTITY, productInformation.quantity());
        validator.validateNumericValue(MIN_PRODUCT_RATING, MAX_PRODUCT_RATING, productInformation.rating());
        validator.validateNumericValue(MIN_PRODUCT_SHIPPING_PRICE, MAX_PRODUCT_SHIPPING_PRICE, productInformation.shippingPrice());
        validator.validateNumericValue(MIN_PRODUCT_SOLD, productInformation.sold());
        validator.validateTextInput(productInformation.brand(), MIN_LENGTH_NAME_INPUT, MAX_LENGTH_NAME_INPUT);
        validator.validateTextInput(productInformation.description(), MIN_LENGTH_DESCRIPTION, MAX_LENGTH_DESCRIPTION);
        validator.validateTextInput(productInformation.seller(), MIN_LENGTH_NAME_INPUT, MAX_LENGTH_NAME_INPUT);
    }
}
