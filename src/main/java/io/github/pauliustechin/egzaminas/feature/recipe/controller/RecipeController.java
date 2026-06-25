package io.github.pauliustechin.egzaminas.feature.recipe.controller;

import io.github.pauliustechin.egzaminas.feature.recipe.dto.CreateRecipeRequest;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.RecipeListResponse;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.RecipeResponse;
import io.github.pauliustechin.egzaminas.feature.recipe.service.RecipeService;
import io.github.pauliustechin.egzaminas.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/recipes")
    public ResponseEntity<RecipeListResponse> getAllRecipes(
            @RequestParam(required = false) String recipeName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(required = false) BigDecimal maxRating,
            @ParameterObject @PageableDefault(page = 0, size = 10, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        RecipeListResponse response = recipeService.getAllRecipes(recipeName, categoryName, minRating, maxRating, pageable);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Get recipe by id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/recipes/{recipeId}")
    public ResponseEntity<RecipeResponse> getRecipeById(
            @PathVariable Long recipeId) {

        RecipeResponse response = recipeService.getRecipeById(recipeId);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Create recipe")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/category/{categoryId}/recipes")
    public ResponseEntity<RecipeResponse> createRecipe(
            Authentication authentication,
            @Valid @RequestBody CreateRecipeRequest request,
            @PathVariable Long categoryId) {

        UserDetailsImpl userDetails =
                (UserDetailsImpl) authentication.getPrincipal();

        Long userId = userDetails.getId();

        RecipeResponse response = recipeService.createRecipe(userId, categoryId, request);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Delete recipe")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/recipes/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(
            Authentication authentication,
            @PathVariable Long recipeId) {

        UserDetailsImpl userDetails =
                (UserDetailsImpl) authentication.getPrincipal();

        Long userId = userDetails.getId();

        recipeService.deleteRecipe(userId, recipeId);

        return ResponseEntity.noContent().build();
    }
}