package com.mdn.backend.model;

import com.mdn.backend.model.review.CafeReview;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Integer id;

    @Column(name = "location_en")
    @NotNull(message = "Location is required")
    @Size(max = 255, message = "Location cannot exceed 255 characters")
    private String locationEN;

    @Column(name = "location_ua")
    @NotNull(message = "Location is required")
    @Size(max = 255, message = "Location cannot exceed 255 characters")
    private String locationUA;

    @Column(name = "latitude")
    @NotNull(message = "Latitude is required")
    private double latitude;

    @Column(name = "longitude")
    @NotNull(message = "Longitude is required")
    private double longitude;

    @Column(name = "image_url")
    @Pattern(regexp = "^https?://.*",
            message = "Image URL must start with http:// or https://")
    private String imageUrl;

    @Column(name = "phone_number")
    @Pattern(regexp = "(?:\\+380|0)\\d{9}",
            message = "Phone number must be in format +380XXXXXXXXX or 0XXXXXXXXX")
    private String phoneNumber;

    @Column(name = "average_rating")
    @Hidden
    private double averageRating;

    @Column(name = "total_reviews")
    @Hidden
    private int totalReviews;

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Hidden
    private List<CafeReview> reviews;
}
