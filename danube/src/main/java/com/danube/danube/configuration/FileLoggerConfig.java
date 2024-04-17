package com.danube.danube.configuration;

import com.danube.danube.utility.filellogger.FileLogger;
import com.danube.danube.utility.filellogger.FileLoggerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileLoggerConfig {
    @Bean
    public FileLogger fileLogger(){
        return new FileLoggerImpl();
    }
}
