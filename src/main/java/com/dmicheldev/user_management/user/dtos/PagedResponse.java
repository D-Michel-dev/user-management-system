package com.dmicheldev.user_management.user.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int pageSize;
    private int numberOfElements;
    private Long totalElements;
    private int totalPages;
}
