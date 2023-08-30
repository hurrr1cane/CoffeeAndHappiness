package com.mdn.backend.service;

import com.mdn.backend.exception.FoodNotFoundException;
import com.mdn.backend.model.Food;
import com.mdn.backend.model.FoodType;
import com.mdn.backend.model.review.FoodReview;
import com.mdn.backend.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public List<Food> getAllFoods() {
        for (Food food : foodRepository.findAll()) {
            fetchReviewsToFood(food);
        }
        return foodRepository.findAll();
    }

    private static void fetchReviewsToFood(Food food) {
        for (FoodReview review : food.getReviews()) {
            Integer userId = review.getUser().getId();
            Integer foodId = review.getFood().getId();
            review.setUserId(userId);
            review.setFoodId(foodId);
        }
    }

    public Food getFoodById(Integer id) {
        Food food = foodRepository.findById(id).orElseThrow(
                () -> new FoodNotFoundException("No such food with id " + id + " found")
        );
        fetchReviewsToFood(food);
        return food;
    }

    public List<Food> getFoodsByIds(List<Integer> foodIds) {
        List<Food> foods = foodRepository.findByIdIn(foodIds);

        if (foods.size() != foodIds.size()) {
            List<Integer> foundFoodIds = foods.stream()
                    .map(Food::getId)
                    .toList();

            List<Integer> notFoundFoodIds = foodIds.stream()
                    .filter(id -> !foundFoodIds.contains(id))
                    .toList();

            throw new FoodNotFoundException("Food not found with IDs: " + notFoundFoodIds);
        }

        for (Food food : foods) {
            for (FoodReview review : food.getReviews()) {
                Integer userId = review.getUser().getId();
                review.setUserId(userId);
            }
        }

        return foods;
    }

    public List<Food> getFoodByType(FoodType type) {
        return foodRepository.findByType(type);
    }

    public Food addFood(Food food) {
        try {
            return foodRepository.save(food);
        } catch (Exception ex) {
            throw new RuntimeException("Error while adding food: " + ex.getMessage(), ex);
        }
    }

    public Food editFood(Integer id, Food food) {
        var editedFood = foodRepository.findById(id).orElseThrow(
                () -> new FoodNotFoundException("No such food with id " + id + " found")
        );

        editedFood.setImageUrl(food.getImageUrl());
        editedFood.setIngredientsEN(food.getIngredientsEN());
        editedFood.setIngredientsUA(food.getIngredientsUA());
        editedFood.setNameEN(food.getNameEN());
        editedFood.setNameUA(food.getNameUA());
        editedFood.setPrice(food.getPrice());
        editedFood.setType(food.getType());
        editedFood.setWeight(food.getWeight());
        editedFood.setDescriptionEN(food.getDescriptionEN());
        editedFood.setDescriptionUA(food.getDescriptionUA());

        try {
            return foodRepository.save(editedFood);
        } catch (Exception ex) {
            throw new RuntimeException("Error while editing food: " + ex.getMessage(), ex);
        }
    }

    public void deleteFood(Integer id) {
        var foodToDelete = foodRepository.findById(id).orElseThrow(
                () -> new FoodNotFoundException("No such food with id " + id + " found")
        );
        foodRepository.delete(foodToDelete);
    }
}
