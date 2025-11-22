/* (C) 2024 */ 

package com.example.demo;

import com.example.demo.bookstore.DemoTestsBase;
import com.example.demo.bookstore.configuration.BookstoreProperties;
import com.example.demo.bookstore.tenancy.TenantDataSourceProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DemoApplicationTests extends DemoTestsBase {

    @Autowired
    private BookstoreProperties bookstoreProperties;

    @Autowired
    private TenantDataSourceProperties multiTenancyDataSources;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(bookstoreProperties.getFile());
        Assertions.assertNotNull(multiTenancyDataSources.getDatasources());
    }
}
