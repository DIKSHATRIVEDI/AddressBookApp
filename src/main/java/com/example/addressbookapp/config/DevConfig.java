package com.example.addressbookapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevConfig {

    @Bean
    public String profileBean() {
        System.out.println("DEV profile active");
        return "DEV Configuration";
    }
}
