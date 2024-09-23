package com.example.demo.bookstore.event;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BookAddedIntoCart {

    private int cartId;
    private int bookId;
    private int amount;
    private BigDecimal price;

}
