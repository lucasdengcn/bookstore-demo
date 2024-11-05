/* (C) 2024 */ 

package com.example.demo.bookstore.model.output;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "cart summary information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartSummary {

    @Schema(description = "total price overall")
    private BigDecimal total;
}
