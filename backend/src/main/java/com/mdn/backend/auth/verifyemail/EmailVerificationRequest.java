package com.mdn.backend.auth.verifyemail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationRequest {

    private String email;
    private String verificationCode;

}
