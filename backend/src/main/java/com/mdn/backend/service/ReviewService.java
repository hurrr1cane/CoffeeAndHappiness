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

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final CafeRepository cafeRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;

    public CafeReview addCafeReview(Integer cafeId, Integer userId, CafeReview cafeReview) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new CafeNotFoundException("Cafe not found with id: " + cafeId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        cafeReview.setCafe(cafe);
        cafeReview.setUser(user);

        cafe.getReviews().add(cafeReview);

        cafeRepository.save(cafe);
        userRepository.save(user);

        return cafeReview;
    }

    public FoodReview addFoodReview(Integer foodId, Integer userId, FoodReview foodReview) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new FoodNotFoundException("Food not found with id: " + foodId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        foodReview.setFood(food);
        foodReview.setUser(user);

        food.getReviews().add(foodReview);

        foodRepository.save(food);
        userRepository.save(user);

        return foodReview;
    }

}
