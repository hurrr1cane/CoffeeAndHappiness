package com.mdn.backend.auth.verifyemail;

import com.mdn.backend.auth.forgotpassword.EmailService;
import com.mdn.backend.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;

    public void sendEmailVerification(User user) {
        EmailVerification existingCode = emailVerificationRepository.findByUser(user);

        if (existingCode != null) {
            emailVerificationRepository.delete(existingCode);
        }

        var expirationTime = LocalDateTime.now().plusMinutes(5);

        var verificationCode = EmailVerification.builder()
                .verificationCode(generateRandomCode())
                .expirationTime(expirationTime)
                .user(user)
                .build();

        emailService.sendEmailVerification(user.getEmail(), verificationCode.getVerificationCode());

        emailVerificationRepository.save(verificationCode);
    }

    private String generateRandomCode() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }

    public void validateEmailVerification(String email, String verificationCode) {
        EmailVerification emailVerification = emailVerificationRepository.findByUserEmail(email);

        if (emailVerification == null) {
            throw new RuntimeException("Invalid verification code");
        }

        if (!emailVerification.getVerificationCode().equals(verificationCode)) {
            throw new RuntimeException("Invalid verification code");
        }

        if (emailVerification.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code expired");
        }

        User user = emailVerification.getUser();
        user.setEnabled(true);
        emailVerificationRepository.delete(emailVerification);
    }
}

