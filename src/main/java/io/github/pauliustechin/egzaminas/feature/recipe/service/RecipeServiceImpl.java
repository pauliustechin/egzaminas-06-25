package io.github.pauliustechin.egzaminas.feature.recipe.service;

import io.github.pauliustechin.egzaminas.exception.NotAllowedApiActionException;
import io.github.pauliustechin.egzaminas.exception.ResourceNotFoundException;
import io.github.pauliustechin.egzaminas.feature.category.model.Category;
import io.github.pauliustechin.egzaminas.feature.category.repository.CategoryRepository;
import io.github.pauliustechin.egzaminas.feature.ratings.model.Rating;
import io.github.pauliustechin.egzaminas.feature.ratings.repository.RatingRepository;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.CreateRecipeRequest;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.RecipeListResponse;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.RecipeMapper;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.RecipeResponse;
import io.github.pauliustechin.egzaminas.feature.recipe.model.Recipe;
import io.github.pauliustechin.egzaminas.feature.recipe.repository.RecipeRepository;
import io.github.pauliustechin.egzaminas.feature.recipe.repository.RecipeSpecification;
import io.github.pauliustechin.egzaminas.feature.user.dto.UserRole;
import io.github.pauliustechin.egzaminas.feature.user.model.User;
import io.github.pauliustechin.egzaminas.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public RecipeListResponse getAllRecipes(
            String recipeName,
            String categoryName,
            BigDecimal minRating,
            BigDecimal maxRating,
            Pageable pageable
    ) {

        Specification<Recipe> specification =
                Specification
                        .where(RecipeSpecification.hasRecipeName(recipeName))
                        .and(RecipeSpecification.hasCategoryName(categoryName))
                        .and(RecipeSpecification.hasMinimumRating(minRating))
                        .and(RecipeSpecification.hasMaximumRating(maxRating));

        Page<Recipe> pageRecipes = recipeRepository.findAll(specification, pageable);

        List<RecipeResponse> recipes = pageRecipes.stream()
                .map(recipe -> {
                    BigDecimal ratingAvg = null;
                    Set<Rating> ratings = recipe.getRatings();
                    if(ratings != null && !ratings.isEmpty()) {
                        ratingAvg = ratings.stream()
                                .map(Rating::getRating)
                                .map(BigDecimal::valueOf)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                .divide(
                                        BigDecimal.valueOf(ratings.size()),
                                        2,
                                        RoundingMode.HALF_UP
                                );
                    }
                    return recipeMapper.toResponse(recipe, ratingAvg);
                })
                .toList();

        RecipeListResponse response = new RecipeListResponse();
        response.setContent(recipes);
        response.setNumber(pageRecipes.getNumber());
        response.setSize(pageRecipes.getSize());
        response.setTotalElements(pageRecipes.getTotalElements());
        response.setTotalPages(pageRecipes.getTotalPages());
        response.setFirst(pageRecipes.isFirst());
        response.setLast(pageRecipes.isLast());

        return response;
    }

    @Override
    public RecipeResponse getRecipeById(Long recipeId) {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", recipeId));

        BigDecimal ratingAvg = null;
        Set<Rating> ratings = recipe.getRatings();
        if(ratings != null && !ratings.isEmpty()) {
            ratingAvg = ratings.stream()
                    .map(Rating::getRating)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(
                            BigDecimal.valueOf(ratings.size()),
                            2,
                            RoundingMode.HALF_UP
                    );
        }

        return recipeMapper.toResponse(recipe, ratingAvg);
    }

    @Override
    public RecipeResponse createRecipe(Long userId, Long categoryId, CreateRecipeRequest request) {

        if(userId == null) {
            throw new ResourceNotFoundException("User", userId);
        }

        if(categoryId == null) {
            throw new ResourceNotFoundException("Category", categoryId);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));

        Recipe recipe = recipeMapper.toEntity(request);
        recipe.setUser(user);
        recipe.setCategory(category);

        Recipe savedRecipe = recipeRepository.save(recipe);

        BigDecimal ratingAvg = null;
        Set<Rating> ratings = recipe.getRatings();
        if(ratings != null && !ratings.isEmpty()) {
            ratingAvg = ratings.stream()
                    .map(Rating::getRating)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(
                            BigDecimal.valueOf(ratings.size()),
                            2,
                            RoundingMode.HALF_UP
                    );
        }

        return recipeMapper.toResponse(savedRecipe, ratingAvg);
    }

    @Override
    public void deleteRecipe(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", recipeId));

        if(user.getRole().equals(UserRole.ROLE_USER) && !user.getId().equals(recipe.getUser().getId())) {
            throw new NotAllowedApiActionException("Recipe doesn't belong to current user.");
        }

        recipeRepository.delete(recipe);
    }
}
