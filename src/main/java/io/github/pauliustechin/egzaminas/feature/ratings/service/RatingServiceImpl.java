package io.github.pauliustechin.egzaminas.feature.ratings.service;

import io.github.pauliustechin.egzaminas.exception.NotAllowedApiActionException;
import io.github.pauliustechin.egzaminas.exception.ResourceNotFoundException;
import io.github.pauliustechin.egzaminas.feature.ratings.dto.CreateRatingRequest;
import io.github.pauliustechin.egzaminas.feature.ratings.dto.RatingResponse;
import io.github.pauliustechin.egzaminas.feature.ratings.model.Rating;
import io.github.pauliustechin.egzaminas.feature.ratings.repository.RatingRepository;
import io.github.pauliustechin.egzaminas.feature.recipe.model.Recipe;
import io.github.pauliustechin.egzaminas.feature.recipe.repository.RecipeRepository;
import io.github.pauliustechin.egzaminas.feature.user.dto.UserRole;
import io.github.pauliustechin.egzaminas.feature.user.model.User;
import io.github.pauliustechin.egzaminas.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Override
    public RatingResponse createRating(Long userId, Long recipeId, CreateRatingRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", recipeId));

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setRecipe(recipe);
        rating.setRating(request.rating());
        if((request.comment() != null) && !request.comment().isBlank()) {
            rating.setComment(request.comment());
        }

        ratingRepository.save(rating);

        return new RatingResponse("Rating created successfully");
    }

    @Override
    public void deleteRating(Long userId, Long ratingId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Rating rating =  ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", ratingId));

        if(user.getRole().equals(UserRole.ROLE_USER) && !user.getId().equals(rating.getUser().getId())) {
            throw new NotAllowedApiActionException("Rating doesn't belong to current user.");
        }

        ratingRepository.delete(rating);

    }
}
