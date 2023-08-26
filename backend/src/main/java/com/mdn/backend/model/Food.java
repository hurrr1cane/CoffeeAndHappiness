package com.mdn.backend.model;


import com.mdn.backend.model.review.FoodReview;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Integer id;

    @Column(name = "name_en")
    @NotNull(message = "Name is required")
    private String nameEN;

    @Column(name = "name_ua")
    @NotNull(message = "Name is required")
    private String nameUA;

    @Column(name = "description_en")
    private String descriptionEN;

    @Column(name = "description_ua")
    private String descriptionUA;

    @Column(name = "image_url")
    @Pattern(regexp = "^https?://.*",
            message = "Image URL must start with http:// or https://")
    private String imageUrl;

    @Column(name = "price")
    @Min(value = 0, message = "Price must be greater than 0")
    @Max(value = 10000, message = "Price must be less than 10000")
    @NotNull(message = "Price is required")
    private Double price;

    @Column(name = "ingredients_en")
    @Pattern(regexp = "^[a-zA-Z\\s,]*$",
            message = "Ingredients can only contain letters, commas, and spaces")
    private String ingredientsEN;

    @Column(name = "ingredients_ua")
    @Pattern(regexp = "^[a-zA-Z\\s,]*$",
            message = "Ingredients can only contain letters, commas, and spaces")
    private String ingredientsUA;

    @Column(name = "weight")
    @Min(value = 0, message = "Weight must be greater than 0")
    @Max(value = 10000, message = "Weight must be less than 10000")
    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @NotNull(message = "Type is required")
    @Schema(title = "Food type", allowableValues = "MAIN, DRINK, COFFEE, SALAD, DESSERT, ICE_CREAM")
    private FoodType type;

    @Column(name = "average_rating")
    @Hidden
    private double averageRating;

    @Column(name = "total_reviews")
    @Hidden
    private int totalReviews;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    @Hidden
    private List<FoodReview> reviews;
}
