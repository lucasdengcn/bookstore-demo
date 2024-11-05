/* (C) 2024 */ 

package com.example.demo.bookstore.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "cart update information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartUpdateInput {

    @Schema(description = "book's unique identifier")
    @NotNull(message = "please provide book's identifier") private Integer bookId;

    @Schema(description = "amount allow to buy a book per user")
    @Max(value = 10, message = "book's amount up to 10.")
    @Min(value = -10, message = "book's amount should be in range from -10 to 10.")
    private int amount;
}
