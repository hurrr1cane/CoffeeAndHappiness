package com.mdn.backend.repository;

import com.mdn.backend.model.review.CafeReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeReviewRepository extends JpaRepository<CafeReview, Integer> {
}
