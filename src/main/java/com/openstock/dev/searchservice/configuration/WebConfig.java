package com.openstock.dev.searchservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS to all endpoints
                .allowedOrigins("https://www.myopenstock.com") // Allow only this origin; use "*" for allowing all
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("Authorization", "Content-Type") // Allowed headers
                .allowCredentials(true) // Allow credentials
                .maxAge(3600); // Max age before a preflight request needs to be done again
    }
}