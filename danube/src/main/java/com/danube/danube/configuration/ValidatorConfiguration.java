package com.danube.danube.configuration;

import com.danube.danube.utility.validation.Validator;
import com.danube.danube.utility.validation.ValidatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfiguration {
    @Bean
    public Validator validator(){
        return new ValidatorImpl();
    }
}
