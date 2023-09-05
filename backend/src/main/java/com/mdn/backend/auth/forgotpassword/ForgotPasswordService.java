package com.mdn.backend.auth.forgotpassword;

import com.mdn.backend.exception.UserNotFoundException;
import com.mdn.backend.exception.verificationcode.VerificationCodeExpiredException;
import com.mdn.backend.exception.verificationcode.VerificationCodeMismatchException;
import com.mdn.backend.exception.verificationcode.VerificationCodeNotFoundException;
import com.mdn.backend.model.user.User;
import com.mdn.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;

    public void sendVerificationCode(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        var expirationTime = LocalDateTime.now().plusMinutes(3);

        var verificationCode = VerificationCode.builder()
                .code(generateRandomCode())
                .expirationTime(expirationTime)
                .user(user)
                .build();

        emailService.sendVerificationCode(user.getEmail(), verificationCode.getCode());

        verificationCodeRepository.save(verificationCode);
    }

    private String generateRandomCode() {

        Random random = new Random();
        int min = 100000;
        int max = 999999;
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }

    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        VerificationCode codeEntity = verificationCodeRepository.findByUser(user);
        if (codeEntity != null) {
            verificationCodeRepository.delete(codeEntity);
        }
    }

    public void validateVerificationCode(String email, String verificationCode) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        VerificationCode codeEntity = verificationCodeRepository.findByUser(user);

        if (codeEntity == null) {
            throw new VerificationCodeNotFoundException("Verification code not found for the user.");
        }

        if (!codeEntity.getCode().equals(verificationCode)) {
            throw new VerificationCodeMismatchException("Verification code does not match.");
        }

        if (codeEntity.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new VerificationCodeExpiredException("Verification code has expired.");
        }
    }
}