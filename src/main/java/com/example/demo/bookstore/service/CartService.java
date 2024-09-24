package com.example.demo.bookstore.service;

import com.example.demo.bookstore.entity.Cart;
import com.example.demo.bookstore.event.BookAddedIntoCart;
import com.example.demo.bookstore.event.BookRemovedFromCart;
import com.example.demo.bookstore.exception.BookNotAvailableException;
import com.example.demo.bookstore.exception.EntityNotFoundException;
import com.example.demo.bookstore.mapper.CartMapper;
import com.example.demo.bookstore.model.input.CartCreateInput;
import com.example.demo.bookstore.model.output.BookInfo;
import com.example.demo.bookstore.model.output.CartInfo;
import com.example.demo.bookstore.model.output.CartSummary;
import com.example.demo.bookstore.repository.CartRepository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class CartService {
    /**
     * fake userId for demo purpose.
     */
    public static int currentUserId = 1001;

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final EntityManager entityManager;

    public CartService(CartRepository cartRepository,
                       CartMapper cartMapper,
                       ApplicationEventPublisher applicationEventPublisher,
                       EntityManager entityManager) {
        //
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.applicationEventPublisher = applicationEventPublisher;
        this.entityManager = entityManager;
    }

    /**
     *
     * @param cartInput
     * @param bookInfo
     * @return
     * @throws BookNotAvailableException
     */
    @Transactional
    public CartInfo create(CartCreateInput cartInput, BookInfo bookInfo) throws BookNotAvailableException {
        if (null == bookInfo.getActive() || !bookInfo.getActive()){
            log.error("check bookInfo: {}", bookInfo);
            throw new BookNotAvailableException(bookInfo.getId());
        }
        Cart cart = cartRepository.findByUserIdAndBookId(currentUserId, cartInput.getBookId());
        log.debug("findByUserIdAndBookId: {}, {}, {}", currentUserId, cartInput.getBookId(), cart);
        if (null == cart){
            cart = cartMapper.toCart(currentUserId, cartInput, bookInfo.getPrice());
            cart.setTotal(cart.getPrice().multiply(BigDecimal.valueOf(cart.getAmount())));
            cart = cartRepository.save(cart);
        } else {
            cartRepository.offsetAmount(cart.getId(), cartInput.getAmount());
            if (entityManager.contains(cart)) {
                entityManager.refresh(cart);
            }
            cart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException.CartNotFound(cart.getId()));
        }
        //
        BookAddedIntoCart event = BookAddedIntoCart.builder()
                        .bookId(cart.getBookId()).cartId(cart.getId())
                        .price(cart.getPrice()).amount(cartInput.getAmount()).build();
        applicationEventPublisher.publishEvent(event);
        //
        return cartMapper.toCartInfo(cart);
    }

    public void delete(Integer id){
        cartRepository.deleteById(id);
    }

    public List<CartInfo> findByCurrentUser(){
        List<Cart> cartList = cartRepository.findByUserId(currentUserId);
        return cartMapper.toCartInfos(cartList);
    }

    @Transactional
    public CartInfo updateAmount(Integer cartId, int amount) {
        // check
        Cart cart = cartRepository.findById(cartId).orElseThrow(EntityNotFoundException.CartNotFound(cartId));
        // update
        cartRepository.offsetAmount(cart.getId(), amount);
        cart.setAmount(cart.getAmount() + amount);
        cart.setTotal(cart.getPrice().multiply(BigDecimal.valueOf(cart.getAmount())));
        //
        if (amount > 0) {
            BookAddedIntoCart event = BookAddedIntoCart.builder()
                    .bookId(cart.getBookId()).cartId(cart.getId()).price(cart.getPrice()).amount(amount).build();
            applicationEventPublisher.publishEvent(event);
        } else {
            BookRemovedFromCart event = BookRemovedFromCart.builder()
                    .bookId(cart.getBookId()).cartId(cart.getId()).amount(-1 * amount).build();
            applicationEventPublisher.publishEvent(event);
        }
        //
        return cartMapper.toCartInfo(cart);
    }

    public CartSummary findByCurrentUserInSummary() {
        BigDecimal total = cartRepository.getTotalPrice(currentUserId);
        if (null == total){
            total = BigDecimal.ZERO;
        }
        return new CartSummary(total);
    }

}
