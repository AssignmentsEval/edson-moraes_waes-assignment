package com.waes.diffservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    @Primary
    public SwaggerResourcesProvider swaggerResourcesProvider(InMemorySwaggerResourcesProvider defaultResourcesProvider) {
        return () -> {
            SwaggerResource wsResource = new SwaggerResource();
            wsResource.setName("Diff Service");
            wsResource.setSwaggerVersion("2.0");
            wsResource.setUrl("/swagger.yml");

//            This list contains the default generated swagger.yml
//            List<SwaggerResource> swaggerResources = new ArrayList<>(defaultResourcesProvider.get());

            List<SwaggerResource> swaggerResources = new ArrayList<>();
            swaggerResources.add(wsResource);
            return swaggerResources;
        };
    }

}