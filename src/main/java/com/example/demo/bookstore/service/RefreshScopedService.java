/* (C) 2024 */ 

package com.example.demo.bookstore.service;

import com.example.demo.bookstore.configuration.BookstoreProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RefreshScopedService {

    @Autowired
    private BookstoreProperties bookstoreProperties;

    @PostConstruct
    public void refresh() {
        log.info("running on : {}", bookstoreProperties.getRules());
    }
}
