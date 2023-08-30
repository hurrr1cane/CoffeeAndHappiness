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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.Provider;
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
