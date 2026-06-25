package io.github.pauliustechin.egzaminas.feature.category.dto;

import java.util.List;

public record CategoryListResponse(
        List<CategoryResponse> content
) {
}
