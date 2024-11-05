/* (C) 2024 */ 

package com.example.demo.bookstore.configuration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "app.file.directory=public/images/")
class BookstoreConfigTests {

    @Autowired
    BookstoreConfig bookstoreConfig;

    @Autowired
    BookstoreProperties bookstoreProperties;

    @Test
    public void test_config_value() throws IOException {
        assertNotNull(bookstoreProperties);
        assertNotNull(bookstoreProperties.getFile());
        assertNotNull(bookstoreProperties.getFile().getDirectory());
        assertEquals("public/images/", bookstoreProperties.getFile().getDirectory());
        System.out.println(bookstoreProperties.getRelativePath());
    }
}
