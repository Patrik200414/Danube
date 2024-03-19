package com.danube.danube.configuration;

import com.danube.danube.service.utility.converter.Converter;
import com.danube.danube.service.utility.converter.ConverterImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfiguration {
    @Bean
    public Converter converter(){
        return new ConverterImpl();
    }
}
