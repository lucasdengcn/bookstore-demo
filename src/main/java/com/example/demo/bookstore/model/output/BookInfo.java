/* (C) 2024 */ 

package com.example.demo.bookstore.model.output;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Schema(description = "book's cover image", example = "http://image.example.com/a/1.png")
    private String coverImageUrl;
}
