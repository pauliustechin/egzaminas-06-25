package io.github.pauliustechin.egzaminas.feature.recipe.repository;

import io.github.pauliustechin.egzaminas.feature.ratings.model.Rating;
import io.github.pauliustechin.egzaminas.feature.recipe.model.Recipe;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

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

    public static Specification<Recipe> hasMinimumRating(BigDecimal minRating) {

        return (root, query, cb) -> {

            if (minRating == null) {
                return null;
            }

            Join<Recipe, Rating> ratings =
                    root.join("ratings", JoinType.LEFT);

            query.groupBy(root.get("id"));

            query.having(
                    cb.ge(
                            cb.avg(ratings.get("rating")),
                            minRating.doubleValue()
                    )
            );

            return null;
        };
    }

    public static Specification<Recipe> hasMaximumRating(BigDecimal maxRating) {

        return (root, query, cb) -> {

            if (maxRating == null) {
                return null;
            }

            Join<Recipe, Rating> ratings =
                    root.join("ratings", JoinType.LEFT);

            query.groupBy(root.get("id"));

            query.having(
                    cb.le(
                            cb.avg(ratings.get("rating")),
                            maxRating.doubleValue()
                    )
            );

            return null;
        };
    }
}
