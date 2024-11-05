package com.example.demo.bookstore.api.controller;

import com.example.demo.bookstore.DemoTestsBase;
import com.example.demo.bookstore.model.input.BookCreateInput;
import com.example.demo.bookstore.model.input.BookUpdateInput;
import com.example.demo.bookstore.model.output.BookInfo;
import com.example.demo.bookstore.model.output.PageableOutput;
import com.example.demo.bookstore.service.BookService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class BookControllerTests extends DemoTestsBase {

    public static final String TITLE = "book ABC";
    public static final String AUTHOR = "James";
    public static final String CATEGORY = "Java";
    public static final BigDecimal PRICE = BigDecimal.valueOf(50.20);
    public static final int AMOUNT = 100;

    public static final String TITLE_V2 = "book ABC V2";
    public static final int AMOUNT_V2 = 110;
    public static final BigDecimal PRICE_V2 = BigDecimal.valueOf(50.50);
    public static final String APPLICATION_JSON = "application/json";

    public static Faker faker = new Faker();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BookService bookService;

    private String randomTitle() {
        return faker.lorem().characters(10, 100, true, true);
    }

    @Test
    void test_api_create_book_success() throws Exception {
        String url = "/books/v1/";
        // Prepare
        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .title(randomTitle()).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
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
                        BookInfo bookInfo = objectMapper.readValue(content, BookInfo.class);
                        //
                        Assertions.assertEquals(bookCreateInput.getTitle(), bookInfo.getTitle());
                        Assertions.assertEquals(bookCreateInput.getAuthor(), bookInfo.getAuthor());
                        Assertions.assertEquals(bookCreateInput.getAmount(), bookInfo.getAmount());
                        Assertions.assertEquals(bookCreateInput.getPrice(), bookInfo.getPrice());
                        Assertions.assertEquals(bookCreateInput.getCategory(), bookInfo.getCategory());
                    }
                });
    }

    @Test
    void test_api_create_book_with_cover_success() throws Exception {
        String url = "/books/v2/";
        // Prepare
        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .title(randomTitle()).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        String jsonValue = objectMapper.writeValueAsString(bookCreateInput);
        //
        MockPart file = new MockPart("file", "image.jpg", "image data".getBytes(), MediaType.MULTIPART_FORM_DATA);
        MockPart jsonPart = new MockPart("jsonData", "json", jsonValue.getBytes(), MediaType.APPLICATION_JSON);
        //
        MockHttpServletRequestBuilder requestBuilder = multipart(url)
                .part(file, jsonPart)
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        BookInfo bookInfo = objectMapper.readValue(content, BookInfo.class);
                        //
                        Assertions.assertEquals(bookCreateInput.getTitle(), bookInfo.getTitle());
                        Assertions.assertEquals(bookCreateInput.getAuthor(), bookInfo.getAuthor());
                        Assertions.assertEquals(bookCreateInput.getAmount(), bookInfo.getAmount());
                        Assertions.assertEquals(bookCreateInput.getPrice(), bookInfo.getPrice());
                        Assertions.assertEquals(bookCreateInput.getCategory(), bookInfo.getCategory());
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
                .title(randomTitle()).author(AUTHOR).category(CATEGORY)
                .price(PRICE_V2).amount(AMOUNT_V2).build();
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
                        BookInfo bookInfo = objectMapper.readValue(content, BookInfo.class);
                        //
                        Assertions.assertEquals(bookUpdateInput.getTitle(), bookInfo.getTitle());
                        Assertions.assertEquals(bookUpdateInput.getAuthor(), bookInfo.getAuthor());
                        Assertions.assertEquals(bookUpdateInput.getAmount(), bookInfo.getAmount());
                        Assertions.assertEquals(bookUpdateInput.getPrice(), bookInfo.getPrice());
                        Assertions.assertEquals(bookUpdateInput.getCategory(), bookInfo.getCategory());
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
                .title(randomTitle()).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        BookInfo bookInfoCreated = bookService.create(bookCreateInput);
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
                        //
                        TypeReference<PageableOutput<BookInfo>> typeRef = new TypeReference<PageableOutput<BookInfo>>() {};
                        PageableOutput<BookInfo> pageableOutput = objectMapper.readValue(content, typeRef);
                        //
                        Assertions.assertTrue(pageableOutput.getTotalItems() > 0);
                        //
                        long count = pageableOutput.getItems().stream()
                                .filter(bookInfo -> Objects.equals(bookInfo.getId(), bookInfoCreated.getId()))
                                .count();
                        Assertions.assertTrue(count > 0);
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
        String url = "/books/v1/10000/10";
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
                        //
                        TypeReference<PageableOutput<BookInfo>> typeRef = new TypeReference<PageableOutput<BookInfo>>() {};
                        PageableOutput<BookInfo> pageableOutput = objectMapper.readValue(content, typeRef);
                        //
                        Assertions.assertTrue(pageableOutput.getItems().isEmpty());
                    }
                });
    }

    @Test
    void test_api_find_available_books_deActive_should_not_include() throws Exception  {
        // Prepare Data
        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .title(randomTitle()).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        BookInfo bookInfoExclude = bookService.create(bookCreateInput);
        // change status
        bookService.updateStatus(bookInfoExclude.getId(), false);
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
                        //
                        TypeReference<PageableOutput<BookInfo>> typeRef = new TypeReference<PageableOutput<BookInfo>>() {};
                        PageableOutput<BookInfo> pageableOutput = objectMapper.readValue(content, typeRef);
                        //
                        pageableOutput.getItems().forEach(bookInfo -> assertNotEquals(bookInfo.getId(), bookInfoExclude.getId()));
                    }
                });
    }

    @Test
    void test_api_get_book_detail() throws Exception {
        // Prepare Data
        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .title(randomTitle()).author(AUTHOR).category(CATEGORY)
                .price(PRICE).amount(AMOUNT).build();
        BookInfo bookInfoExpect = bookService.create(bookCreateInput);
        //
        String url = "/books/v1/" + bookInfoExpect.getId();
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
                        BookInfo bookInfo = objectMapper.readValue(content, BookInfo.class);
                        //
                        Assertions.assertEquals(bookInfoExpect.getTitle(), bookInfo.getTitle());
                        Assertions.assertEquals(bookInfoExpect.getAuthor(), bookInfo.getAuthor());
                        Assertions.assertEquals(bookInfoExpect.getAmount(), bookInfo.getAmount());
                        Assertions.assertEquals(bookInfoExpect.getPrice(), bookInfo.getPrice());
                        Assertions.assertEquals(bookInfoExpect.getCategory(), bookInfo.getCategory());
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