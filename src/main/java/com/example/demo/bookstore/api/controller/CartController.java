package com.example.demo.bookstore.api.controller;

import com.example.demo.bookstore.model.input.CartInput;
import com.example.demo.bookstore.model.output.CartInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Shopping Cart APIs")
@RestController
@RequestMapping("/carts")
public class CartController {

    int userId = 1001;

    @Operation(description = "Create a cart on current user")
    @PostMapping("/v1/book/")
    public CartInfo create(@ParameterObject @Valid @RequestBody CartInput input) {
        return null;
    }

    @Operation(description = "Update cart's amount of a book")
    @PutMapping("/v1/{id}/book")
    public CartInfo put(@PathVariable("id") Integer id, @ParameterObject @Valid @RequestBody CartInput input){
        return null;
    }

    @Operation(description = "get items in current user's cart")
    @GetMapping("/v1/books")
    public List<CartInfo> findCartItems(){
        return null;
    }

}
