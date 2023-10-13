package com.mdn.backend.controller;

import com.mdn.backend.exception.notfound.FoodNotFoundException;
import com.mdn.backend.model.Food;
import com.mdn.backend.model.FoodType;
import com.mdn.backend.model.dto.FoodDTO;
import com.mdn.backend.service.FoodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class FoodControllerTest {

    @Mock
    private FoodService foodService;

    @InjectMocks
    private FoodController foodController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllFoods() {
        List<Food> foods = new ArrayList<>();

        foods.add(new Food());
        when(foodService.getAllFoods()).thenReturn(foods);

        ResponseEntity<?> response = foodController.getAllFoods();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(foods, response.getBody());
    }

    @Test
    void testGetFoodById() {
        Integer foodId = 1;
        Food food = new Food();
        food.setId(foodId);
        when(foodService.getFoodById(foodId)).thenReturn(food);

        ResponseEntity<?> response = foodController.getFoodById(foodId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(food, response.getBody());
    }

    @Test
    void testGetFoodByIdNotFound() {
        Integer foodId = 3;
        when(foodService.getFoodById(foodId)).thenThrow(
                new FoodNotFoundException("Food not found with id: " + foodId)
        );

        ResponseEntity<?> response = foodController.getFoodById(foodId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Food not found with id: " + foodId, response.getBody());
    }

    @Test
    void testGetFoodByType() {
        String foodType = "DRINK";
        Food food = new Food();
        food.setType(FoodType.DRINK);
        List<Food> foods = new ArrayList<>();
        foods.add(food);
        when(foodService.getFoodByType(FoodType.DRINK)).thenReturn(foods);

        ResponseEntity<?> response = foodController.getFoodByType(foodType);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(foods, response.getBody());
    }

    @Test
    void testGetFoodByTypeInvalid() {
        String foodType = "INVALID";
        when(foodService.getFoodByType(FoodType.DRINK)).thenThrow(
                new IllegalArgumentException("Invalid food type: " + foodType)
        );

        ResponseEntity<?> response = foodController.getFoodByType(foodType);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid food type: " + foodType, response.getBody());
    }

    @Test
    void testAddFood() {
        FoodDTO foodDTO = new FoodDTO();
        Food food = new Food();
        when(foodService.addFood(foodDTO)).thenReturn(food);

        ResponseEntity<?> response = foodController.addFood(foodDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(food, response.getBody());
    }

    @Test
    void testAddFoodImage() {
        Integer foodId = 1;
        Food food = new Food();
        food.setId(foodId);

        when(foodService.addFoodImage(foodId, null)).thenReturn(food);

        ResponseEntity<?> response = foodController.addFoodImage(foodId, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(food, response.getBody());
    }

    @Test
    void testAddFoodImageNotFound() {
        Integer foodId = 3;
        when(foodService.addFoodImage(foodId, null)).thenThrow(
                new FoodNotFoundException("Food not found with id: " + foodId)
        );

        ResponseEntity<?> response = foodController.addFoodImage(foodId, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Food not found with id: " + foodId, response.getBody());
    }

    @Test
    void testDeleteFoodImage() {
        Integer foodId = 1;
        Food food = new Food();
        food.setId(foodId);
        when(foodService.deleteFoodImage(foodId)).thenReturn(food);

        ResponseEntity<?> response = foodController.deleteFoodImage(foodId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(food, response.getBody());
    }

    @Test
    void testDeleteFoodImageNotFound() {
        Integer foodId = 3;
        when(foodService.deleteFoodImage(foodId)).thenThrow(
                new FoodNotFoundException("Food not found with id: " + foodId)
        );

        ResponseEntity<?> response = foodController.deleteFoodImage(foodId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Food not found with id: " + foodId, response.getBody());
    }

    @Test
    void testEditFood() {
        Integer foodId = 1;
        FoodDTO foodDTO = new FoodDTO();
        Food food = new Food();
        food.setId(foodId);
        when(foodService.editFood(foodId, foodDTO)).thenReturn(food);

        ResponseEntity<?> response = foodController.editFood(foodId, foodDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(food, response.getBody());
    }

    @Test
    void testEditFoodNotFound() {
        Integer foodId = 3;
        FoodDTO foodDTO = new FoodDTO();
        when(foodService.editFood(foodId, foodDTO)).thenThrow(
                new FoodNotFoundException("Food not found with id: " + foodId)
        );

        ResponseEntity<?> response = foodController.editFood(foodId, foodDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Food not found with id: " + foodId, response.getBody());
    }

    @Test
    void testDeleteFood() {
        Integer foodId = 1;
        Food food = new Food();
        food.setId(foodId);

        ResponseEntity<?> response = foodController.deleteFood(foodId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteFoodNotFound() {
        Integer foodId = 3;

        doThrow(new FoodNotFoundException("Food not found with id: " + foodId))
                .when(foodService).deleteFood(foodId);

        ResponseEntity<?> response = foodController.deleteFood(foodId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Food not found with id: " + foodId, response.getBody());
    }

}