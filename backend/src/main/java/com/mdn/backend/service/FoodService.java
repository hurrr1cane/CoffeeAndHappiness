package com.mdn.backend.service;

import com.mdn.backend.exception.notfound.FoodNotFoundException;
import com.mdn.backend.model.Food;
import com.mdn.backend.model.FoodType;
import com.mdn.backend.model.dto.FoodDTO;
import com.mdn.backend.model.review.FoodReview;
import com.mdn.backend.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final AzureBlobStorageService azureStorageService;

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

        var foods = new ArrayList<Food>();

        for (Integer foodId : foodIds) {
            Food food = foodRepository.findById(foodId).orElseThrow(
                    () -> new FoodNotFoundException("No such food with id " + foodId + " found")
            );
            fetchReviewsToFood(food);
            foods.add(food);
        }

        return foods;
    }

    public List<Food> getFoodByType(FoodType type) {
        return foodRepository.findByType(type);
    }

    public Food addFood(FoodDTO foodDTO) {
        try {
            Food food = buildFoodFromDTO(foodDTO);
            return foodRepository.save(food);
        } catch (Exception ex) {
            throw new RuntimeException("Error while adding food: " + ex.getMessage(), ex);
        }
    }

    public Food editFood(Integer id, FoodDTO foodDTO) {
        var editedFood = foodRepository.findById(id).orElseThrow(
                () -> new FoodNotFoundException("No such food with id " + id + " found")
        );

        try {
            editFoodWithCheckingForNull(foodDTO, editedFood);
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

    public Food addFoodImage(Integer foodId, MultipartFile image) {

        Food food = foodRepository.findById(foodId).orElseThrow(
                () -> new FoodNotFoundException("No such food with id " + foodId + " found")
        );

        azureStorageService.deleteImage("food", foodId);
        String imageUrl = azureStorageService.saveImage(image, "food", foodId);

        food.setImageUrl(imageUrl);
        return foodRepository.save(food);

    }

    public Food deleteFoodImage(Integer foodId) {

        Food food = foodRepository.findById(foodId).orElseThrow(
                () -> new FoodNotFoundException("No such food with id " + foodId + " found")
        );

        azureStorageService.deleteImage("food", foodId);

        food.setImageUrl(null);
        return foodRepository.save(food);
    }

    private static void editFoodWithCheckingForNull(FoodDTO foodDTO, Food editedFood) {
        if (foodDTO.getIngredientsEN() != null) editedFood.setIngredientsEN(foodDTO.getIngredientsEN());
        if (foodDTO.getIngredientsUA() != null) editedFood.setIngredientsUA(foodDTO.getIngredientsUA());
        if (foodDTO.getImageUrl() != null) editedFood.setImageUrl(foodDTO.getImageUrl());
        if (foodDTO.getNameEN() != null) editedFood.setNameEN(foodDTO.getNameEN());
        if (foodDTO.getNameUA() != null) editedFood.setNameUA(foodDTO.getNameUA());
        if (foodDTO.getDescriptionEN() != null) editedFood.setDescriptionEN(foodDTO.getDescriptionEN());
        if (foodDTO.getDescriptionUA() != null) editedFood.setDescriptionUA(foodDTO.getDescriptionUA());
        if (foodDTO.getIngredientsEN() != null) editedFood.setIngredientsEN(foodDTO.getIngredientsEN());
        if (foodDTO.getIngredientsUA() != null) editedFood.setIngredientsUA(foodDTO.getIngredientsUA());
        if (foodDTO.getPrice() != null) editedFood.setPrice(foodDTO.getPrice());
        if (foodDTO.getWeight() != null) editedFood.setWeight(foodDTO.getWeight());
        if (foodDTO.getType() != null) editedFood.setType(foodDTO.getType());
    }

    private static Food buildFoodFromDTO(FoodDTO foodDTO) {
        return Food.builder()
                .nameEN(foodDTO.getNameEN())
                .nameUA(foodDTO.getNameUA())
                .descriptionEN(foodDTO.getDescriptionEN())
                .descriptionUA(foodDTO.getDescriptionUA())
                .ingredientsEN(foodDTO.getIngredientsEN())
                .ingredientsUA(foodDTO.getIngredientsUA())
                .price(foodDTO.getPrice())
                .weight(foodDTO.getWeight())
                .type(foodDTO.getType())
                .build();
    }
}
