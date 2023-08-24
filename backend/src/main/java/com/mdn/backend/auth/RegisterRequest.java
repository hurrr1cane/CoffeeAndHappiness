package com.mdn.backend.auth;

import com.mdn.backend.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;

}
