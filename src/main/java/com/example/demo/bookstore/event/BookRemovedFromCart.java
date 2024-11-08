/* (C) 2024 */ 

package com.example.demo.bookstore.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema
@Data
@Builder
public class BookRemovedFromCart {

    private int cartId;
    private int bookId;
    private int amount;
}
