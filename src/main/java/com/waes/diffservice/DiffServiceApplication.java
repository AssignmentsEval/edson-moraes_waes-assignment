package com.waes.diffservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DiffServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiffServiceApplication.class, args);
    }

}
