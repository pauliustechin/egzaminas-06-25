package io.github.pauliustechin.egzaminas.feature.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(

        @NotBlank
        String categoryName,

        @NotBlank
        String description
) {
}
