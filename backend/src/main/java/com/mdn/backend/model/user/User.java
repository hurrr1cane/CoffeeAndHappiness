package com.mdn.backend.model.user;

import com.mdn.backend.model.review.CafeReview;
import com.mdn.backend.model.review.FoodReview;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    @NotNull(message = "First name is required")
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = "Last name is required")
    private String lastName;

    @Column(name = "email")
    @NotNull(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Column(name = "password")
    @NotNull(message = "Password is required")
    private String password;

    @Column(name = "image_url")
    @Pattern(regexp = "^https?://.*",
            message = "Image URL must start with http:// or https://")
    private String imageUrl;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "bonus_points")
    @Min(value = 0, message = "Bonus points must be greater than 0")
    private int bonusPoints;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodReview> foodReviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CafeReview> cafeReviews;
}