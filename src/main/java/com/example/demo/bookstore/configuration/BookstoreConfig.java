package com.example.demo.bookstore.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookstoreConfig {

    @Bean
    @ConfigurationProperties("app")
     public BookstoreProperties bookstoreProperties(){
        return new BookstoreProperties();
    }

}
