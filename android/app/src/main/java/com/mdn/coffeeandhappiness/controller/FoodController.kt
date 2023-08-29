package com.mdn.coffeeandhappiness.controller

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.mdn.coffeeandhappiness.model.Food
import com.mdn.coffeeandhappiness.model.Review
import com.mdn.coffeeandhappiness.tools.BackendAddress
import okhttp3.*
import java.io.IOException

import org.json.JSONArray
import org.json.JSONObject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class FoodController {

    public suspend fun addReview(id:Int, rating: Int, comment: String, sharedPreferences:SharedPreferences) {
        return withContext(Dispatchers.IO) {

            val url = "${BackendAddress().address}/api/review/food/"
            val finalUrl = url + id.toString()

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create JSON data for the request body
            val json = """
                {
                    "rating": $rating,
                    "comment": "$comment"
                }
            """.trimIndent()

            // Set the media type as JSON
            val mediaType = "application/json; charset=utf-8".toMediaType()

            // Create a request body
            val requestBody = json.toRequestBody(mediaType)

            val token = sharedPreferences.getString("AccessToken", "")

            val request = Request.Builder()
                .url(finalUrl)
                .post(requestBody)
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            try {
                // Use the OkHttpClient to send the POST request
                val response: Response = client.newCall(request).execute()

                // Check if the request was successful
                if (response.isSuccessful) {
                    // Handle the successful response here
                    val responseBody = response.body?.string()

                    // Log the response body for debugging
                    Log.d("FoodController", "Response Body: $responseBody")

                } else {
                    // Log the error message for debugging
                    Log.e("FoodController", "HTTP Error: ${response.code}")
                }

                // Close the response body to release resources
                response.close()

            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

        }
    }

    public suspend fun getFood(foodType: String): MutableList<Food> {
        return withContext(Dispatchers.IO) {
            val foodList: MutableList<Food> = mutableListOf()

            // Define the URL you want to send the GET request to
            val url = "${BackendAddress().address}/api/food/type/"

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

    public suspend fun getFood(id: Int): Food? {
        return withContext(Dispatchers.IO) {
            var myFood: Food? = null

            // Define the URL you want to send the GET request to
            val url = "${BackendAddress().address}/api/food/"

            val finalUrl = "$url$id"

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
                    myFood = parseSingleFood(responseBody!!)
                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

            myFood
        }
    }


    private fun parseSingleFood(response: String): Food {
        val jsonItem = JSONObject(response)

        // Parse individual fields from the JSON object
        val id = jsonItem.getInt("id")
        val nameEN = jsonItem.getString("nameEN")
        val nameUA = jsonItem.getString("nameUA")
        val descriptionEN = jsonItem.getString("descriptionEN")
        val descriptionUA = jsonItem.getString("descriptionUA")
        val imageUrl = jsonItem.getString("imageUrl")
        val price = jsonItem.getDouble("price")
        val ingredientsEN = jsonItem.getString("ingredientsEN")
        val ingredientsUA = jsonItem.getString("ingredientsEN")
        val weight = jsonItem.getDouble("weight")
        val type = jsonItem.getString("type")
        val averageRating = jsonItem.getDouble("averageRating")
        val totalReviews = jsonItem.getInt("totalReviews")
        val reviews = jsonItem.getJSONArray("reviews")

        val reviewsList: MutableList<Review> = mutableListOf<Review>()

        for (i in 0 until reviews.length()) {
            val jsonItemReview = reviews.getJSONObject(i)

            val idReview = jsonItemReview.getInt("id")
            val rating = jsonItemReview.getInt("rating")
            val comment = jsonItemReview.getString("comment")
            val userId = jsonItemReview.getInt("userId")

            val singleReview = Review(
                idReview, rating, comment, userId)

            reviewsList.add(singleReview)
        }

        // Create a MenuItem object
        val menuItem = Food(
            id, nameEN, nameUA, descriptionEN, descriptionUA, imageUrl, price, ingredientsEN,
            ingredientsUA, weight, type, averageRating, totalReviews, reviewsList
        )

        return menuItem
    }

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
            val nameEN = jsonItem.getString("nameEN")
            val nameUA = jsonItem.getString("nameUA")
            val descriptionEN = jsonItem.getString("descriptionEN")
            val descriptionUA = jsonItem.getString("descriptionUA")
            val imageUrl = jsonItem.getString("imageUrl")
            val price = jsonItem.getDouble("price")
            val ingredientsEN = jsonItem.getString("ingredientsEN")
            val ingredientsUA = jsonItem.getString("ingredientsEN")
            val weight = jsonItem.getDouble("weight")
            val type = jsonItem.getString("type")
            val averageRating = jsonItem.getDouble("averageRating")
            val totalReviews = jsonItem.getInt("totalReviews")

            // Create a MenuItem object and add it to the list
            val menuItem = Food(
                id, nameEN, nameUA, descriptionEN, descriptionUA, imageUrl, price, ingredientsEN,
                ingredientsUA, weight, type, averageRating, totalReviews, emptyList()
            )

            menuItems.add(menuItem)
        }

        return menuItems
    }
}