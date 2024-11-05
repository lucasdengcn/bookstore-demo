/* (C) 2024 */ 

package com.example.demo.bookstore.event;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookAddedIntoCart {

    private int cartId;
    private int bookId;
    private int amount;
    private BigDecimal price;
}
