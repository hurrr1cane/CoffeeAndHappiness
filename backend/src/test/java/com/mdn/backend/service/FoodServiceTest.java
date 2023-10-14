package com.mdn.backend.service;

import com.mdn.backend.exception.notfound.FoodNotFoundException;
import com.mdn.backend.model.Food;
import com.mdn.backend.model.FoodType;
import com.mdn.backend.model.dto.FoodDTO;
import com.mdn.backend.model.review.FoodReview;
import com.mdn.backend.model.user.User;
import com.mdn.backend.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FoodServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private FoodService foodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllFoods() {
        // Mock data
        List<Food> foods = new ArrayList<>();
        foods.add(new Food());
        foods.add(new Food());

        // Mock behavior
        when(foodRepository.findAll()).thenReturn(foods);

        // Test
        List<Food> result = foodService.getAllFoods();

        assertEquals(foods.size(), result.size());
    }

    @Test
    void testGetFoodById() {
        // Mock data
        Food food = new Food();
        food.setId(1);

        // Mock behavior
        when(foodRepository.findById(1)).thenReturn(Optional.of(food));

        // Test
        Food result = foodService.getFoodById(1);

        assertEquals(food, result);
    }

    @Test
    void testGetFoodByIdNotFound() {
        // Mock behavior
        when(foodRepository.findById(1)).thenReturn(Optional.empty());

        // Test
        assertThrows(FoodNotFoundException.class, () -> foodService.getFoodById(1));
    }

    @Test
    void testGetFoodsByIds() {
        // Mock data
        List<Integer> foodIds = List.of(1, 2, 3);

        Food food1 = new Food();
        food1.setId(1);
        Food food2 = new Food();
        food2.setId(2);
        Food food3 = new Food();
        food3.setId(3);

        // Mock behavior
        when(foodRepository.findById(1)).thenReturn(Optional.of(food1));
        when(foodRepository.findById(2)).thenReturn(Optional.of(food2));
        when(foodRepository.findById(3)).thenReturn(Optional.of(food3));

        // Test
        List<Food> result = foodService.getFoodsByIds(foodIds);

        assertEquals(foodIds.size(), result.size());
    }

    @Test
    void testGetFoodsByIdsNotFound() {
        // Mock data
        List<Integer> foodIds = List.of(1, 2, 3);

        // Mock behavior
        when(foodRepository.findById(1)).thenReturn(Optional.empty());
        when(foodRepository.findById(2)).thenReturn(Optional.empty());
        when(foodRepository.findById(3)).thenReturn(Optional.empty());

        // Test
        assertThrows(FoodNotFoundException.class, () -> foodService.getFoodsByIds(foodIds));
    }

    @Test
    void testGetFoodByType() {
        // Mock data
        FoodType foodType = FoodType.MAIN;
        List<Food> foods = new ArrayList<>();
        foods.add(new Food());
        foods.add(new Food());

        // Mock behavior
        when(foodRepository.findByType(foodType)).thenReturn(foods);

        // Test
        List<Food> result = foodService.getFoodByType(foodType);

        assertEquals(foods.size(), result.size());
    }

    @Test
    void testFetchReviewsToFoodWithNullReviews() {
        // Arrange
        Food food = new Food();
        food.setReviews(null);

        // Act
        FoodService.fetchReviewsToFood(food);

        // Assert
        assertNull(food.getReviews());
    }

    @Test
    void testFetchReviewsToFoodWithReviews() {
        // Arrange
        FoodReview review = new FoodReview();
        review.setUser(new User());
        review.setFood(new Food());

        Food food = new Food();
        food.setReviews(Collections.singletonList(review));

        // Act
        FoodService.fetchReviewsToFood(food);

        // Assert
        assertNotNull(food.getReviews());
        assertEquals(1, food.getReviews().size());
        assertNotNull(food.getReviews().get(0).getUser());
        assertNotNull(food.getReviews().get(0).getFood());
    }

    @Test
    void testAddFood() {
        // Mock data
        FoodDTO foodDTO = new FoodDTO();
        foodDTO.setNameEN("Test Food");
        foodDTO.setNameUA("Тест Їжа");
        foodDTO.setPrice(10.0);
        foodDTO.setType(FoodType.MAIN);

        Food food = new Food();
        food.setNameEN("Test Food");
        food.setNameUA("Тест Їжа");
        food.setPrice(10.0);
        food.setType(FoodType.MAIN);

        // Mock behavior
        when(foodRepository.save(any(Food.class))).thenReturn(food);

        // Test
        Food result = foodService.addFood(foodDTO);

        assertEquals(food, result);
    }

    @Test
    void testEditFood() {
        // Mock data
        FoodDTO foodDTO = new FoodDTO();
        foodDTO.setNameEN("Updated Food");

        Food existingFood = new Food();
        existingFood.setId(1);
        existingFood.setNameEN("Original Food");

        // Mock behavior
        when(foodRepository.findById(1)).thenReturn(Optional.of(existingFood));
        when(foodRepository.save(any(Food.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test
        Food result = foodService.editFood(1, foodDTO);

        assertEquals("Updated Food", result.getNameEN());
    }

    @Test
    void testEditFoodWithCheckingForNull() {
        // Arrange
        int foodId = 1;
        FoodDTO foodDTO = new FoodDTO();
        Food editedFood = new Food();
        editedFood.setId(foodId);

        foodDTO.setDescriptionEN("New Description EN");
        foodDTO.setDescriptionUA("New Description UA");
        foodDTO.setImageUrl("https://new_image_url");
        foodDTO.setIngredientsEN("New Ingredients EN");
        foodDTO.setIngredientsUA("New Ingredients UA");
        foodDTO.setPrice(15.0);
        foodDTO.setWeight(200.0);
        foodDTO.setType(FoodType.DESSERT);

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(editedFood));

        // Act
        foodService.editFood(foodId, foodDTO);

        // Assert
        assertEquals("New Description EN", editedFood.getDescriptionEN());
        assertEquals("New Description UA", editedFood.getDescriptionUA());
        assertEquals("https://new_image_url", editedFood.getImageUrl());
        assertEquals("New Ingredients EN", editedFood.getIngredientsEN());
        assertEquals("New Ingredients UA", editedFood.getIngredientsUA());
        assertEquals(15.0, editedFood.getPrice());
        assertEquals(200.0, editedFood.getWeight());
        assertEquals(FoodType.DESSERT, editedFood.getType());
    }

    @Test
    void testEditFoodWithCheckingForNullWhenNullValues() {
        // Arrange
        int foodId = 1;
        Food editedFood = new Food();

        editedFood.setNameEN("Original Food");
        editedFood.setNameUA("Оригінальна Їжа");
        editedFood.setDescriptionEN("Original Description EN");
        editedFood.setDescriptionUA("Original Description UA");
        editedFood.setIngredientsEN("Original Ingredients EN");
        editedFood.setIngredientsUA("Original Ingredients UA");
        editedFood.setPrice(20.0);
        editedFood.setWeight(300.0);
        editedFood.setType(FoodType.MAIN);

        FoodDTO foodDTO = new FoodDTO();
        editedFood.setId(foodId);

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(editedFood));

        // Act
        foodService.editFood(foodId, foodDTO);

        // Assert
        assertEquals("Original Food", editedFood.getNameEN());
        assertEquals("Оригінальна Їжа", editedFood.getNameUA());
        assertEquals("Original Description EN", editedFood.getDescriptionEN());
        assertEquals("Original Description UA", editedFood.getDescriptionUA());
        assertEquals("Original Ingredients EN", editedFood.getIngredientsEN());
        assertEquals("Original Ingredients UA", editedFood.getIngredientsUA());
        assertEquals(20.0, editedFood.getPrice());
        assertEquals(300.0, editedFood.getWeight());
        assertEquals(FoodType.MAIN, editedFood.getType());
    }

    @Test
    void testEditFoodFoodNotFound() {
        // Arrange
        int foodId = 1;
        FoodDTO foodDTO = new FoodDTO();

        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(FoodNotFoundException.class, () -> foodService.editFood(foodId, foodDTO));
    }

    @Test
    void testDeleteFood() {
        // Mock behavior
        when(foodRepository.findById(1)).thenReturn(Optional.of(new Food()));

        // Test
        assertDoesNotThrow(() -> foodService.deleteFood(1));
    }

    @Test
    void testDeleteFoodNotFound() {
        // Mock behavior
        when(foodRepository.findById(1)).thenReturn(Optional.empty());

        // Test
        assertThrows(FoodNotFoundException.class, () -> foodService.deleteFood(1));
    }
}
