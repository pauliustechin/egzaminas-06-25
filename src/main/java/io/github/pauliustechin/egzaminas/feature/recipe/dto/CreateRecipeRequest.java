package io.github.pauliustechin.egzaminas.feature.recipe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateRecipeRequest(

        @NotBlank
        String recipeName,

        @NotBlank
        String description,

        @NotNull
        List<Long> ingredients
) {
}
