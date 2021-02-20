package com.waes.diffservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class BeanConfiguration {

    @Bean
    public Base64.Decoder getBase64Decoder(){
        return Base64.getDecoder();
    }
}
