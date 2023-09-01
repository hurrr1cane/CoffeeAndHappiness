package com.mdn.coffeeandhappiness.model

data class Order(
    var id: Int,
    var foods: MutableList<Food>,
    var totalPrice: Double,
    var orderDate: String,
    var bonusPointsEarned: Int
)