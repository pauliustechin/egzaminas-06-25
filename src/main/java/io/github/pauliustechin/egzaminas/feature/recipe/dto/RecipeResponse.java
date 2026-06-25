package io.github.pauliustechin.egzaminas.feature.recipe.dto;

import java.math.BigDecimal;

public record RecipeResponse(
        Long recipeId,
        String recipeName,
        String description,
        BigDecimal rating
//        List<Ingredients> ingredients,

) {
}
