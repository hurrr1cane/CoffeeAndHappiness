package com.mdn.coffeeandhappiness.tools

import com.mdn.coffeeandhappiness.model.Food

class OrderHelper {

    fun countTotalPrice(listOfFood: MutableList<Food>): Double {
        var price = 0.0

        for (food in listOfFood) {
            price += food.price
        }
        return price
    }

    fun getListOfIds(listOfFood: MutableList<Food>): MutableList<Int> {
        var listOfIds = mutableListOf<Int>()

        for (food in listOfFood) {
            listOfIds.add(food.id)
        }
        return listOfIds
    }
}