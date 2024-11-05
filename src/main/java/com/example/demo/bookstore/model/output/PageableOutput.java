/* (C) 2024 */ 

package com.example.demo.bookstore.model.output;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

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

    @Schema(description = "would the list reach to the tail.")
    private boolean hasNextPage;

    @Schema(description = "would the list reach to the head.")
    private boolean hasPreviousPage;

    public PageableOutput(List<T> items, Page<?> page, int pageIndex, int pageSize) {
        this.items = items;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.totalItems = (int) page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.hasNextPage = page.hasNext();
        this.hasPreviousPage = page.hasPrevious();
    }
}
