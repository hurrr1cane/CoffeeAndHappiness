package com.mdn.backend.model.dto;

import com.mdn.backend.model.FoodType;
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

    private String ingredientsEN;
    private String ingredientsUA;

    private String imageUrl;

    private Double weight;
    private Double price;
    private FoodType type;

}
