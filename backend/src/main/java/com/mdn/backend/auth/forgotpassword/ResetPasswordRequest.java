package com.mdn.backend.auth.forgotpassword;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

    private String email;
    private String verificationCode;
    private String newPassword;

}
