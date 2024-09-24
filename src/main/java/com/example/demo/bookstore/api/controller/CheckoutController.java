package com.example.demo.bookstore.api.controller;

import com.example.demo.bookstore.model.output.CartSummary;
import com.example.demo.bookstore.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Shopping Cart APIs")
@RestController
@RequestMapping("/checkouts")
public class CheckoutController {

    private final CartService cartService;

    public CheckoutController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(description = "checkout current user's cart's summary")
    @GetMapping("/v1/summary")
    @ResponseStatus(HttpStatus.OK)
    public CartSummary summary(){
        CartSummary cartSummary = cartService.findByCurrentUserInSummary();
        return cartSummary;
    }

}