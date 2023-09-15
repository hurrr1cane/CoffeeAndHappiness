package com.mdn.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CafeDTO {

    private String locationEN;
    private String locationUA;

    private Double latitude;
    private Double longitude;

    private String imageUrl;
    private String phoneNumber;

}
