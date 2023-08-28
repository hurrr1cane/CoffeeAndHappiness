package com.mdn.backend.model.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdn.backend.model.Food;
import com.mdn.backend.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FoodReview extends Review {

    @ManyToOne
    @JoinColumn(name = "food_id")
    @JsonIgnore
    private Food food;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Transient
    private Integer userId;
}
