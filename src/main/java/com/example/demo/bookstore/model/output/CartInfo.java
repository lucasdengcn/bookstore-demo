/* (C) 2024 */ 

package com.example.demo.bookstore.model.output;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "cart item information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartInfo {

    @Schema(description = "cart identifier for a item")
    private Integer id;

    @Schema(description = "cart owner's identifier")
    private Integer userId;

    @Schema(description = "cart book identifier")
    private Integer bookId;

    @Schema(description = "purchasing amount for a item")
    private int amount;

    @Schema(description = "item price per unit")
    private BigDecimal price;

    @Schema(description = "total price")
    private BigDecimal total;

    @Schema(description = "item's information")
    private BookInfo bookInfo;
}
