package com.mdn.coffeeandhappiness.model

data class News(
    var id: Int,
    var titleEN: String,
    var titleUA: String,
    var descriptionEN: String,
    var descriptionUA: String,
    var imageUrl: String,
    var publishedAt: String
)