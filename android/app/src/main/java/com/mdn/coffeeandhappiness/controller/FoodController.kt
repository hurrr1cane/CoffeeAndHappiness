package com.mdn.coffeeandhappiness.controller

import com.mdn.coffeeandhappiness.model.Food
import okhttp3.*
import java.io.IOException

import org.json.JSONArray
import org.json.JSONObject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodController {


    public suspend fun getFood(foodType: String): MutableList<Food> {
        return withContext(Dispatchers.IO) {
            val foodList: MutableList<Food> = mutableListOf()

            // Define the URL you want to send the GET request to
            val url = "http://192.168.0.23:8080/api/food/type/"

            val finalUrl = "$url$foodType"

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create a request object for the GET request
            val request = Request.Builder()
                .url(finalUrl)
                .build()

            try {
                // Use the OkHttpClient to send the GET request and await the response
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    foodList.addAll(parseFood(responseBody!!))
                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

            foodList
        }
    }

    /*public fun getFood(foodType:String): MutableList<Food> {

        var foodList: MutableList<Food> = mutableListOf<Food>()

        // Define the URL you want to send the GET request to
        val url = "http://192.168.0.23:8080/api/food/type/"  // Replace with your API endpoint

        var finalUrl = url + foodType

        // Create an OkHttpClient instance
        val client = OkHttpClient()

        // Create a request object for the GET request
        val request = Request.Builder()
            .url(finalUrl)
            .build()

        // Use the OkHttpClient to send the GET request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    // Handle unsuccessful response (e.g., HTTP error codes)
                    return
                }

                // Handle the successful response here
                val responseBody = response.body?.string()

                foodList = parseFood(responseBody!!)

            }
        })


        return foodList

    }*/

    private fun parseFood(response:String): MutableList<Food> {
        // Assuming your JSON response is stored in a variable called responseBody
        val jsonArray = JSONArray(response)

// Create a list to store parsed objects
        val menuItems = mutableListOf<Food>()

// Iterate through the JSON array
        for (i in 0 until jsonArray.length()) {
            val jsonItem = jsonArray.getJSONObject(i)

            // Parse individual fields from the JSON object
            val id = jsonItem.getInt("id")
            val name = jsonItem.getString("name")
            val description = jsonItem.getString("description")
            val imageUrl = jsonItem.getString("imageUrl")
            val price = jsonItem.getDouble("price")
            val ingredients = jsonItem.getString("ingredients")
            val weight = jsonItem.getDouble("weight")
            val type = jsonItem.getString("type")
            val averageRating = jsonItem.getDouble("averageRating")
            val totalReviews = jsonItem.getInt("totalReviews")

            // Create a MenuItem object and add it to the list
            val menuItem = Food(
                id, name, description, imageUrl, price, ingredients,
                weight, type, averageRating, totalReviews, emptyList()
            )

            menuItems.add(menuItem)
        }

        return menuItems
    }
}