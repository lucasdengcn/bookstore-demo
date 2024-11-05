/* (C) 2024 */ 

package com.example.demo.bookstore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyRefreshListener implements ApplicationListener<EnvironmentChangeEvent> {

    @Autowired
    RefreshScopedService refreshScopedService;

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        log.info("refresh event: {}", event.getKeys());
        if (event.getKeys().contains("app.rules")) {
            refreshScopedService.refresh();
        }
    }
}
