package io.github.pauliustechin.egzaminas.feature.ratings.controller;

import io.github.pauliustechin.egzaminas.feature.ratings.dto.CreateRatingRequest;
import io.github.pauliustechin.egzaminas.feature.ratings.dto.RatingResponse;
import io.github.pauliustechin.egzaminas.feature.ratings.service.RatingService;
import io.github.pauliustechin.egzaminas.security.service.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @Operation(summary = "Create rating")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/recipe/{recipeId}/ratings")
    public ResponseEntity<RatingResponse> createRating(
            Authentication authentication,
            @PathVariable Long recipeId,
            @Valid @RequestBody CreateRatingRequest request) {

        UserDetailsImpl userDetails =
                (UserDetailsImpl) authentication.getPrincipal();

        Long userId = userDetails.getId();

        RatingResponse response = ratingService.createRating(userId, recipeId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Delete rating")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteRating(
            Authentication authentication,
            @PathVariable Long ratingId) {

        UserDetailsImpl userDetails =
                (UserDetailsImpl) authentication.getPrincipal();

        Long userId = userDetails.getId();

        ratingService.deleteRating(userId, ratingId);

        return ResponseEntity.noContent().build();
    }
}
