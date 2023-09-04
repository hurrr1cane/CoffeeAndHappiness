package com.mdn.backend.service;

import com.mdn.backend.exception.UserNotFoundException;
import com.mdn.backend.model.user.User;
import com.mdn.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public void sendVerificationCode(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        String verificationCode = generateRandomCode();

        emailService.sendVerificationCode(user.getEmail(), verificationCode);
    }

    private String generateRandomCode() {

        Random random = new Random();
        int min = 100000;
        int max = 999999;
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }

}
