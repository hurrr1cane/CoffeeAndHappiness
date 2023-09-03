package com.mdn.coffeeandhappiness.controller

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.model.CafeReview
import com.mdn.coffeeandhappiness.model.Food
import com.mdn.coffeeandhappiness.model.Order
import com.mdn.coffeeandhappiness.model.Person
import com.mdn.coffeeandhappiness.tools.BackendAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class OrderController {
    suspend fun placeOrder(
        sharedPreferences: SharedPreferences,
        id: Int,
        listOfIds: MutableList<Int>
    ) {
        return withContext(Dispatchers.IO) {

            val url = "${BackendAddress().address}/api/order"

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create a request body
            val requestBody = createJsonBody(id, listOfIds)

            val token = sharedPreferences.getString("AccessToken", "")

            val request = Request.Builder()
                .url(url)
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
                    Log.d("AccountController", "Response Body: $responseBody")
                } else {
                    // Log the error message for debugging
                    Log.e("AccountController", "HTTP Error: ${response.code}")
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

    suspend fun spendPoints(
        sharedPreferences: SharedPreferences,
        id: Int,
        listOfIds: MutableList<Int>
    ) {
        return withContext(Dispatchers.IO) {

            val url = "${BackendAddress().address}/api/order/spend-points"

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create a request body
            val requestBody = createJsonBody(id, listOfIds)

            val token = sharedPreferences.getString("AccessToken", "")

            val request = Request.Builder()
                .url(url)
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
                    Log.d("AccountController", "Response Body: $responseBody")
                } else {
                    // Log the error message for debugging
                    Log.e("AccountController", "HTTP Error: ${response.code}")
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

    private fun createJsonBody(userId: Int, listOfFood: MutableList<Int>): RequestBody {
        val jsonBody = JSONObject()
        jsonBody.put("userId", userId)
        val foodIdsArray = JSONArray(listOfFood)
        jsonBody.put("foodIds", foodIdsArray)

        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        return jsonBody.toString().toRequestBody(jsonMediaType)
    }

    suspend fun getMyOrders(sharedPreferences: SharedPreferences): MutableList<Order> {
        return withContext(Dispatchers.IO) {
            var listOfOrders = mutableListOf<Order>()

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

            try {
                // Use the OkHttpClient to send the GET request and await the response
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()

                    listOfOrders = parseOrdersFromJson(responseBody)


                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
                throw NoInternetException()
            }

            listOfOrders
        }
    }

    private fun parseOrdersFromJson(responseBody: String?): MutableList<Order> {
        var listOfOrders = mutableListOf<Order>()
        val jsonItem = JSONObject(responseBody)

        val orders = jsonItem.getJSONArray("orders")

        for (i in 0 until orders.length()) {
            val jsonItemOrder = orders.getJSONObject(i)

            val orderId = jsonItemOrder.getInt("id")
            val foods = jsonItemOrder.getJSONArray("foods")
            val totalPrice = jsonItemOrder.getDouble("totalPrice")
            val orderDate = jsonItemOrder.getString("orderDate")
            val bonusPointsGained = jsonItemOrder.getInt("bonusPointsEarned")


            val listOfFood = mutableListOf<Food>()

            FoodController().parseArrayOfFood(foods, listOfFood)

            val order = Order(orderId, listOfFood, totalPrice, orderDate, bonusPointsGained)


            listOfOrders.add(order)
        }

        return listOfOrders
    }

}