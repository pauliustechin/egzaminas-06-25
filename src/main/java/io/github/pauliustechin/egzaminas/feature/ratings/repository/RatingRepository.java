package io.github.pauliustechin.egzaminas.feature.ratings.repository;

import io.github.pauliustechin.egzaminas.feature.ratings.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findAllByRecipeId(Long recipeId);
}
