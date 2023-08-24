package com.mdn.backend.model;

import com.mdn.backend.model.review.CafeReview;
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
    private Integer id;

    @Column(name = "location")
    @NotNull(message = "Location is required")
    @Size(max = 255, message = "Location cannot exceed 255 characters")
    private String location;

    @Column(name = "image_url")
    @Pattern(regexp = "^https?://.*",
            message = "Image URL must start with http:// or https://")
    private String imageUrl;

    @Column(name = "phone_number")
    @Pattern(regexp = "(?:\\+380|0)\\d{9}",
            message = "Phone number must be in format +380XXXXXXXXX or 0XXXXXXXXX")
    private String phoneNumber;

    @Column(name = "average_rating")
    private double averageRating;

    @Column(name = "total_reviews")
    private int totalReviews;

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CafeReview> reviews;
}
