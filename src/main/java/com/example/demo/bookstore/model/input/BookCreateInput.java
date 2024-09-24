package com.example.demo.bookstore.model.input;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springdoc.core.annotations.ParameterObject;

import java.math.BigDecimal;

@ParameterObject
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateInput {

    @Parameter(description = "Book's title", required = true, example = "Practice Java")
    @NotEmpty(message = "please provide book's title")
    @Length(max = 100, message = "book's title should be limited in 100 characters")
    @Pattern(regexp = "[0-9a-zA-Z\\s]{1,100}", message = "book's title only allow alphabets and numbers")
    private String title;

    @Parameter(description = "Book's author", required = true, example = "John Mark")
    @NotEmpty(message = "please provide book's author")
    @Length(max = 20, message = "book's author should be limited in 20 characters")
    @Pattern(regexp = "[0-9a-zA-Z\\s]{1,20}", message = "book's author only allow alphabets and numbers")
    private String author;

    @Parameter(description = "Book's amount", required = true, example = "100")
    @Max(value = 1000, message = "book's amount up to 1000.")
    @Min(value = 1, message = "book's amount should be in range from 1 to 1000.")
    private int amount;

    @Parameter(description = "Book's price", required = true, example = "50.20")
    @NotNull(message = "please provide book's price")
    @Min(value = 1, message = "book's price should be in range from 1 to 1000.")
    private BigDecimal price;

    @Parameter(description = "Book's category", required = true, example = "Java")
    @NotEmpty(message = "please provide book's category")
    @Length(max = 10, message = "book's category should be limited in 10 characters")
    @Pattern(regexp = "[0-9a-zA-Z\\s]{1,10}", message = "book's category only allow alphabets and numbers")
    private String category;

}
