package com.example.demo.bookstore.api.controller;

import com.example.demo.bookstore.model.input.CartCreateInput;
import com.example.demo.bookstore.model.input.CartUpdateInput;
import com.example.demo.bookstore.model.output.BookInfo;
import com.example.demo.bookstore.model.output.CartInfo;
import com.example.demo.bookstore.service.BookService;
import com.example.demo.bookstore.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Shopping Cart APIs")
@RestController
@RequestMapping("/carts")
@Slf4j
public class CartController {

    private final CartService cartService;
    private final BookService bookService;

    public CartController(CartService cartService, BookService bookService) {
        this.cartService = cartService;
        this.bookService = bookService;
    }

    @Operation(description = "Create a cart on current user")
    @PostMapping("/v1/book/")
    @ResponseStatus(HttpStatus.CREATED)
    public CartInfo create(@ParameterObject @Valid @RequestBody CartCreateInput input) {
        BookInfo bookInfo = bookService.findById(input.getBookId());
        return cartService.create(input, bookInfo);
    }

    @Operation(description = "Update cart's amount of a book")
    @PutMapping("/v1/{id}/book")
    @ResponseStatus(HttpStatus.OK)
    public CartInfo put(@PathVariable("id") Integer id, @ParameterObject @Valid @RequestBody CartUpdateInput input){
        return cartService.updateAmount(id, input.getAmount());
    }

    @Operation(description = "get items in current user's cart")
    @GetMapping("/v1/books")
    @ResponseStatus(HttpStatus.OK)
    public List<CartInfo> findCartItems(){
        return cartService.findByCurrentUser();
    }

}