/* (C) 2024 */ 

package com.example.demo.bookstore.api.controller;

import com.example.demo.bookstore.configuration.BookstoreProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Rules APIs")
@RestController
@RequestMapping("/rules")
@Slf4j
public class RuleController {

    @Autowired
    private BookstoreProperties bookstoreProperties;

    @Operation(description = "get rules")
    @GetMapping("/v1/")
    @ResponseStatus(HttpStatus.OK)
    public String info() {
        return bookstoreProperties.getRules();
    }
}
