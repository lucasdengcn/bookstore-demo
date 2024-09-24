package com.example.demo.bookstore.model.output;

import io.swagger.v3.oas.annotations.Parameter;
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
import java.time.LocalDateTime;

@Schema(description = "book information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookInfo {

    @Schema(description = "book's unique identifier", example = "10101")
    private Integer id;

    @Schema(description = "book's title", example = "100 years")
    private String title;

    @Schema(description = "book's author", example = "John Pet")
    private String author;

    @Schema(description = "book's available amount", example = "100")
    private int amount;

    @Schema(description = "book's current price", example = "50.20")
    private BigDecimal price;

    @Schema(description = "book's category", example = "Java")
    private String category;

    @Schema(description = "book's status", example = "true or false")
    private Boolean active;

}
