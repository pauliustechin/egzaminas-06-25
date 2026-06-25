package io.github.pauliustechin.egzaminas.feature.ratings.service;

import io.github.pauliustechin.egzaminas.feature.ratings.dto.CreateRatingRequest;
import io.github.pauliustechin.egzaminas.feature.ratings.dto.RatingResponse;
import jakarta.validation.Valid;

public interface RatingService {
    RatingResponse createRating(Long userId, Long recipeId, CreateRatingRequest request);

    void deleteRating(Long userId, Long ratingId);
}
