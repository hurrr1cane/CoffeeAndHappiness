package com.mdn.backend.auth.forgotpassword;

import com.mdn.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {
    VerificationCode findByUser(User user);
}
