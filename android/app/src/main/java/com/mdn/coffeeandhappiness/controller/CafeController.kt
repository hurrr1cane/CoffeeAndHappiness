package com.mdn.coffeeandhappiness.controller

import android.content.SharedPreferences
import android.util.Log
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.model.Cafe
import com.mdn.coffeeandhappiness.model.Review
import com.mdn.coffeeandhappiness.tools.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class CafeController {


    public suspend fun addReview(
        id: Int,
        rating: Int,
        comment: String,
        sharedPreferences: SharedPreferences
    ) {
        return withContext(Dispatchers.IO) {

            val url = "${Constants().address}/api/review/cafe/"
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
                    Log.d("CafeController", "Response Body: $responseBody")

                } else {
                    // Log the error message for debugging
                    Log.e("CafeController", "HTTP Error: ${response.code}")
                }

                // Close the response body to release resources
                response.close()

            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
                throw NoInternetException()
            }

        }
    }

    public suspend fun getCafe(): MutableList<Cafe> {
        return withContext(Dispatchers.IO) {
            val cafeList: MutableList<Cafe> = mutableListOf()

            // Define the URL you want to send the GET request to
            val url = "${Constants().address}/api/cafe"

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create a request object for the GET request
            val request = Request.Builder()
                .url(url)
                .build()

            try {
                // Use the OkHttpClient to send the GET request and await the response
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    cafeList.addAll(parseCafe(responseBody!!))
                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
                throw NoInternetException()
            }

            cafeList
        }
    }

    public suspend fun getCafe(id: Int): Cafe? {
        return withContext(Dispatchers.IO) {
            var myCafe: Cafe? = null

            // Define the URL you want to send the GET request to
            val url = "${Constants().address}/api/cafe/"

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
                    myCafe = parseSingleCafe(responseBody!!)
                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
                throw NoInternetException()
            }

            myCafe
        }
    }

    private fun parseSingleCafe(response: String): Cafe {
        val jsonItem = JSONObject(response)

        // Parse individual fields from the JSON object
        val id = jsonItem.getInt("id")
        val locationEN = jsonItem.getString("locationEN")
        val locationUA = jsonItem.getString("locationUA")
        val latitude = jsonItem.getDouble("latitude")
        val longitude = jsonItem.getDouble("longitude")
        val imageUrl = jsonItem.getString("imageUrl")
        val phoneNumber = jsonItem.getString("phoneNumber")
        val averageRating = jsonItem.getDouble("averageRating")
        val totalReviews = jsonItem.getInt("totalReviews")
        val reviews = jsonItem.getJSONArray("reviews")

        var reviewsList: MutableList<Review> = mutableListOf<Review>()

        for (i in 0 until reviews.length()) {
            val jsonItemReview = reviews.getJSONObject(i)

            val idReview = jsonItemReview.getInt("id")
            val rating = jsonItemReview.getInt("rating")
            val comment = jsonItemReview.getString("comment")
            val date = jsonItemReview.getString("date")
            val userId = jsonItemReview.getInt("userId")

            val singleReview = Review(
                idReview, rating, comment, date, userId
            )

            reviewsList.add(singleReview)
        }

        reviewsList = reviewsList.sortedByDescending {it.id}.toMutableList()

        // Create a MenuItem object
        val cafe = Cafe(
            id, locationEN, locationUA, latitude, longitude, imageUrl, phoneNumber,
            averageRating, totalReviews, reviewsList
        )

        return cafe
    }

    private fun parseCafe(response: String): MutableList<Cafe> {
        // Assuming your JSON response is stored in a variable called responseBody
        val jsonArray = JSONArray(response)

        // Create a list to store parsed objects
        val menuItems = mutableListOf<Cafe>()

        // Iterate through the JSON array
        parseArrayOfCafe(jsonArray, menuItems)

        return menuItems
    }

    private fun parseArrayOfCafe(
        jsonArray: JSONArray,
        cafes: MutableList<Cafe>
    ) {
        for (i in 0 until jsonArray.length()) {
            val jsonItem = jsonArray.getJSONObject(i)

            // Parse individual fields from the JSON object
            val id = jsonItem.getInt("id")
            val locationEN = jsonItem.getString("locationEN")
            val locationUA = jsonItem.getString("locationUA")
            val latitude = jsonItem.getDouble("latitude")
            val longitude = jsonItem.getDouble("longitude")
            val imageUrl = jsonItem.getString("imageUrl")
            val phoneNumber = jsonItem.getString("phoneNumber")
            val averageRating = jsonItem.getDouble("averageRating")
            val totalReviews = jsonItem.getInt("totalReviews")

            // Create a MenuItem object and add it to the list
            val cafe = Cafe(
                id, locationEN, locationUA, latitude, longitude, imageUrl, phoneNumber,
                averageRating, totalReviews, emptyList()
            )

            cafes.add(cafe)
        }
    }
}