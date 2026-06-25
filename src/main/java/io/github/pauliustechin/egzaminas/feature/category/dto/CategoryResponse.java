package io.github.pauliustechin.egzaminas.feature.category.dto;

public record CategoryResponse(
        Long categoryId,
        String categoryName,
        String description
) {
}
