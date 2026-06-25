package io.github.pauliustechin.egzaminas.feature.recipe.repository;

import io.github.pauliustechin.egzaminas.feature.recipe.model.Recipe;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecification {

    public static Specification<Recipe> hasRecipeName(String recipeName) {
        return (root, query, criteriaBuilder) ->
                recipeName == null ? null :
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("recipeName")),
                                "%" + recipeName.toLowerCase() + "%"
                        );
    }


    public static Specification<Recipe> hasCategoryName(String categoryName) {
        return (root, query, criteriaBuilder) -> {

            if (categoryName == null) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(
                            root.join("category").get("categoryName")
                    ),
                    "%" + categoryName.toLowerCase() + "%"
            );
        };
    }
}
