package com.example.JustGetStartedBackEnd.API.Common.DTO;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PagingResponseDTO<T> (
    List<T> content,
    int pageNo,
    int pageSize,
    long totalElements,
    int totalPages,
    boolean last
) {
    public static <T> PagingResponseDTO<T> of(Page<T> page, List<T> list) {
        return PagingResponseDTO.<T>builder()
                .content(list)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}