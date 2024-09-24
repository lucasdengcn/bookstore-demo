package com.example.demo.bookstore.api.controller;

import com.example.demo.bookstore.model.output.CartSummary;
import com.example.demo.bookstore.repository.CartRepository;
import com.example.demo.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CheckoutControllerTests {

    public static final String APPLICATION_JSON = "application/json";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BookService bookService;

    @Autowired
    CartRepository cartRepository;

    @Test
    void test_having_carts_should_gte_0() throws Exception {
        //
        String url = "/checkouts/v1/summary";
        //
        MockHttpServletRequestBuilder requestBuilder = get(url)
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        CartSummary cartSummary = objectMapper.readValue(content, CartSummary.class);
                        Assertions.assertTrue(cartSummary.getTotal().compareTo(BigDecimal.ZERO) > 0);
                    }
                });

    }

    @Test
    void test_no_carts_should_equal_0() throws Exception {
        //
        String url = "/checkouts/v1/summary";
        //
        MockHttpServletRequestBuilder requestBuilder = get(url)
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        CartSummary cartSummary = objectMapper.readValue(content, CartSummary.class);
                        assertEquals(1, cartSummary.getTotal().compareTo(BigDecimal.ZERO));
                    }
                });

    }
}