package com.mdn.backend.model;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Integer id;

    @Column(name = "title_en")
    @NotNull(message = "Title is required")
    private String titleEN;

    @Column(name = "title_ua")
    @NotNull(message = "Title is required")
    private String titleUA;

    @Column(name = "description_en")
    private String descriptionEN;

    @Column(name = "description_ua")
    private String descriptionUA;

    @Column(name = "url")
    @Pattern(regexp = "^https?://.*",
            message = "Image URL must start with http:// or https://")
    private String imageUrl;

    @Column(name = "published_at")
    @Hidden
    private Date publishedAt;

}
