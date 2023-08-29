package com.mdn.coffeeandhappiness.model

data class Person(
    var id: Int,
    var firstName: String,
    var lastName: String,
    var email: String,
    var imageUrl: String,
    var role: String,
    var bonusPoints: Int
)
