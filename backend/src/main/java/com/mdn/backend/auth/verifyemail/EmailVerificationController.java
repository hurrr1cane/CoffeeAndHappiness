package com.mdn.backend.auth.verifyemail;

import com.mdn.backend.exception.ErrorResponse;
import com.mdn.backend.exception.UserNotFoundException;
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
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @Operation(summary = "Validate email verification code", description = "Validate the email verification code provided by the user.")
    @PostMapping("/validate-email-verification")
    public ResponseEntity<?> validateEmailVerification(@RequestBody EmailVerificationRequest request) {
        try {
            emailVerificationService.validateEmailVerification(request.getEmail(), request.getVerificationCode());
            return ResponseEntity.ok("Email verification code validated successfully");
        } catch (UserNotFoundException e) {
            log.error("Validation failed - User not found: {}", request.getEmail());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found", "The specified user does not exist."));
        } catch (VerificationCodeMismatchException e) {
            log.error("Validation failed - Email verification code mismatch");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Email verification code mismatch", "The email verification code provided is incorrect."));
        } catch (VerificationCodeExpiredException e) {
            log.error("Validation failed - Email verification code expired");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Email verification code expired", "The email verification code provided has expired."));
        } catch (VerificationCodeNotFoundException e) {
            log.error("Validation failed - Email verification code not found");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Email verification code not found", "The email verification code provided does not exist."));
        }
    }
}

