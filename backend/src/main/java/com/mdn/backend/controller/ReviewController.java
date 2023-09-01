package com.mdn.backend.controller;

import com.mdn.backend.exception.CafeNotFoundException;
import com.mdn.backend.exception.FoodNotFoundException;
import com.mdn.backend.model.review.CafeReview;
import com.mdn.backend.model.review.FoodReview;
import com.mdn.backend.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/review")
@CrossOrigin(value = "*")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Add cafe review", description = "Add a review for a cafe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review added successfully"),
            @ApiResponse(responseCode = "404", description = "Cafe or user not found"),
    })
    @PostMapping("cafe/{cafeId}")
    public ResponseEntity<?> addCafeReview(
            @PathVariable Integer cafeId,
            @RequestBody CafeReview cafeReview,
            Principal principal) {
        try {
            CafeReview addedReview = reviewService.addCafeReview(cafeId, cafeReview, principal);
            return new ResponseEntity<>(addedReview, HttpStatus.CREATED);
        } catch (CafeNotFoundException ex) {
            log.error("Cafe not found with id: {}", cafeId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cafe not found with id: " + cafeId);
        }
    }

    @Operation(summary = "Add food review", description = "Add a review for a food.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review added successfully"),
            @ApiResponse(responseCode = "404", description = "Food or user not found"),
    })
    @DeleteMapping("cafe/{reviewId}")
    public ResponseEntity<?> deleteCafeReview(
            @PathVariable Integer reviewId,
            Principal principal) {
        try {
            CafeReview addedReview = reviewService.deleteCafeReview(reviewId, principal);
            return new ResponseEntity<>(addedReview, HttpStatus.CREATED);
        } catch (CafeNotFoundException ex) {
            log.error("Review not found with id: {}", reviewId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Review not found with id: " + reviewId);
        }
    }

    @Operation(summary = "Delete food review", description = "Delete a review for a food.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review or user not found"),
    })
    @DeleteMapping("food/{reviewId}")
    public ResponseEntity<?> deleteFoodReview(
            @PathVariable Integer reviewId,
            Principal principal) {
        try {
            FoodReview addedReview = reviewService.deleteFoodReview(reviewId, principal);
            return new ResponseEntity<>(addedReview, HttpStatus.CREATED);
        } catch (CafeNotFoundException ex) {
            log.error("Review not found with id: {}", reviewId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Review not found with id: " + reviewId);
        }
    }

    @Operation(summary = "Add food review", description = "Add a review for a food.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review added successfully"),
            @ApiResponse(responseCode = "404", description = "Food or user not found"),
    })
    @PostMapping("food/{foodId}")
    public ResponseEntity<?> addFoodReview(
            @PathVariable Integer foodId,
            @RequestBody FoodReview foodReview,
            Principal principal) {
        try {
            FoodReview addedReview = reviewService.addFoodReview(foodId, foodReview, principal);
            return new ResponseEntity<>(addedReview, HttpStatus.CREATED);
        } catch (FoodNotFoundException ex) {
            log.error("Food not found with id: {}", foodId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Food not found with id: " + foodId);
        }
    }
}
