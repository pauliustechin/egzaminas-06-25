package io.github.pauliustechin.egzaminas.feature.recipe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateRecipeRequest(

        @NotBlank
        String recipeName,

        @NotBlank
        String description,

        @NotNull
        List<Long> ingredients
) {
}
