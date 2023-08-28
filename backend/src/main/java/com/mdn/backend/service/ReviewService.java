package com.mdn.backend.service;

import com.mdn.backend.exception.CafeNotFoundException;
import com.mdn.backend.exception.FoodNotFoundException;
import com.mdn.backend.exception.UserNotFoundException;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.model.Food;
import com.mdn.backend.model.review.CafeReview;
import com.mdn.backend.model.review.FoodReview;
import com.mdn.backend.model.user.User;
import com.mdn.backend.repository.CafeRepository;
import com.mdn.backend.repository.FoodRepository;
import com.mdn.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final CafeRepository cafeRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;

    public CafeReview addCafeReview(Integer cafeId, CafeReview cafeReview, Principal principal) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new CafeNotFoundException("Cafe not found with id: " + cafeId));

        String userEmail = principal.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));

        double updatedRating = (cafe.getAverageRating() * cafe.getReviews().size() + cafeReview.getRating()) / (cafe.getReviews().size() + 1);
        updatedRating = Math.round(updatedRating * 10.0) / 10.0;

        cafeReview.setCafe(cafe);
        cafeReview.setUser(user);
        cafeReview.setUserId(user.getId());
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
        foodReview.setDate(new Date());
        foodReview.setFood(food);

        food.getReviews().add(foodReview);
        food.setAverageRating(updatedRating);
        food.setTotalReviews(food.getReviews().size());

        foodRepository.save(food);
        userRepository.save(user);

        return foodReview;
    }

}
