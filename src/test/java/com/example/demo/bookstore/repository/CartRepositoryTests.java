package com.example.demo.bookstore.repository;

import com.example.demo.bookstore.entity.Book;
import com.example.demo.bookstore.entity.Cart;
import com.example.demo.bookstore.service.CartService;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartRepositoryTests {

    @Autowired
    CartRepository cartRepository;

    Faker faker = new Faker();

    Book bookInit = null;
    Cart cartInit = null;

    private String randomTitle() {
        return faker.lorem().characters(10, 100, true, true);
    }

    @BeforeEach
    void setup(){
        // prepare a book
        bookInit = Book.builder().title(randomTitle())
                .id(faker.random().nextInt(100, 100000))
                .author("James smith").category("JAVA")
                .isActive(true)
                .amount(100).price(BigDecimal.valueOf(50.20))
                .build();
        //
        cartInit = Cart.builder().price(bookInit.getPrice())
                .bookId(bookInit.getId())
                .amount(0)
                .userId(CartService.currentUserId)
                .total(BigDecimal.ZERO)
                .build();
        cartInit = cartRepository.save(cartInit);
    }

    @AfterEach
    void cleanUp(){
        cartRepository.deleteById(cartInit.getId());
    }

    @Test
    void test_create_cart(){
        Cart cart = Cart.builder().price(BigDecimal.valueOf(10.0))
                .bookId(faker.random().nextInt(1, 100000))
                .amount(0)
                .userId(faker.random().nextInt(1, 10000))
                .total(BigDecimal.ZERO)
                .build();
        cartRepository.save(cart);
        //
        cartRepository.delete(cart);
    }

    @Test
    void test_find_by_user_and_book(){
        Cart cart = cartRepository.findByUserIdAndBookId(CartService.currentUserId, bookInit.getId());
        Assertions.assertEquals(cartInit.getId(), cart.getId());
        Assertions.assertEquals(cartInit.getBookId(), cart.getBookId());
        Assertions.assertEquals(CartService.currentUserId, cart.getUserId());
    }

    @Test
    void test_offset_amount_incr(){
        cartRepository.offsetAmount(cartInit.getId(), 2);
        //
        Cart cart = cartRepository.findById(cartInit.getId()).orElseThrow();
        Assertions.assertEquals(2, cart.getAmount());
    }

    @Test
    void test_offset_amount_decr(){
        cartRepository.offsetAmount(cartInit.getId(), 2);
        //
        Cart cart = cartRepository.findById(cartInit.getId()).orElseThrow();
        Assertions.assertEquals(2, cart.getAmount());
        //
        cartRepository.offsetAmount(cartInit.getId(), -1);
        cart = cartRepository.findById(cartInit.getId()).orElseThrow();
        Assertions.assertEquals(1, cart.getAmount());
    }

    @Test
    void test_find_by_userId(){
        List<Cart> cartList = cartRepository.findByUserId(cartInit.getUserId());
        Assertions.assertFalse(cartList.isEmpty());
        long count = cartList.stream().filter(cart -> Objects.equals(cart.getUserId(), cartInit.getUserId())).count();
        Assertions.assertTrue(count > 0);
    }

    @Test
    void test_sum_of_price_zero_when_empty(){
        BigDecimal sum = cartRepository.getTotalPrice(cartInit.getUserId());
        Assertions.assertEquals(BigDecimal.ZERO, sum);
    }


    @Test
    void test_sum_of_gte_zero_when_added(){
        cartRepository.offsetAmount(cartInit.getId(), 2);
        //
        BigDecimal sum = cartRepository.getTotalPrice(cartInit.getUserId());
        BigDecimal expected = cartInit.getPrice().multiply(BigDecimal.valueOf(2));
        Assertions.assertEquals(expected, sum);
        //
    }

}