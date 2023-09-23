package com.mdn.backend.auth.verifyemail;

import com.mdn.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Integer> {
    EmailVerification findByUser(User user);
    EmailVerification findByUserEmail(String email);
}
