package com.example.demo.bookstore.model.output;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class CartSummary {

    @Schema(description = "total price overall")
    private BigDecimal total;
}
