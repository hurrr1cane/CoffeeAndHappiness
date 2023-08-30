package com.mdn.backend.service;

import com.mdn.backend.exception.CafeNotFoundException;
import com.mdn.backend.exception.FoodNotFoundException;
import com.mdn.backend.exception.UnauthorizedAccessException;
import com.mdn.backend.exception.UserNotFoundException;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.model.Food;
import com.mdn.backend.model.review.CafeReview;
import com.mdn.backend.model.review.FoodReview;
import com.mdn.backend.model.user.User;
import com.mdn.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final CafeRepository cafeRepository;
    private final CafeReviewRepository cafeReviewRepository;
    private final FoodReviewRepository foodReviewRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;

    public CafeReview addCafeReview(Integer cafeId, CafeReview cafeReview, Principal principal) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new CafeNotFoundException("Cafe not found with id: " + cafeId));

        String userEmail = principal.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));

        double updatedRating = (cafe.getAverageRating() * cafe.getReviews().size() +
                cafeReview.getRating()) / (cafe.getReviews().size() + 1);
        updatedRating = Math.round(updatedRating * 10.0) / 10.0;

        cafeReview.setCafe(cafe);
        cafeReview.setUser(user);
        cafeReview.setUserId(user.getId());
        cafeReview.setCafeId(cafe.getId());
        cafeReview.setDate(new Date());

        cafe.getReviews().add(cafeReview);
        cafe.setAverageRating(updatedRating);
        cafe.setTotalReviews(cafe.getReviews().size());

        cafeRepository.save(cafe);
        userRepository.save(user);

        return cafeReview;
    }

    public FoodReview addFoodReview(Integer foodId, FoodReview foodReview, Principal principal) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new FoodNotFoundException("Food not found with id: " + foodId));

        String userEmail = principal.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));

        double updatedRating = (food.getAverageRating() * food.getReviews().size() + foodReview.getRating()) / (food.getReviews().size() + 1);
        updatedRating = Math.round(updatedRating * 10.0) / 10.0;

        foodReview.setUser(user);
        foodReview.setUserId(user.getId());
        foodReview.setFoodId(food.getId());
        foodReview.setDate(new Date());
        foodReview.setFood(food);

        food.getReviews().add(foodReview);
        food.setAverageRating(updatedRating);
        food.setTotalReviews(food.getReviews().size());

        foodRepository.save(food);
        userRepository.save(user);

        return foodReview;
    }

    public CafeReview deleteCafeReview(Integer reviewId, Principal principal) {
        CafeReview cafeReview = cafeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new CafeNotFoundException("Cafe review not found with id: " + reviewId));

        String userEmail = principal.getName();

        User ownerOfReview = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));


        if (cafeReview.getUser() == ownerOfReview || isAdmin()) {
            return deleteCafeReview(cafeReview);
        } else {
            throw new UnauthorizedAccessException("You are not authorized to delete this review.");
        }
    }

    private CafeReview deleteCafeReview(CafeReview cafeReview) {
        Cafe cafe = cafeReview.getCafe();
        double updatedRating = (cafe.getAverageRating() * cafe.getReviews().size() - cafeReview.getRating()) / (cafe.getReviews().size() - 1);
        updatedRating = Math.round(updatedRating * 10.0) / 10.0;

        cafe.getReviews().remove(cafeReview);
        cafe.setAverageRating(updatedRating);
        cafe.setTotalReviews(cafe.getReviews().size());

        cafeRepository.save(cafe);
        userRepository.save(cafeReview.getUser());
        cafeReviewRepository.delete(cafeReview);

        return cafeReview;
    }

    public FoodReview deleteFoodReview(Integer reviewId, Principal principal) {
        FoodReview foodReview = foodReviewRepository.findById(reviewId)
                .orElseThrow(() -> new FoodNotFoundException("Food review not found with id: " + reviewId));

        String userEmail = principal.getName();

        User ownerOfReview = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));

        if (foodReview.getUser() == ownerOfReview || isAdmin()) {
            return deleteFoodReview(foodReview);
        } else {
            throw new UnauthorizedAccessException("You are not authorized to delete this review.");
        }

    }

    private FoodReview deleteFoodReview(FoodReview foodReview) {
        Food food = foodReview.getFood();
        double updatedRating = (food.getAverageRating() * food.getReviews().size() - foodReview.getRating()) / (food.getReviews().size() - 1);
        updatedRating = Math.round(updatedRating * 10.0) / 10.0;

        food.getReviews().remove(foodReview);
        food.setAverageRating(updatedRating);
        food.setTotalReviews(food.getReviews().size());

        foodRepository.save(food);
        userRepository.save(foodReview.getUser());
        foodReviewRepository.delete(foodReview);

        return foodReview;
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
