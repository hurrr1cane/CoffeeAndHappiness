package com.mdn.coffeeandhappiness.model

data class Food(
    val id: Int,
    val nameEN: String,
    val nameUA: String,
    val descriptionEN: String,
    val descriptionUA: String,
    val imageUrl: String,
    val price: Double,
    val ingredientsEN: String,
    val ingredientsUA: String,
    val weight: Double,
    val type: String,
    val averageRating: Double,
    val totalReviews: Int,
    val reviews: List<Any>
)