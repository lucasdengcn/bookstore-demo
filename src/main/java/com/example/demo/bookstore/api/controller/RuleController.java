package com.example.demo.bookstore.api.controller;

import com.example.demo.bookstore.configuration.BookstoreProperties;
import com.example.demo.bookstore.lock.DistributedLock;
import com.example.demo.bookstore.lock.DxLockInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

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
    public String info(){
        return bookstoreProperties.getRules();
    }

}