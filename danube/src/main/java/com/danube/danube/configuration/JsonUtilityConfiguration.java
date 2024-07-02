package com.danube.danube.configuration;

import com.danube.danube.utility.json.JsonUtility;
import com.danube.danube.utility.json.JsonUtilityImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonUtilityConfiguration {

    @Bean
    public JsonUtility jsonUtility(){
        return new JsonUtilityImpl();
    }
}
