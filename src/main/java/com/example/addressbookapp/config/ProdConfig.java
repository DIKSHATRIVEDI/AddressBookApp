package com.example.addressbookapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdConfig {

    @Bean
    public String profileBean() {
        System.out.println("PROD profile active");
        return "PROD Configuration";
    }
}
