package com.example.demo.bookstore.api.controller;

import com.example.demo.bookstore.model.input.BookCreateInput;
import com.example.demo.bookstore.model.input.BookUpdateInput;
import com.example.demo.bookstore.model.output.BookInfo;
import com.example.demo.bookstore.model.output.PageableOutput;
import com.example.demo.bookstore.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTests {

    public static final String TITLE = "book ABC";
    public static final String AUTHOR = "James";
    public static final String CATEGORY = "Java";
    public static final BigDecimal PRICE = BigDecimal.valueOf(50.20);
    public static final int AMOUNT = 100;
    public static final int BOOK_ID = 1;

    public static final String TITLE_V2 = "book ABC V2";
    public static final int AMOUNT_V2 = 110;
    public static final BigDecimal PRICE_V2 = BigDecimal.valueOf(50.50);
    public static final int PAGE_SIZE = 10;
    public static final int PAGE_NUMBER = 0;
    public static final String APPLICATION_JSON = "application/json";

    public static Faker faker = new Faker();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BookService bookService;


    @Test
    void test_api_create_book_success() throws Exception {
        String url = "/books/v1/";
        // Prepare
        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .title(TITLE).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        //
        BookInfo bookInfo = BookInfo.builder().title(TITLE).author(AUTHOR)
                .category(CATEGORY).price(PRICE)
                .id(BOOK_ID).amount(AMOUNT).build();
        //
        MockHttpServletRequestBuilder requestBuilder = post(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookCreateInput))
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        BookInfo value = objectMapper.readValue(content, BookInfo.class);
                        Assertions.assertEquals(bookInfo, value);
                    }
                });
    }

    @Test
    void test_api_create_book_invalid_inputs_should_return_bad_request() throws Exception {
        String url = "/books/v1/";
        // Prepare
        String invalidTitle = "ab09AZ!*&^^@**@@@(@(@";
        String invalidAuthor = "James*&^^";
        String invalidCategory = "Java@#$$%";

        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .title(invalidTitle).author(invalidAuthor).category(invalidCategory)
                .price(PRICE).amount(AMOUNT).build();
        //
        MockHttpServletRequestBuilder requestBuilder = post(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookCreateInput))
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        ProblemDetail problemDetail = objectMapper.readValue(content, ProblemDetail.class);
                        Assertions.assertEquals(400, problemDetail.getStatus());
                        Assertions.assertEquals("Bad Request", problemDetail.getTitle());
                    }
                });
    }

    @Test
    void test_api_create_book_missing_required_inputs_should_return_bad_request() throws Exception {
        String url = "/books/v1/";
        // Prepare
        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        //
        MockHttpServletRequestBuilder requestBuilder = post(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookCreateInput))
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        ProblemDetail problemDetail = objectMapper.readValue(content, ProblemDetail.class);
                        Assertions.assertEquals(400, problemDetail.getStatus());
                        Assertions.assertEquals("Bad Request", problemDetail.getTitle());
                    }
                });
    }

    @Test
    void test_api_update_book() throws Exception  {
        String url = "/books/v1/1";
        // Prepare
        BookUpdateInput bookUpdateInput = BookUpdateInput.builder()
                .title(TITLE_V2).author(AUTHOR).category(CATEGORY)
                .price(PRICE_V2).amount(AMOUNT_V2).build();
        //
        BookInfo bookInfo = BookInfo.builder().title(TITLE_V2).author(AUTHOR)
                .category(CATEGORY).price(PRICE_V2)
                .id(BOOK_ID).amount(AMOUNT_V2).build();
        //
        MockHttpServletRequestBuilder requestBuilder = put(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookUpdateInput))
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        BookInfo value = objectMapper.readValue(content, BookInfo.class);
                        Assertions.assertEquals(bookInfo, value);
                    }
                });

    }

    @Test
    void test_api_update_book_invalid_inputs_should_return_bad_request() throws Exception {
        String url = "/books/v1/1";
        // Prepare
        String invalidTitle = "ab09AZ!*&^^@**@@@(@(@";
        String invalidAuthor = "James*&^^";
        String invalidCategory = "Java@#$$%";

        BookUpdateInput updateInput = BookUpdateInput.builder()
                .title(invalidTitle).author(invalidAuthor).category(invalidCategory)
                .price(PRICE).amount(AMOUNT).build();
        //
        MockHttpServletRequestBuilder requestBuilder = put(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateInput))
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        ProblemDetail problemDetail = objectMapper.readValue(content, ProblemDetail.class);
                        Assertions.assertEquals(400, problemDetail.getStatus());
                        Assertions.assertEquals("Bad Request", problemDetail.getTitle());
                    }
                });
    }

    @Test
    void test_api_find_available_books() throws Exception  {
        // Prepare Data
        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .title(TITLE).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        bookService.create(bookCreateInput);
        //
        String url = "/books/v1/0/10";
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
                        PageableOutput<BookInfo> pageableOutput = objectMapper.readValue(content, PageableOutput.class);
                        Assertions.assertEquals(1, pageableOutput.getTotalItems());
                    }
                });
    }

    @Test
    void test_api_find_available_books_incorrect_pageIndex_should_return_empty() throws Exception  {
        // Prepare Data
        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .title(TITLE).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        bookService.create(bookCreateInput);
        //
        String url = "/books/v1/10/10";
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
                        PageableOutput<BookInfo> pageableOutput = objectMapper.readValue(content, PageableOutput.class);
                        Assertions.assertTrue(pageableOutput.getItems().isEmpty());
                    }
                });
    }

    @Test
    void test_api_find_available_books_all_deActive_should_return_empty() throws Exception  {
        // Prepare Data
        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .title(TITLE).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        BookInfo bookInfo = bookService.create(bookCreateInput);
        // change status
        bookService.updateStatus(bookInfo.getId(), false);
        // execute query
        String url = "/books/v1/0/10";
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
                        PageableOutput<BookInfo> pageableOutput = objectMapper.readValue(content, PageableOutput.class);
                        Assertions.assertEquals(0, pageableOutput.getTotalItems());
                    }
                });
    }

    @Test
    void test_api_get_book_detail() throws Exception {
        // Prepare Data
        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .title(TITLE).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        bookService.create(bookCreateInput);
        //
        String url = "/books/v1/1";
        //
        BookInfo bookInfo = BookInfo.builder().title(TITLE).author(AUTHOR)
                .category(CATEGORY).price(PRICE)
                .id(BOOK_ID).amount(AMOUNT).build();
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
                        BookInfo value = objectMapper.readValue(content, BookInfo.class);
                        Assertions.assertEquals(bookInfo, value);
                    }
                });
    }

    @Test
    void test_api_get_book_detail_non_exist_should_return_404() throws Exception {
        //
        String url = "/books/v1/1000000";
        //
        MockHttpServletRequestBuilder requestBuilder = get(url)
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        ProblemDetail problemDetail = objectMapper.readValue(content, ProblemDetail.class);
                        Assertions.assertEquals(404, problemDetail.getStatus());
                        Assertions.assertEquals("Not Found", problemDetail.getTitle());
                    }
                });
    }
}