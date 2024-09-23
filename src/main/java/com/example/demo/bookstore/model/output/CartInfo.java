package com.example.demo.bookstore.model.output;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "cart summary information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartInfo {

    @Schema(description = "cart identifier for a item")
    private Integer id;

    @Schema(description = "purchasing amount for a item")
    private int amount;

    @Schema(description = "item price per unit")
    private BigDecimal price;

    @Schema(description = "total price overall")
    private BigDecimal total;

    @Schema(description = "item's information")
    private BookInfo bookInfo;

}
