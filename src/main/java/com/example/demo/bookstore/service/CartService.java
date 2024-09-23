package com.example.demo.bookstore.service;

import com.example.demo.bookstore.mapper.CartMapper;
import com.example.demo.bookstore.model.input.CartInput;
import com.example.demo.bookstore.model.output.CartInfo;
import com.example.demo.bookstore.repository.CartRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

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

    public CartInfo create(CartInput cartInput){
        return null;
    }

    public void delete(Integer id){

    }

    public List<CartInfo> findByUserId(Integer userId){
        return null;
    }

    public CartInfo updateAmount(Integer cartId, Integer amount) {
        return null;
    }

}
