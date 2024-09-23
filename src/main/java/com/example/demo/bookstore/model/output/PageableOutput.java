package com.example.demo.bookstore.model.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "pageable list")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableOutput<T> {

    @Schema(description = "data in the list")
    private List<T> items;

    @Schema(description = "total items per page")
    private int pageSize;

    @Schema(description = "current page no.")
    private int pageIndex;

    @Schema(description = "the number of items according to query criteria")
    private int totalItems;

    @Schema(description = "the number of pages")
    private int totalPages;

}
