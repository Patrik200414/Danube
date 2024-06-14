package com.danube.danube.configuration;

import com.danube.danube.security.jwt.JwtUtils;
import com.danube.danube.utility.converter.categoriesanddetails.ProductCategoriesAndDetailsConverter;
import com.danube.danube.utility.converter.categoriesanddetails.ProductCategoriesAndDetailsConverterImpl;
import com.danube.danube.utility.converter.converterhelper.ConverterHelper;
import com.danube.danube.utility.converter.converterhelper.ConverterHelperImpl;
import com.danube.danube.utility.converter.productview.ProductViewConverter;
import com.danube.danube.utility.converter.productview.ProductViewConverterImpl;
import com.danube.danube.utility.converter.uploadproduct.ProductUploadConverter;
import com.danube.danube.utility.converter.uploadproduct.ProductUploadConverterImpl;
import com.danube.danube.utility.converter.user.UserConverter;
import com.danube.danube.utility.converter.user.UserConverterImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfiguration {
    @Bean
    public ProductCategoriesAndDetailsConverter productCategoriesAndDetailsConverter(){
        return new ProductCategoriesAndDetailsConverterImpl();
    }

    @Bean
    public ProductViewConverter productViewConverter(){
        return new ProductViewConverterImpl();
    }

    @Bean
    public ProductUploadConverter productUploadConverter(){
        return new ProductUploadConverterImpl();
    }

    @Bean
    public UserConverter userConverter(){
        return new UserConverterImpl();
    }
    @Bean
    public ConverterHelper converterHelper(){
        return new ConverterHelperImpl();
    }
}
