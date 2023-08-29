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
            @ApiResponse(responseCode = "500", description = "Internal server error")
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
        } catch (Exception ex) {
            log.error("Error while adding cafe review: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while adding cafe review: " + ex.getMessage());
        }
    }

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
        } catch (Exception ex) {
            log.error("Error while adding cafe review: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while adding cafe review: " + ex.getMessage());
        }
    }

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
        } catch (Exception ex) {
            log.error("Error while adding cafe review: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while adding cafe review: " + ex.getMessage());
        }
    }

    @Operation(summary = "Add food review", description = "Add a review for a food.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review added successfully"),
            @ApiResponse(responseCode = "404", description = "Food or user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
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
        } catch (Exception ex) {
            log.error("Error while adding food review: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while adding food review: " + ex.getMessage());
        }
    }
}
