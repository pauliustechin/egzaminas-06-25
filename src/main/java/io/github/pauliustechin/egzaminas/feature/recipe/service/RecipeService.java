package io.github.pauliustechin.egzaminas.feature.recipe.service;

import io.github.pauliustechin.egzaminas.feature.recipe.dto.CreateRecipeRequest;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.RecipeListResponse;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.RecipeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface RecipeService {
    RecipeListResponse getAllRecipes(String recipeName, String categoryName, Pageable pageable);

    RecipeResponse getRecipeById(Long recipeId);

    RecipeResponse createRecipe(Long userId, Long categoryId, CreateRecipeRequest request);

    void deleteRecipe(Long userId, Long recipeId);
}
