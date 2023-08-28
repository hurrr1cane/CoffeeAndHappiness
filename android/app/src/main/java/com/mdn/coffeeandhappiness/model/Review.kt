package com.mdn.coffeeandhappiness.model

data class Review(
    var id: Int,
    var rating: Int,
    var comment: String,
    var userId: Int)