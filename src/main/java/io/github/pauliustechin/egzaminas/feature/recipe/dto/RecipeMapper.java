package io.github.pauliustechin.egzaminas.feature.recipe.dto;

import io.github.pauliustechin.egzaminas.feature.recipe.model.Recipe;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper {

    public Recipe toEntity(CreateRecipeRequest request) {

        return Recipe.builder()
                .recipeName(request.recipeName())
                .description(request.description())
                .build();
    }


    public RecipeResponse toResponse(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getRecipeName(),
                recipe.getDescription()
        );
    }
}
