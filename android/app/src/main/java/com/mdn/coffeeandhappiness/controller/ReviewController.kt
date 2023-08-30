package com.mdn.coffeeandhappiness.controller

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mdn.coffeeandhappiness.model.CafeReview
import com.mdn.coffeeandhappiness.model.CafeReviewWithCafe
import com.mdn.coffeeandhappiness.model.Food
import com.mdn.coffeeandhappiness.model.FoodReview
import com.mdn.coffeeandhappiness.model.FoodReviewWithFood
import com.mdn.coffeeandhappiness.model.Review
import com.mdn.coffeeandhappiness.tools.BackendAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ReviewController {

    suspend fun getCafeReviewsWithCafe (sharedPreferences:SharedPreferences): MutableList<CafeReviewWithCafe> {
        return withContext(Dispatchers.IO) {
            var listOfCafeReviewsWithCafe = mutableListOf<CafeReviewWithCafe>()
            val listOfCafeReviews = getCafeReviews(sharedPreferences)
            for (foodReview in listOfCafeReviews) {
                val food = FoodController().getFood(foodReview.cafeId)
                listOfCafeReviewsWithCafe.add(CafeReviewWithCafe(foodReview.id, foodReview.rating, foodReview.comment, foodReview.date, foodReview.userId, foodReview.cafeId, food!!.imageUrl, food.nameUA, food.nameEN))
            }
            listOfCafeReviewsWithCafe
        }
    }

    suspend fun getFoodReviewsWithFood (sharedPreferences:SharedPreferences): MutableList<FoodReviewWithFood> {
        return withContext(Dispatchers.IO) {
            var listOfFoodReviewWithFood = mutableListOf<FoodReviewWithFood>()
            val listOfFoodReviews = getFoodReviews(sharedPreferences)
            for (foodReview in listOfFoodReviews) {
                val food = FoodController().getFood(foodReview.foodId)
                listOfFoodReviewWithFood.add(FoodReviewWithFood(foodReview.id, foodReview.rating, foodReview.comment, foodReview.date, foodReview.userId, foodReview.foodId, food!!.imageUrl, food.nameUA, food.nameEN))
            }
            listOfFoodReviewWithFood
        }
    }

    suspend fun getFoodReviews (sharedPreferences:SharedPreferences): MutableList<FoodReview> {
        return withContext(Dispatchers.IO) {
            // Define the URL you want to send the GET request to
            val url = "${BackendAddress().address}/api/user/me"

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            val token = sharedPreferences.getString("AccessToken", "")

            // Create a request object for the GET request
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            var listFoodReviews: MutableList<FoodReview> = mutableListOf()

            try {
                // Use the OkHttpClient to send the GET request and await the response
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()

                    listFoodReviews = parseFoodReviewsFromJson(responseBody!!)

                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

            listFoodReviews
        }
    }

    private fun parseFoodReviewsFromJson(jsonResponse: String): MutableList<FoodReview> {
        val listFoodReviews = mutableListOf<FoodReview>()

        val jsonItem = JSONObject(jsonResponse)

        val reviews = jsonItem.getJSONArray("foodReviews")


        for (i in 0 until reviews.length()) {
            val jsonItemReview = reviews.getJSONObject(i)

            val idReview = jsonItemReview.getInt("id")
            val rating = jsonItemReview.getInt("rating")
            val comment = jsonItemReview.getString("comment")
            val date = jsonItemReview.getString("date")
            val userId = jsonItemReview.getInt("userId")
            val foodId = jsonItemReview.getInt("foodId")

            val singleReview = FoodReview(
                idReview, rating, comment, date, userId, foodId)

            listFoodReviews.add(singleReview)
        }

        return listFoodReviews
    }

    suspend fun getCafeReviews (sharedPreferences:SharedPreferences): MutableList<CafeReview> {
        return withContext(Dispatchers.IO) {
            // Define the URL you want to send the GET request to
            val url = "${BackendAddress().address}/api/user/me"

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            val token = sharedPreferences.getString("AccessToken", "")

            // Create a request object for the GET request
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            var listCafeReviews: MutableList<CafeReview> = mutableListOf()

            try {
                // Use the OkHttpClient to send the GET request and await the response
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()

                    listCafeReviews = parseCafeReviewsFromJson(responseBody!!)

                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

            listCafeReviews
        }
    }

    private fun parseCafeReviewsFromJson(jsonResponse: String): MutableList<CafeReview> {
        val listCafeReviews = mutableListOf<CafeReview>()

        val jsonItem = JSONObject(jsonResponse)

        val reviews = jsonItem.getJSONArray("cafeReviews")


        for (i in 0 until reviews.length()) {
            val jsonItemReview = reviews.getJSONObject(i)

            val idReview = jsonItemReview.getInt("id")
            val rating = jsonItemReview.getInt("rating")
            val comment = jsonItemReview.getString("comment")
            val date = jsonItemReview.getString("date")
            val userId = jsonItemReview.getInt("userId")
            val cafeId = jsonItemReview.getInt("cafeId")

            val singleReview = CafeReview(
                idReview, rating, comment, date, userId, cafeId)

            listCafeReviews.add(singleReview)
        }

        return listCafeReviews
    }

    suspend fun deleteFoodReview(sharedPreferences:SharedPreferences, reviewId: Int) {
        return withContext(Dispatchers.IO) {
            // Define the URL you want to send the GET request to
            val url = "${BackendAddress().address}/api/review/food/"
            val finalUrl = url + reviewId.toString()

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            val token = sharedPreferences.getString("AccessToken", "")

            // Create a request object for the GET request
            val request = Request.Builder()
                .url(finalUrl)
                .delete()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            try {
                // Use the OkHttpClient to send the GET request and await the response
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {


                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

        }
    }

    suspend fun deleteCafeReview(sharedPreferences:SharedPreferences, reviewId: Int) {
        return withContext(Dispatchers.IO) {
            // Define the URL you want to send the GET request to
            val url = "${BackendAddress().address}/api/review/cafe/"
            val finalUrl = url + reviewId.toString()

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            val token = sharedPreferences.getString("AccessToken", "")

            // Create a request object for the GET request
            val request = Request.Builder()
                .url(finalUrl)
                .delete()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            try {
                // Use the OkHttpClient to send the GET request and await the response
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {


                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

        }
    }
}