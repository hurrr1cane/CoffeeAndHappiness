package com.mdn.backend.controller;

import com.mdn.backend.exception.FoodNotFoundException;
import com.mdn.backend.model.Food;
import com.mdn.backend.model.FoodType;
import com.mdn.backend.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/food")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
@Slf4j
public class FoodController {

    private final FoodService foodService;

    @Operation(summary = "Get all foods", description = "Retrieve a list of all available foods.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Food>> getAllFoods() {
        log.info("Getting all foods");
        return ResponseEntity.ok(foodService.getAllFoods());
    }

    @Operation(summary = "Get food by id", description = "Retrieve a food by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Food not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getFoodById(@PathVariable Integer id) {
        log.info("Getting food with id {}", id);
        try {
            Food food = foodService.getFoodById(id);
            return ResponseEntity.ok(food);
        } catch (FoodNotFoundException ex) {
            log.error("Food not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food not found with id: " + id);
        }
    }

    @Operation(summary = "Get food by type", description = "Retrieve a food by its type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Invalid food type"),
    })
    @GetMapping("type/{type}")
    public ResponseEntity<?> getFoodByType(@PathVariable String type) {
        log.info("Getting food with type {}", type);
        try {
            FoodType foodType = FoodType.valueOf(type.toUpperCase());
            List<Food> foodList = foodService.getFoodByType(foodType);
            return ResponseEntity.ok(foodList);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid food type: {}", type);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid food type: " + type);
        }
    }

    @Operation(summary = "Add new food", description = "Add a new food.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Food created"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<?> addFood(@RequestBody @Valid Food food) {
        log.info("Adding new food");

        Food addedFood = foodService.addFood(food);
        return new ResponseEntity<>(addedFood, HttpStatus.CREATED);
    }

    @Operation(summary = "Add food image", description = "Add an image to an existing food.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food image added"),
            @ApiResponse(responseCode = "404", description = "Food not found"),
    })
    @PostMapping("{foodId}/image/add")
    public ResponseEntity<?> addFoodImage(@PathVariable Integer foodId, @RequestParam("image") MultipartFile image) {
        log.info("Adding image to food with id {}", foodId);

        try {
            Food food = foodService.addFoodImage(foodId, image);
            return ResponseEntity.ok(food);
        } catch (FoodNotFoundException ex) {
            log.error("Food not found with id: {}", foodId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Food not found with id: " + foodId);
        }
    }

    @Operation(summary = "Delete food", description = "Delete an existing food.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food deleted"),
            @ApiResponse(responseCode = "404", description = "Food not found"),
    })
    @DeleteMapping("{foodId}/image/delete")
    public ResponseEntity<?> deleteFoodImage(@PathVariable Integer foodId) {
        log.info("Deleting image from food with id {}", foodId);

        try {
            Food food = foodService.deleteFoodImage(foodId);
            return ResponseEntity.ok(food);
        } catch (FoodNotFoundException ex) {
            log.error("Food not found with id: {}", foodId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Food not found with id: " + foodId);
        }
    }

    @Operation(summary = "Edit food", description = "Edit an existing food.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food edited"),
            @ApiResponse(responseCode = "404", description = "Food not found"),
    })
    @PutMapping("{id}")
    public ResponseEntity<?> editFood(@PathVariable Integer id, @RequestBody @Valid Food food) {
        log.info("Editing food with id {}", id);

        try {
            Food editedFood = foodService.editFood(id, food);
            return ResponseEntity.ok(editedFood);
        } catch (FoodNotFoundException ex) {
            log.error("Food not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Food not found with id: " + id);
        }
    }

    @Operation(summary = "Delete food", description = "Delete an existing food.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Food deleted"),
            @ApiResponse(responseCode = "404", description = "Food not found"),
    })
    @DeleteMapping({"{id}"})
    public ResponseEntity<?> deleteFood(@PathVariable Integer id) {
        log.info("Deleting food with id {}", id);
        try {
            foodService.deleteFood(id);
            return ResponseEntity.noContent().build();
        } catch (FoodNotFoundException ex) {
            log.error("Food not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Food not found with id: " + id);
        }
    }
}
