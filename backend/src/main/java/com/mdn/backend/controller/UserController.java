package com.mdn.backend.controller;

import com.mdn.backend.exception.UserNotFoundException;
import com.mdn.backend.model.user.User;
import com.mdn.backend.model.user.UserEditRequest;
import com.mdn.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/user")
@CrossOrigin(value = "*")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all available users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Getting all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by id", description = "Retrieve a user by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        log.info("Getting user with id {}", id);
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException ex) {
            log.error("User not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
        }
    }

    @Operation(summary = "Get user by email", description = "Retrieve a user by its email address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        log.info("Getting user with email {}", email);
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException ex) {
            log.error("User not found with email: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email: " + email);
        }
    }

    /*
    *     @Operation(summary = "Add food image", description = "Add an image to an existing food.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food image added"),
            @ApiResponse(responseCode = "404", description = "Food not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("{foodId}/image")
    public ResponseEntity<?> addFoodImage(@PathVariable Integer foodId, @RequestParam("image") MultipartFile image) {
        log.info("Adding image to food with id {}", foodId);

        try {
            Food food = foodService.addFoodImage(foodId, image);
            return ResponseEntity.ok(food);
        } catch (FoodNotFoundException ex) {
            log.error("Food not found with id: {}", foodId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Food not found with id: " + foodId);
        } catch (Exception ex) {
            log.error("Error while adding image to food: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while adding image to food: " + ex.getMessage());
        }
    }*/

    @PostMapping("me/edit/image")
    public ResponseEntity<?> editMyselfImage(Principal principal, @RequestParam("image") MultipartFile image) {
        log.info("Editing myself image");
        try {
            User editedUser = userService.addUserImage(principal, image);
            return ResponseEntity.ok(editedUser);
        } catch (UserNotFoundException ex) {
            log.error("User not found with email: {}", principal.getName());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email: " + principal.getName());
        } catch (Exception ex) {
            log.error("Error while editing image to user: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while editing image to user: " + ex.getMessage());
        }
    }

    @Operation(summary = "Get myself", description = "Retrieve the user that is currently logged in.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("me")
    public ResponseEntity<?> getMyself(Principal principal) {
        log.info("Getting myself");
        try {
            User user = userService.getMyself(principal);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException ex) {
            log.error("User not found with email: {}", principal.getName());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email: " + principal.getName());
        }
    }

    @Operation(summary = "Edit myself", description = "Edit the user that is currently logged in.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/me/edit")
    public ResponseEntity<?> editMyself(Principal principal, @RequestBody UserEditRequest user) {
        log.info("Editing myself");
        try {
            User editedUser = userService.editMyself(principal, user);
            return ResponseEntity.ok(editedUser);
        } catch (UserNotFoundException ex) {
            log.error("User not found with email: {}", principal.getName());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email: " + principal.getName());
        }
    }
}
