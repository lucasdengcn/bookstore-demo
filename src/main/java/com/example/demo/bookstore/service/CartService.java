package com.example.demo.bookstore.service;

import com.example.demo.bookstore.entity.Cart;
import com.example.demo.bookstore.exception.BookNotAvailableException;
import com.example.demo.bookstore.exception.EntityNotFoundException;
import com.example.demo.bookstore.mapper.CartMapper;
import com.example.demo.bookstore.model.input.CartInput;
import com.example.demo.bookstore.model.output.BookInfo;
import com.example.demo.bookstore.model.output.CartInfo;
import com.example.demo.bookstore.model.output.CartSummary;
import com.example.demo.bookstore.repository.CartRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    /**
     * fake userId for demo purpose.
     */
    public static int currentUserId = 1001;

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CartService(CartRepository cartRepository,
                       CartMapper cartMapper,
                       ApplicationEventPublisher applicationEventPublisher) {
        //
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     *
     * @param cartInput
     * @param bookInfo
     * @return
     * @throws BookNotAvailableException
     */
    public CartInfo create(CartInput cartInput, BookInfo bookInfo) throws BookNotAvailableException {
        if (null == bookInfo.getIsActive() || !bookInfo.getIsActive()){
            throw new BookNotAvailableException(bookInfo.getId());
        }
        Cart cart = cartRepository.findByUserIdAndBookId(currentUserId, cartInput.getBookId());
        if (null == cart){
            cart = cartMapper.toCart(currentUserId, cartInput, bookInfo.getPrice());
            cart = cartRepository.save(cart);
        } else {
            cartRepository.offsetAmount(cart.getId(), cartInput.getAmount());
            cart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException.CartNotFound(cart.getId()));
        }
        return cartMapper.toCartInfo(cart);
    }

    public void delete(Integer id){
        cartRepository.deleteById(id);
    }

    public List<CartInfo> findByCurrentUser(){
        List<Cart> cartList = cartRepository.findByUserId(currentUserId);
        return cartMapper.toCartInfos(cartList);
    }

    public CartInfo updateAmount(Integer cartId, int amount) {
        // check
        Cart cart = cartRepository.findById(cartId).orElseThrow(EntityNotFoundException.CartNotFound(cartId));
        // update
        cartRepository.offsetAmount(cart.getId(), amount);
        cart.setAmount(cart.getAmount() + amount);
        cart.setTotal(cart.getPrice().multiply(BigDecimal.valueOf(cart.getAmount())));
        return cartMapper.toCartInfo(cart);
    }

    public CartSummary findByCurrentUserInSummary() {
        BigDecimal total = cartRepository.getTotalPrice(currentUserId);
        return new CartSummary(total);
    }

}
