package com.danube.danube.configuration;

import com.danube.danube.utility.Converter;
import com.danube.danube.utility.ConverterImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfiguration {
    @Bean
    public Converter converter(){
        return new ConverterImpl();
    }
}
