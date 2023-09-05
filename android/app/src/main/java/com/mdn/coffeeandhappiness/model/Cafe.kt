package com.mdn.coffeeandhappiness.model

data class Cafe (
    var id: Int,
    var locationEN: String,
    var locationUA: String,
    var latitude: Double,
    var longitude: Double,
    var imageUrl: String,
    var phoneNumber: String,
    var averageRating: Double,
    var totalReviews: Int,
    var reviews: List<Review>
)