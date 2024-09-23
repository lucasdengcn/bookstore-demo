package com.example.demo.bookstore.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Schema(description = "cart submission information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartInput {

    @Schema(description = "book's unique identifier")
    @NotNull(message = "please provide book's info")
    private Integer bookId;

    @Schema(description = "amount to buy of a book")
    @Max(value = 1000, message = "book's amount up to 1000.")
    @Min(value = 1, message = "book's amount should be in range from 1 to 1000.")
    private int amount;

}
