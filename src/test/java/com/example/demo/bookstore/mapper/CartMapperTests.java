package com.example.demo.bookstore.mapper;

import com.example.demo.bookstore.entity.Book;
import com.example.demo.bookstore.entity.Cart;
import com.example.demo.bookstore.model.input.CartInput;
import com.example.demo.bookstore.model.output.CartInfo;
import com.example.demo.bookstore.service.CartService;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartMapperTests {

    @Autowired
    CartMapper cartMapper;

    Faker faker = new Faker();

    @Test
    void test_cart_input_to_cart() {
        // Prepare
        int userId = faker.random().nextInt(10, 10000);
        CartInput cartInput = CartInput.builder().amount(1).bookId(123).build();
        //
        BigDecimal price = BigDecimal.valueOf(45.0);
        Cart cartInfo = cartMapper.toCart(userId, cartInput, price);
        //
        Assertions.assertEquals(userId, cartInfo.getUserId());
        Assertions.assertEquals(price, cartInfo.getPrice());
        Assertions.assertEquals(cartInput.getBookId(), cartInfo.getBookId());
        Assertions.assertEquals(cartInput.getAmount(), cartInfo.getAmount());
    }

    @Test
    void test_cart_input_null_to_cart() {
        // Prepare
        int userId = faker.random().nextInt(10, 10000);
        Cart cartInfo = cartMapper.toCart(userId, null, null);
        //
        Assertions.assertNull(cartInfo);
    }

    @Test
    void test_cart_input_null2_to_cart() {
        // Prepare
        int userId = faker.random().nextInt(10, 10000);
        CartInput cartInput = CartInput.builder().amount(1).bookId(123).build();
        // execute
        Cart cartInfo = cartMapper.toCart(userId, cartInput, null);
        //
        Assertions.assertEquals(userId, cartInfo.getUserId());
        Assertions.assertNull(cartInfo.getPrice());
        Assertions.assertEquals(cartInput.getBookId(), cartInfo.getBookId());
        Assertions.assertEquals(cartInput.getAmount(), cartInfo.getAmount());
    }

    @Test
    void test_car_to_cartInfo() {
        //
        Cart cart = Cart.builder()
                .price(BigDecimal.ONE)
                .bookId(faker.random().nextInt(1, 1000))
                .amount(0)
                .userId(faker.random().nextInt(1, 1000))
                .total(BigDecimal.ZERO)
                .build();
        //
        CartInfo cartInfo = cartMapper.toCartInfo(cart);
        //
        Assertions.assertEquals(cart.getBookId(), cartInfo.getBookId());
        Assertions.assertEquals(cart.getAmount(), cartInfo.getAmount());
        Assertions.assertEquals(cart.getPrice(), cartInfo.getPrice());
        Assertions.assertEquals(cart.getTotal(), cartInfo.getTotal());
    }

    @Test
    void test_carts_to_cartInfo_null() {
        //
        CartInfo cartInfo = cartMapper.toCartInfo(null);
        //
        Assertions.assertNull(cartInfo);
    }


    @Test
    void test_carts_to_cartInfos() {
        //
        Cart cart0 = Cart.builder()
                .price(BigDecimal.ONE)
                .bookId(faker.random().nextInt(1, 1000))
                .amount(0)
                .userId(faker.random().nextInt(1, 1000))
                .total(BigDecimal.ZERO)
                .build();
        List<Cart> cartList = List.of(cart0);
        //
        List<CartInfo> cartInfoList = cartMapper.toCartInfos(cartList);
        //

        for (int i = 0; i < cartInfoList.size(); i++) {
            Cart cart = cartList.get(i);
            CartInfo cartInfo = cartInfoList.get(i);
            //
            Assertions.assertEquals(cart.getBookId(), cartInfo.getBookId());
            Assertions.assertEquals(cart.getAmount(), cartInfo.getAmount());
            Assertions.assertEquals(cart.getPrice(), cartInfo.getPrice());
            Assertions.assertEquals(cart.getTotal(), cartInfo.getTotal());
        }
    }
}