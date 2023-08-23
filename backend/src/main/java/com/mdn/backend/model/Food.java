package com.mdn.backend.model;


import com.mdn.backend.model.review.FoodReview;
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
    private Integer id;

    @NotNull(message = "Name is required")
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    @Pattern(regexp = "^https?://.*",
            message = "Image URL must start with http:// or https://")
    private String imageUrl;

    @Column(name = "price")
    @Min(value = 0, message = "Price must be greater than 0")
    @Max(value = 10000, message = "Price must be less than 10000")
    @NotNull(message = "Price is required")
    private Double price;

    @Column(name = "ingredients")
    @NotNull(message = "Ingredients are required")
    @Pattern(regexp = "^[a-zA-Z\\s,]*$",
            message = "Ingredients can only contain letters, commas, and spaces")
    private String ingredients;

    @Column(name = "weight")
    @Min(value = 0, message = "Weight must be greater than 0")
    @Max(value = 10000, message = "Weight must be less than 10000")
    private Double weight;

    @Enumerated(EnumType.STRING)
    private FoodType type;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodReview> reviews;
}
