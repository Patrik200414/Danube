package com.danube.danube.configuration;

import com.danube.danube.utility.validation.Validator;
import com.danube.danube.utility.validation.request.product.ProductRequestValidator;
import com.danube.danube.utility.validation.request.product.ProductRequestValidatorImpl;
import com.danube.danube.utility.validation.request.user.UserRequestValidator;
import com.danube.danube.utility.validation.request.user.UserRequestValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestValidatorConfiguration {
    private final Validator validator;

    @Autowired
    public RequestValidatorConfiguration(Validator validator) {
        this.validator = validator;
    }

    @Bean
    public UserRequestValidator userRequestValidator(){
        return new UserRequestValidatorImpl(validator);
    }

    @Bean
    public ProductRequestValidator productRequestValidator(){
        return new ProductRequestValidatorImpl(validator);
    }
}
