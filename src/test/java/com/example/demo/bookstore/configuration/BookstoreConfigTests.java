package com.example.demo.bookstore.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "app.file.directory=public/images/")
class BookstoreConfigTests {

    @Autowired
    BookstoreConfig bookstoreConfig;

    @Autowired
    BookstoreProperties bookstoreProperties;

    @Test
    public void test_config_value() throws IOException {
        assertNotNull(bookstoreProperties);
        assertNotNull(bookstoreProperties.file);
        assertNotNull(bookstoreProperties.file.getDirectory());
        assertEquals("public/images/", bookstoreProperties.file.getDirectory());
        System.out.println(bookstoreProperties.getRelativePath());
    }

}