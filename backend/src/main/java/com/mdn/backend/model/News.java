package com.mdn.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    @NotNull(message = "Title is required")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    @Pattern(regexp = "^https?://.*",
            message = "Image URL must start with http:// or https://")
    private String imageUrl;

    @Column(name = "published_at")
    private Date publishedAt;

}
