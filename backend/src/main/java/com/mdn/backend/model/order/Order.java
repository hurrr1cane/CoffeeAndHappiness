package com.mdn.backend.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdn.backend.model.Food;
import com.mdn.backend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToMany
    @JoinTable(
            name = "order_food",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> foods;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "bonus_points_earned")
    private Integer bonusPointsEarned;
}
