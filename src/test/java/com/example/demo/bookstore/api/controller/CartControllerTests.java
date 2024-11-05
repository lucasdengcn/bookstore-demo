package com.example.demo.bookstore.api.controller;

import com.example.demo.bookstore.DemoTestsBase;
import com.example.demo.bookstore.entity.Cart;
import com.example.demo.bookstore.model.input.BookCreateInput;
import com.example.demo.bookstore.model.input.CartCreateInput;
import com.example.demo.bookstore.model.input.CartUpdateInput;
import com.example.demo.bookstore.model.output.BookInfo;
import com.example.demo.bookstore.model.output.CartInfo;
import com.example.demo.bookstore.model.output.PageableOutput;
import com.example.demo.bookstore.repository.CartRepository;
import com.example.demo.bookstore.service.BookService;
import com.example.demo.bookstore.service.CartService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class CartControllerTests extends DemoTestsBase {

    public static final String APPLICATION_JSON = "application/json";
    public static Faker faker = new Faker();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BookService bookService;

    @Autowired
    CartRepository cartRepository;

    BookInfo bookInfo;

    private String randomTitle() {
        return faker.lorem().characters(10, 100, true, true);
    }

    @BeforeEach
    void setUpBooks(){
        BookCreateInput bookCreateInput = BookCreateInput.builder()
                .title(randomTitle()).author(faker.name().name()).category("Java")
                .price(BigDecimal.valueOf(10.10)).amount(5).build();
        bookInfo = bookService.create(bookCreateInput);
    }

    @Test
    void test_add_book_into_cart_and_check_book_status() throws Exception {
        //
        int amount = 5;
        CartCreateInput input = CartCreateInput.builder().amount(amount).bookId(bookInfo.getId()).build();
        String url = "/carts/v1/book/";
        //
        MockHttpServletRequestBuilder requestBuilder = post(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        CartInfo cartInfo = objectMapper.readValue(content, CartInfo.class);
                        //
                        Assertions.assertEquals(CartService.currentUserId, cartInfo.getUserId());
                        Assertions.assertEquals(bookInfo.getId(), cartInfo.getBookId());
                        Assertions.assertEquals(bookInfo.getPrice(), cartInfo.getPrice());
                        Assertions.assertEquals(amount, cartInfo.getAmount());
                        Assertions.assertEquals(bookInfo.getPrice(), cartInfo.getPrice());
                        //
                        BigDecimal total = bookInfo.getPrice().multiply(BigDecimal.valueOf(amount));
                        Assertions.assertEquals(total, cartInfo.getTotal());
                        //
                        BookInfo bookInfoUpdated = bookService.findById(bookInfo.getId());
                        Assertions.assertEquals(0, bookInfoUpdated.getAmount());
                        Assertions.assertFalse(bookInfoUpdated.getActive());
                    }
                });
    }

    @Test
    void test_create_with_valid_input_on_first() throws Exception {
        //
        CartCreateInput input = CartCreateInput.builder().amount(1).bookId(bookInfo.getId()).build();
        String url = "/carts/v1/book/";
        //
        MockHttpServletRequestBuilder requestBuilder = post(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        CartInfo cartInfo = objectMapper.readValue(content, CartInfo.class);
                        //
                        Assertions.assertEquals(CartService.currentUserId, cartInfo.getUserId());
                        Assertions.assertEquals(bookInfo.getId(), cartInfo.getBookId());
                        Assertions.assertEquals(bookInfo.getPrice(), cartInfo.getPrice());
                        Assertions.assertEquals(1, cartInfo.getAmount());
                        Assertions.assertEquals(bookInfo.getPrice(), cartInfo.getPrice());
                        Assertions.assertEquals(bookInfo.getPrice(), cartInfo.getTotal());
                    }
                });
    }

    @Test
    void test_create_with_valid_input_on_second() throws Exception {
        // Prepare a data
        Cart cartInit = Cart.builder().price(bookInfo.getPrice())
                .bookId(bookInfo.getId())
                .amount(1)
                .userId(CartService.currentUserId)
                .total(bookInfo.getPrice())
                .build();
        cartRepository.save(cartInit);
        //
        //
        CartCreateInput input = CartCreateInput.builder().amount(1).bookId(bookInfo.getId()).build();
        String url = "/carts/v1/book/";
        //
        MockHttpServletRequestBuilder requestBuilder = post(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        CartInfo cartInfo = objectMapper.readValue(content, CartInfo.class);
                        //
                        Assertions.assertEquals(CartService.currentUserId, cartInfo.getUserId());
                        Assertions.assertEquals(bookInfo.getId(), cartInfo.getBookId());
                        Assertions.assertEquals(bookInfo.getPrice(), cartInfo.getPrice());
                        Assertions.assertEquals(2, cartInfo.getAmount());
                        Assertions.assertEquals(bookInfo.getPrice(), cartInfo.getPrice());
                        //
                        BigDecimal total = bookInfo.getPrice().multiply(BigDecimal.TWO);
                        Assertions.assertEquals(total, cartInfo.getTotal());
                    }
                });
    }

    @Test
    void test_create_with_invalid_input_should_return_400() throws Exception {
        //
        //
        CartCreateInput input = CartCreateInput.builder().amount(10000).bookId(bookInfo.getId()).build();
        String url = "/carts/v1/book/";
        //
        MockHttpServletRequestBuilder requestBuilder = post(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
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
    void test_create_with_invalid_input2_should_return_400() throws Exception {
        //
        CartCreateInput input = CartCreateInput.builder().amount(-10).bookId(bookInfo.getId()).build();
        String url = "/carts/v1/book/";
        //
        MockHttpServletRequestBuilder requestBuilder = post(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
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
    void test_change_cart_amount_incr_valid_input() throws Exception {
        // Prepare a Cart
        Cart cartInit = prepareCart();
        //
        CartUpdateInput input = CartUpdateInput.builder().amount(1).bookId(bookInfo.getId()).build();
        String url = "/carts/v1/"+cartInit.getId()+"/book";
        //
        MockHttpServletRequestBuilder requestBuilder = put(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        CartInfo cartInfo = objectMapper.readValue(content, CartInfo.class);
                        //
                        int amount = cartInit.getAmount() + 1;
                        //
                        Assertions.assertEquals(CartService.currentUserId, cartInfo.getUserId());
                        Assertions.assertEquals(bookInfo.getId(), cartInfo.getBookId());
                        Assertions.assertEquals(bookInfo.getPrice(), cartInfo.getPrice());
                        Assertions.assertEquals(amount, cartInfo.getAmount());
                        Assertions.assertEquals(bookInfo.getPrice(), cartInfo.getPrice());
                        //
                        BigDecimal total = bookInfo.getPrice().multiply(BigDecimal.valueOf(amount));
                        Assertions.assertEquals(total, cartInfo.getTotal());
                    }
                });
    }

    private Cart prepareCart() {
        Cart cartInit = cartRepository.findByUserIdAndBookId(CartService.currentUserId, bookInfo.getId());
        if (null == cartInit) {
            cartInit = Cart.builder().price(bookInfo.getPrice())
                    .bookId(bookInfo.getId())
                    .amount(1)
                    .userId(CartService.currentUserId)
                    .total(bookInfo.getPrice())
                    .build();
            cartInit = cartRepository.save(cartInit);
        }
        return cartInit;
    }

    @Test
    void test_change_cart_amount_decr_valid_input() throws Exception {
        // Prepare a Cart
        Cart cartInit = prepareCart();
        //
        CartUpdateInput input = CartUpdateInput.builder().amount(-1).bookId(bookInfo.getId()).build();
        String url = "/carts/v1/"+cartInit.getId()+"/book";
        //
        MockHttpServletRequestBuilder requestBuilder = put(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
                .accept(APPLICATION_JSON);
        // execute and assert
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult result) throws Exception {
                        String content = result.getResponse().getContentAsString();
                        CartInfo cartInfo = objectMapper.readValue(content, CartInfo.class);
                        //
                        int amount = cartInit.getAmount() - 1;
                        //
                        Assertions.assertEquals(CartService.currentUserId, cartInfo.getUserId());
                        Assertions.assertEquals(bookInfo.getId(), cartInfo.getBookId());
                        Assertions.assertEquals(bookInfo.getPrice(), cartInfo.getPrice());
                        Assertions.assertEquals(amount, cartInfo.getAmount());
                        Assertions.assertEquals(bookInfo.getPrice(), cartInfo.getPrice());
                        //
                        BigDecimal total = bookInfo.getPrice().multiply(BigDecimal.valueOf(amount));
                        Assertions.assertEquals(total, cartInfo.getTotal());
                    }
                });
    }

    @Test
    void test_change_cart_amount_invalid_input_should_400() throws Exception {
        //
        CartUpdateInput input = CartUpdateInput.builder().amount(100).bookId(bookInfo.getId()).build();
        String url = "/carts/v1/1/book";
        //
        MockHttpServletRequestBuilder requestBuilder = put(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
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
    void test_change_cart_amount_invalid_input2_should_400() throws Exception {
        //
        CartUpdateInput input = CartUpdateInput.builder().amount(-100).bookId(bookInfo.getId()).build();
        String url = "/carts/v1/1/book";
        //
        MockHttpServletRequestBuilder requestBuilder = put(url).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
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
    void test_find_current_user_carts() throws Exception {

        String url = "/carts/v1/books";
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
                        TypeReference<List<CartInfo>> typeRef = new TypeReference<List<CartInfo>>() {};
                        List<CartInfo> cartInfoList = objectMapper.readValue(content, typeRef);
                        //
                        cartInfoList.forEach(new Consumer<CartInfo>() {
                            @Override
                            public void accept(CartInfo cartInfo) {
                                Assertions.assertNotNull(cartInfo.getBookInfo());
                                Assertions.assertNotNull(cartInfo.getTotal());
                                Assertions.assertNotNull(cartInfo.getPrice());
                            }
                        });
                    }
                });

    }
}