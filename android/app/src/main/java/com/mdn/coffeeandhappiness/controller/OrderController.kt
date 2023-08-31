package com.mdn.coffeeandhappiness.controller

import android.content.SharedPreferences
import android.util.Log
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

}