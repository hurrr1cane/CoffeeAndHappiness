package com.mdn.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO {

    private String titleEN;
    private String titleUA;

    private String descriptionEN;
    private String descriptionUA;

    private String imageUrl;

    private String publishedAt;

}
