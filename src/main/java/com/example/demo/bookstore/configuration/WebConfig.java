package com.example.demo.bookstore.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private BookstoreProperties bookstoreProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("addResourceHandlers: {}", bookstoreProperties);
        String fileDirectory = bookstoreProperties.getFile().getDirectory();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + fileDirectory);
        log.info("addResourceHandlers DONE. {}", fileDirectory);
    }
}
