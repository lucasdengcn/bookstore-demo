package com.example.demo.bookstore.service;

import com.example.demo.bookstore.entity.Cart;
import com.example.demo.bookstore.exception.BookNotAvailableException;
import com.example.demo.bookstore.exception.EntityNotFoundException;
import com.example.demo.bookstore.mapper.BookMapper;
import com.example.demo.bookstore.mapper.CartMapper;
import com.example.demo.bookstore.model.input.CartInput;
import com.example.demo.bookstore.model.output.BookInfo;
import com.example.demo.bookstore.model.output.CartInfo;
import com.example.demo.bookstore.model.output.CartSummary;
import com.example.demo.bookstore.repository.BookRepository;
import com.example.demo.bookstore.repository.CartRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class CartServiceTests {

    @Autowired
    CartService cartService;

    @MockBean
    CartMapper cartMapper;

    @MockBean
    CartRepository cartRepository;

    Faker faker = new Faker();

    int bookId = faker.random().nextInt(100, 10000);

    int currentUserId = 1001;

    @Test
    void test_create_cart_first_time() {
        // Prepare
        CartInput cartInput = CartInput.builder().amount(1).bookId(bookId).build();
        BookInfo bookInfo = BookInfo.builder()
                .id(bookId)
                .price(BigDecimal.valueOf(10.10))
                .isActive(true)
                .build();
        Cart cart = Cart.builder()
                .price(bookInfo.getPrice())
                .userId(currentUserId)
                .bookId(bookId)
                        .amount(cartInput.getAmount())
                                .total(bookInfo.getPrice().multiply(BigDecimal.valueOf(cartInput.getAmount())))
                                        .build();
        CartInfo cartInfo = CartInfo.builder()
                .price(bookInfo.getPrice())
                .userId(currentUserId)
                .bookId(bookId)
                .amount(cartInput.getAmount())
                .total(bookInfo.getPrice().multiply(BigDecimal.valueOf(cartInput.getAmount())))
                .build();
        //
        given(cartRepository.findByUserIdAndBookId(currentUserId, bookId)).willReturn(null);
        given(cartMapper.toCart(currentUserId, cartInput, bookInfo.getPrice())).willReturn(cart);
        given(cartRepository.save(cart)).willReturn(cart);
        given(cartMapper.toCartInfo(cart)).willReturn(cartInfo);
        // execute
        CartInfo cartInfoSaved = cartService.create(cartInput, bookInfo);
        // assert
        Assertions.assertEquals(cart.getBookId(), cartInfoSaved.getBookId());
        Assertions.assertEquals(cart.getAmount(), cartInfoSaved.getAmount());
        Assertions.assertEquals(cart.getTotal(), cartInfoSaved.getTotal());
        Assertions.assertEquals(cart.getUserId(), cartInfoSaved.getUserId());
        Assertions.assertEquals(cart.getPrice(), cartInfoSaved.getPrice());
    }

    @Test
    void test_create_cart_book_invalid_status_should_throw() {
        // Prepare
        CartInput cartInput = CartInput.builder().amount(1).bookId(bookId).build();
        BookInfo bookInfo = BookInfo.builder()
                .id(bookId)
                .price(BigDecimal.valueOf(10.10))
                .isActive(null)
                .build();
        // execute
        Assertions.assertThrows(BookNotAvailableException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                cartService.create(cartInput, bookInfo);
            }
        });
    }

    @Test
    void test_create_cart_book_inactive_should_throw() {
        // Prepare
        CartInput cartInput = CartInput.builder().amount(1).bookId(bookId).build();
        BookInfo bookInfo = BookInfo.builder()
                .id(bookId)
                .price(BigDecimal.valueOf(10.10))
                .isActive(false)
                .build();
        // execute
        Assertions.assertThrows(BookNotAvailableException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                cartService.create(cartInput, bookInfo);
            }
        });
    }

    @Test
    void test_create_cart_second_time() {
        // Prepare
        CartInput cartInput = CartInput.builder().amount(1).bookId(bookId).build();
        BookInfo bookInfo = BookInfo.builder()
                .id(bookId)
                .price(BigDecimal.valueOf(10.10))
                .isActive(true)
                .build();
        Cart cart = Cart.builder()
                .price(bookInfo.getPrice())
                .id(faker.random().nextInt(10, 10000))
                .userId(currentUserId)
                .bookId(bookId)
                .amount(1)
                .total(bookInfo.getPrice().multiply(BigDecimal.valueOf(cartInput.getAmount())))
                .build();
        CartInfo cartInfo = CartInfo.builder()
                .price(bookInfo.getPrice())
                .id(cart.getId())
                .userId(currentUserId)
                .bookId(bookId)
                .amount(cartInput.getAmount() + cart.getAmount())
                .total(bookInfo.getPrice().multiply(BigDecimal.valueOf(cartInput.getAmount() + cart.getAmount())))
                .build();
        //
        given(cartRepository.findByUserIdAndBookId(currentUserId, bookId)).willReturn(cart);
        given(cartRepository.offsetAmount(cart.getId(), cartInput.getAmount())).willReturn(1);
        //
        cart.setAmount(2);
        cart.setTotal(cart.getPrice().multiply(BigDecimal.valueOf(cart.getAmount())));
        given(cartRepository.findById(cart.getId())).willReturn(Optional.of(cart));
        given(cartMapper.toCartInfo(cart)).willReturn(cartInfo);
        // execute
        CartInfo cartInfoSaved = cartService.create(cartInput, bookInfo);
        // assert
        Assertions.assertEquals(cart.getBookId(), cartInfoSaved.getBookId());
        Assertions.assertEquals(cart.getAmount(), cartInfoSaved.getAmount());
        Assertions.assertEquals(cart.getTotal(), cartInfoSaved.getTotal());
        Assertions.assertEquals(cart.getUserId(), cartInfoSaved.getUserId());
        Assertions.assertEquals(cart.getPrice(), cartInfoSaved.getPrice());
    }


    @Test
    void delete() {
        cartService.delete(100);
    }

    @Test
    void test_find_carts_by_userId() {
        BookInfo bookInfo = BookInfo.builder()
                .id(bookId)
                .price(BigDecimal.valueOf(10.10))
                .isActive(true)
                .build();
        Integer id = faker.random().nextInt(10, 10000);
        Cart cart = Cart.builder()
                .price(bookInfo.getPrice())
                .id(id)
                .userId(currentUserId)
                .bookId(bookId)
                .amount(1)
                .total(bookInfo.getPrice())
                .build();
        CartInfo cartInfo = CartInfo.builder()
                .price(bookInfo.getPrice())
                .id(id)
                .userId(currentUserId)
                .bookId(bookId)
                .amount(1)
                .total(bookInfo.getPrice())
                .build();
        //
        List<Cart> cartList = List.of(cart);
        given(cartRepository.findByUserId(currentUserId)).willReturn(cartList);
        //
        List<CartInfo> cartInfoList = cartService.findByCurrentUser();
        //
        for (int i = 0; i < cartInfoList.size(); i++) {
            CartInfo cartInfo1 = cartInfoList.get(i);
            Cart cart1 = cartList.get(i);
            //
            Assertions.assertEquals(cart.getBookId(), cartInfo1.getBookId());
            Assertions.assertEquals(cart.getAmount(), cartInfo1.getAmount());
            Assertions.assertEquals(cart.getTotal(), cartInfo1.getTotal());
            Assertions.assertEquals(cart.getUserId(), cartInfo1.getUserId());
            Assertions.assertEquals(cart.getPrice(), cartInfo1.getPrice());
        }
    }

    @Test
    void test_update_amount_incr() {
        BookInfo bookInfo = BookInfo.builder()
                .id(bookId)
                .price(BigDecimal.valueOf(10.10))
                .isActive(true)
                .build();
        Integer id = faker.random().nextInt(10, 10000);
        Cart cart = Cart.builder()
                .price(bookInfo.getPrice())
                .id(id)
                .userId(currentUserId)
                .bookId(bookId)
                .amount(1)
                .total(bookInfo.getPrice())
                .build();
        CartInfo cartInfo = CartInfo.builder()
                .price(bookInfo.getPrice())
                .id(id)
                .userId(currentUserId)
                .bookId(bookId)
                .amount(2)
                .total(bookInfo.getPrice().multiply(BigDecimal.TWO))
                .build();
        //
        given(cartRepository.findById(id)).willReturn(Optional.of(cart));
        given(cartRepository.offsetAmount(id, 1)).willReturn(1);
        given(cartMapper.toCartInfo(cart)).willReturn(cartInfo);
        //
        CartInfo cartInfoUpdated = cartService.updateAmount(id, 1);
        //
        Assertions.assertEquals(cartInfo, cartInfoUpdated);
    }

    @Test
    void test_update_amount_decr() {
        BookInfo bookInfo = BookInfo.builder()
                .id(bookId)
                .price(BigDecimal.valueOf(10.10))
                .isActive(true)
                .build();
        Integer id = faker.random().nextInt(10, 10000);
        Cart cart = Cart.builder()
                .price(bookInfo.getPrice())
                .id(id)
                .userId(currentUserId)
                .bookId(bookId)
                .amount(2)
                .total(bookInfo.getPrice().multiply(BigDecimal.TWO))
                .build();
        CartInfo cartInfo = CartInfo.builder()
                .price(bookInfo.getPrice())
                .id(id)
                .userId(currentUserId)
                .bookId(bookId)
                .amount(1)
                .total(bookInfo.getPrice().multiply(BigDecimal.ONE))
                .build();
        //
        given(cartRepository.findById(id)).willReturn(Optional.of(cart));
        given(cartRepository.offsetAmount(id, -1)).willReturn(1);
        given(cartMapper.toCartInfo(cart)).willReturn(cartInfo);
        //
        CartInfo cartInfoUpdated = cartService.updateAmount(id, -1);
        //
        Assertions.assertEquals(cartInfo, cartInfoUpdated);
    }

    @Test
    void test_update_amount_not_found_should_throw() {
        Integer id = faker.random().nextInt(10, 10000);
        //
        given(cartRepository.findById(id)).willThrow(EntityNotFoundException.class);
        //
        Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                cartService.updateAmount(id, -1);
            }
        });
    }

    @Test
    void test_calculate_summary_of_cart(){
        given(cartRepository.getTotalPrice(currentUserId)).willReturn(BigDecimal.ONE);
        CartSummary summary = cartService.findByCurrentUserInSummary();
        Assertions.assertEquals(BigDecimal.ONE, summary.getTotal());
    }

}