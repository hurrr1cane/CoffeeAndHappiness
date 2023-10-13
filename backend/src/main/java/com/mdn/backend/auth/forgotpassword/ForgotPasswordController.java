package com.mdn.backend.auth.forgotpassword;

import com.mdn.backend.exception.handle.ErrorResponse;
import com.mdn.backend.exception.notfound.UserNotFoundException;
import com.mdn.backend.exception.verificationcode.VerificationCodeExpiredException;
import com.mdn.backend.exception.verificationcode.VerificationCodeMismatchException;
import com.mdn.backend.exception.verificationcode.VerificationCodeNotFoundException;
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

    @Operation(summary = "Validate verification code", description = "Validate the verification code provided by the user.")
    @PostMapping("/validate-verification-code")
    public ResponseEntity<?> validateVerificationCode(@RequestBody ResetPasswordRequest request) {
        try {
            forgotPasswordService.validateVerificationCode(request.getEmail(), request.getVerificationCode());
            return ResponseEntity.ok("Verification code validated successfully");
        } catch (UserNotFoundException e) {
            log.error("Validation failed - User not found: {}", request.getEmail());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found", "The specified user does not exist."));
        } catch (VerificationCodeMismatchException e) {
            log.error("Validation failed - Verification code mismatch");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Verification code mismatch", "The verification code provided is incorrect."));
        } catch (VerificationCodeExpiredException e) {
            log.error("Validation failed - Verification code expired");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Verification code expired", "The verification code provided has expired."));
        } catch (VerificationCodeNotFoundException e) {
            log.error("Validation failed - Verification code not found");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Verification code not found", "The verification code provided does not exist."));
        }
    }

    @Operation(summary = "Reset password", description = "Reset the user's password after verifying the code.")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            forgotPasswordService.resetPassword(request);
            return ResponseEntity.ok("Password reset successfully");
        } catch (UserNotFoundException e) {
            log.error("Reset password failed - User not found: {}", request.getEmail());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found", "The specified user does not exist."));
        }
    }
}


