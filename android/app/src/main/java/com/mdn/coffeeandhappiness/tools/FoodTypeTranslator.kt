package com.mdn.coffeeandhappiness.tools

class FoodTypeTranslator {
    fun translateTo(lang: String, type: String): String {
        return when (lang) {
            "uk" -> {
                when (type) {
                    "MAIN" -> "Основна"
                    "DRINK" -> "Напій"
                    "COFFEE" -> "Кава"
                    "SALAD" -> "Салат"
                    "DESSERT" -> "Десерт"
                    "ICE_CREAM" -> "Морозиво"
                    else -> {"Невідомо"}
                }
            }
            "en" -> {
                when (type) {
                    "MAIN" -> "Main"
                    "DRINK" -> "Drink"
                    "COFFEE" -> "Coffee"
                    "SALAD" -> "Salad"
                    "DESSERT" -> "Dessert"
                    "ICE_CREAM" -> "Ice cream"
                    else -> {"Unknown"}
                }
            }
            else -> "Unknown"
        }
    }
}