package io.github.pauliustechin.egzaminas.feature.recipe.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RecipeResponse(
        Long recipeId,
        String recipeName,
        String description,
        BigDecimal rating
//        List<Ingredients> ingredients,

) {
}
