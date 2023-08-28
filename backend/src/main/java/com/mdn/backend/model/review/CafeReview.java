package com.mdn.backend.model.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.model.user.User;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CafeReview extends Review {

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    @JsonIgnore
    private Cafe cafe;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Transient
    private Integer userId;
}
