package com.danube.danube.configuration;

import com.danube.danube.service.utility.product_information_creator.ProductInformationCreator;
import com.danube.danube.service.utility.product_information_creator.ProductInformationCreatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductInformationCreatorConfiguration {
    @Bean
    public ProductInformationCreator productInformationCreator(){
        return new ProductInformationCreatorImpl();
    }
}
