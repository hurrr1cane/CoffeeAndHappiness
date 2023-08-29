package com.mdn.backend.auth;

import com.mdn.backend.exception.UserAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user", description = "Register a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        log.info("Received registration request for user: {}", request.getEmail());
        try {
            AuthenticationResponse response = authenticationService.registerUser(request);
            log.info("User registered successfully: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            log.error("Registration failed - User already exists: {}", request.getEmail());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Operation(summary = "Register a new admin", description = "Register a new admin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register/admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
        log.info("Received registration request for admin: {}", request.getEmail());
        try {
            AuthenticationResponse response = authenticationService.registerAdmin(request);
            log.info("Admin registered successfully: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            log.error("Registration failed - Admin already exists: {}", request.getEmail());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Operation(summary = "Login", description = "Login.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        log.info("Received login request for user: {}", request.getEmail());
        try {
            AuthenticationResponse response = authenticationService.login(request);
            log.info("User logged in successfully: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed - User not found: {}", request.getEmail());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Operation(summary = "Refresh token", description = "Refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        log.info("Received refresh token request");
        try {
            authenticationService.refreshToken(request, response);
            log.info("Token refreshed successfully");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Refresh token failed");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

}
