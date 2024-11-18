/* (C) 2024 */ 

package com.example.demo.bookstore.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Information for Creating a Book")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateInput {

    @Schema(description = "Book's title", requiredMode = Schema.RequiredMode.REQUIRED, example = "Practice Java")
    @NotEmpty(message = "please provide book's title")
    @Length(max = 100, message = "book's title should be limited in 100 characters") @Pattern(regexp = "[0-9a-zA-Z\\s]{1,100}", message = "book's title only allow alphabets and numbers")
    private String title;

    @Schema(description = "Book's author", requiredMode = Schema.RequiredMode.REQUIRED, example = "John Mark")
    @NotEmpty(message = "please provide book's author")
    @Length(max = 20, message = "book's author should be limited in 20 characters") @Pattern(regexp = "[0-9a-zA-Z\\s]{1,20}", message = "book's author only allow alphabets and numbers")
    private String author;

    @Schema(description = "Book's amount", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @Max(value = 1000, message = "book's amount up to 1000.")
    @Min(value = 1, message = "book's amount should be in range from 1 to 1000.")
    private int amount;

    @Schema(description = "Book's price", requiredMode = Schema.RequiredMode.REQUIRED, example = "50.20")
    @NotNull(message = "please provide book's price") @Min(value = 1, message = "book's price should be in range from 1 to 1000.")
    private BigDecimal price;

    @Schema(description = "Book's category", requiredMode = Schema.RequiredMode.REQUIRED, example = "Java")
    @NotEmpty(message = "please provide book's category")
    @Length(max = 10, message = "book's category should be limited in 10 characters") @Pattern(regexp = "[0-9a-zA-Z\\s]{1,10}", message = "book's category only allow alphabets and numbers")
    private String category;
}
