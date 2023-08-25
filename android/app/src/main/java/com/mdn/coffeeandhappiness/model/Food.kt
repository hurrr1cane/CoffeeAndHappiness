package com.mdn.coffeeandhappiness.model

data class Food(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val ingredients: String,
    val weight: Double,
    val type: String,
    val averageRating: Double,
    val totalReviews: Int,
    val reviews: List<Any>
)