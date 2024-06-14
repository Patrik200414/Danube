package com.danube.danube.configuration;

import com.danube.danube.utility.imageutility.ImageUtility;
import com.danube.danube.utility.imageutility.ImageUtilityImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageUtilityConfiguration {
    @Bean
    public ImageUtility imageUtility(){
        return new ImageUtilityImpl();
    }
}
