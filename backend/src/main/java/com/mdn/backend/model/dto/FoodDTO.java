package com.mdn.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDTO {

    private String nameEN;
    private String nameUA;

    private String descriptionEN;
    private String descriptionUA;

    private String imageUrl;

    private double price;

    private String foodType;

}
