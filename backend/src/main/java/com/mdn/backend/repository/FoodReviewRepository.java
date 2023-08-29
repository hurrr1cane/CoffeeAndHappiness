package com.mdn.backend.repository;

import com.mdn.backend.model.review.FoodReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodReviewRepository extends JpaRepository<FoodReview, Integer> {
}
