package com.mdn.backend.auth;

import com.mdn.backend.exception.ErrorResponse;
import com.mdn.backend.exception.UserNotFoundException;
import com.mdn.backend.service.ForgotPasswordService;
import io.swagger.v3.oas.annotations.Operation;
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
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @Operation(summary = "Forgot password", description = "Send a verification code to reset the password.")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            forgotPasswordService.sendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent successfully");
        } catch (UserNotFoundException e) {
            log.error("Forgot password failed - User not found: {}", email);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found", "The specified user does not exist."));
        }
    }

}

