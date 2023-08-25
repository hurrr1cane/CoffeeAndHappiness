package com.mdn.backend.service;

import com.mdn.backend.exception.FoodNotFoundException;
import com.mdn.backend.model.Food;
import com.mdn.backend.model.FoodType;
import com.mdn.backend.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public Food getFoodById(Integer id) {
        return foodRepository.findById(id).orElseThrow(
                () -> new FoodNotFoundException("No such food with id " + id + " found")
        );
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

        return foods;
    }

    public List<Food> getFoodByType(FoodType type) {
        List<Food> foodList = foodRepository.findByType(type);
        if (foodList.isEmpty()) {
            throw new FoodNotFoundException("No such food with type " + type + " found");
        }
        return foodList;
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
        editedFood.setIngredients(food.getIngredients());
        editedFood.setName(food.getName());
        editedFood.setPrice(food.getPrice());
        editedFood.setType(food.getType());
        editedFood.setWeight(food.getWeight());
        editedFood.setDescription(food.getDescription());

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
