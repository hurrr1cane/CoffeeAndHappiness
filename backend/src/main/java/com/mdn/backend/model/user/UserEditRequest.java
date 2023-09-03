package com.mdn.backend.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEditRequest {

    private String firstName;
    private String lastName;
    private String imageUrl;
    private String phoneNumber;

}
