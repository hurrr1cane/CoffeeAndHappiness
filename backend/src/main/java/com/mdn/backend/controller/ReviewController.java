package com.mdn.backend.controller;

import com.mdn.backend.exception.CafeNotFoundException;
import com.mdn.backend.exception.FoodNotFoundException;
import com.mdn.backend.exception.UserNotFoundException;
import com.mdn.backend.model.review.CafeReview;
import com.mdn.backend.model.review.FoodReview;
import com.mdn.backend.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("cafe/{cafeId}/{userId}")
    public ResponseEntity<?> addCafeReview(
            @PathVariable Integer cafeId,
            @PathVariable Integer userId,
            @RequestBody CafeReview cafeReview) {
        try {
            CafeReview addedReview = reviewService.addCafeReview(cafeId, userId, cafeReview);
            return new ResponseEntity<>(addedReview, HttpStatus.CREATED);
        } catch (CafeNotFoundException ex) {
            log.error("Cafe not found with id: {}", cafeId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cafe not found with id: " + cafeId);
        } catch (UserNotFoundException ex) {
            log.error("User not found with id: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with id: " + userId);
        } catch (Exception ex) {
            log.error("Error while adding cafe review: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while adding cafe review: " + ex.getMessage());
        }
    }

    @PostMapping("food/{foodId}/{userId}")
    public ResponseEntity<?> addFoodReview(
            @PathVariable Integer foodId,
            @PathVariable Integer userId,
            @RequestBody FoodReview foodReview) {
        try {
            FoodReview addedReview = reviewService.addFoodReview(foodId, userId, foodReview);
            return new ResponseEntity<>(addedReview, HttpStatus.CREATED);
        } catch (FoodNotFoundException ex) {
            log.error("Food not found with id: {}", foodId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Food not found with id: " + foodId);
        } catch (UserNotFoundException ex) {
            log.error("User not found with id: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with id: " + userId);
        } catch (Exception ex) {
            log.error("Error while adding food review: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while adding food review: " + ex.getMessage());
        }
    }
}
