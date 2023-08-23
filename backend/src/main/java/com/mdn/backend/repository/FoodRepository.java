package com.mdn.backend.repository;

import com.mdn.backend.model.Food;
import com.mdn.backend.model.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findByType(FoodType type);
}
