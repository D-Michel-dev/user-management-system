package com.dmicheldev.user_management.user.dtos;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Generic paginated response")
public class PagedResponse<T> {

    @Schema(description = "List of returned items")
    private List<T> content;

    @Schema(description = "Current page number (starting from 0)", example = "0")
    private int page;

    @Schema(description = "Number of items per page", example = "10")
    private int pageSize;

    @Schema(description = "Number of items in the current page", example = "5")
    private int numberOfElements;

    @Schema(description = "Total number of items", example = "100")
    private Long totalElements;

    @Schema(description = "Total number of pages", example = "10")
    private int totalPages;
}
