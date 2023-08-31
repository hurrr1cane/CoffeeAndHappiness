package com.mdn.coffeeandhappiness.model

data class Review(
    var id: Int,
    var rating: Int,
    var comment: String,
    var date: String,
    var userId: Int
)

data class FoodReview(
    var id: Int,
    var rating: Int,
    var comment: String,
    var date: String,
    var userId: Int,
    var foodId: Int
)

data class CafeReview(
    var id: Int,
    var rating: Int,
    var comment: String,
    var date: String,
    var userId: Int,
    var cafeId: Int
)

data class FoodReviewWithFood(
    var id: Int,
    var rating: Int,
    var comment: String,
    var date: String,
    var userId: Int,
    var foodId: Int,
    var imageUrl: String,
    var nameUA: String,
    var nameEN: String
)

data class CafeReviewWithCafe(
    var id: Int,
    var rating: Int,
    var comment: String,
    var date: String,
    var userId: Int,
    var cafeId: Int,
    var imageUrl: String,
    var nameUA: String,
    var nameEN: String
)
