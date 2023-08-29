/*
package com.mdn.coffeeandhappiness.controller

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class AccountController {

    suspend public fun login(email: String, password: String, sharedPreferences: SharedPreferences): Boolean {
        return withContext(Dispatchers.IO) {

            val url = "http://192.168.0.23:8080/api/auth/login"

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create a request object for the GET request
            val requestBody = FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build()


            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()


            var logined = false
            try {
                // Use the OkHttpClient to send the POST request
                val response = client.newCall(request).execute()

                // Check if the request was successful
                if (response.isSuccessful) {
                    // Handle the successful response here
                    val responseBody = response.body?.string()

                    val editor = sharedPreferences.edit()
                    // Parse the JSON using Gson
                    val gson = Gson()
                    val accessTokenResponse = gson.fromJson(responseBody, AccessTokenResponse::class.java)

                    // Extract the accessToken
                    val accessToken = accessTokenResponse.accessToken

                    editor.putString("AccessToken", accessToken)
                    editor.apply()
                    logined = true

                } else {
                    logined = false
                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

            logined
        }
    }

    fun updateUserInPreferences(email: String, sharedPreferences: SharedPreferences) {


    }
}

data class AccessTokenResponse(
    @SerializedName("accessToken")
    val accessToken: String
)*/

package com.mdn.coffeeandhappiness.controller

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.mdn.coffeeandhappiness.model.Food
import com.mdn.coffeeandhappiness.model.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime
import java.time.LocalTime

class AccountController {

    suspend fun getById(id: Int): Person? {
        return withContext(Dispatchers.IO) {
            var person: Person? = null

            // Define the URL you want to send the GET request to
            val url = "http://192.168.0.23:8080/api/user/"

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

                    // Use Gson to parse the JSON response
                    val gson = Gson()
                    person = gson.fromJson(responseBody, Person::class.java)

                    //val jsonItem = JSONObject(responseBody)
                    //val imageUrl = jsonItem.getString("imageUrl")
                    //val firstName = jsonItem.getString("firstName")
                    //val lastName = jsonItem.getString("lastName")


                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

            person
        }
    }

    suspend fun login(
        email: String,
        password: String,
        sharedPreferences: SharedPreferences
    ): Boolean {
        return withContext(Dispatchers.IO) {

            val url = "http://192.168.0.23:8080/api/auth/login"

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create JSON data for the request body
            val json = """{"email":"$email","password":"$password"}"""

            // Set the media type as JSON
            val mediaType = "application/json; charset=utf-8".toMediaType()

            // Create a request body
            val requestBody = json.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            var logined = false
            try {
                // Use the OkHttpClient to send the POST request
                val response: Response = client.newCall(request).execute()

                // Check if the request was successful
                if (response.isSuccessful) {
                    // Handle the successful response here
                    val responseBody = response.body?.string()

                    // Log the response body for debugging
                    Log.d("AccountController", "Response Body: $responseBody")

                    val editor = sharedPreferences.edit()
                    // Parse the JSON using Gson
                    val gson = Gson()
                    val tokenResponse = gson.fromJson(responseBody, TokenResponse::class.java)

                    val accessToken = tokenResponse.accessToken
                    val refreshToken = tokenResponse.refreshToken

                    editor.putString("AccessToken", accessToken)
                    editor.putString("RefreshToken", refreshToken)
                    editor.putString("Email", email)

                    editor.putLong("UpdateTime", java.util.Date().time)

                    editor.apply()
                    logined = true

                } else {
                    logined = false
                    // Log the error message for debugging
                    Log.e("AccountController", "HTTP Error: ${response.code}")
                }

                // Close the response body to release resources
                response.close()

            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

            logined
        }
    }

    /*suspend fun updateUserInPreferences(email: String, sharedPreferences: SharedPreferences) {
        return withContext(Dispatchers.IO) {
            val foodList: MutableList<Food> = mutableListOf()

            // Define the URL you want to send the GET request to
            val url = "http://192.168.0.23:8080/api/user/email/"

            val finalUrl = "$url$email"

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
    }*/

    fun logout(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.putString("AccessToken", "")
        editor.putString("RefreshToken", "")
        editor.putString("Email", "")
        editor.putString("Name", "")
        editor.putString("Surname", "")
        editor.putBoolean("IsAccountLogged", false)
        editor.apply()
    }

    suspend fun register(
        email: String,
        password: String,
        name: String,
        surname: String,
        sharedPreferences: SharedPreferences
    ): Boolean {
        return withContext(Dispatchers.IO) {

            val url = "http://192.168.0.23:8080/api/auth/register"

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create JSON data for the request body
            val json = """
                {
                   "firstName": "$name",
                   "lastName": "$surname",
                    "email": "$email",
                    "password": "$password"
                }
            """.trimIndent()

            // Set the media type as JSON
            val mediaType = "application/json; charset=utf-8".toMediaType()

            // Create a request body
            val requestBody = json.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            var logined = false
            try {
                // Use the OkHttpClient to send the POST request
                val response: Response = client.newCall(request).execute()

                // Check if the request was successful
                if (response.isSuccessful) {
                    // Handle the successful response here
                    val responseBody = response.body?.string()

                    // Log the response body for debugging
                    Log.d("AccountController", "Response Body: $responseBody")

                    val editor = sharedPreferences.edit()
                    // Parse the JSON using Gson
                    val gson = Gson()
                    val tokenResponse = gson.fromJson(responseBody, TokenResponse::class.java)

                    val accessToken = tokenResponse.accessToken
                    val refreshToken = tokenResponse.refreshToken

                    editor.putString("AccessToken", accessToken)
                    editor.putString("RefreshToken", refreshToken)
                    editor.putString("Email", email)

                    editor.putLong("UpdateTime", java.util.Date().time)

                    editor.apply()
                    logined = true

                } else {
                    logined = false
                    // Log the error message for debugging
                    Log.e("AccountController", "HTTP Error: ${response.code}")
                }

                // Close the response body to release resources
                response.close()

            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

            logined
        }
    }

    suspend fun updateToken(sharedPreferences: SharedPreferences) {
        return withContext(Dispatchers.IO) {

            if (sharedPreferences.getBoolean("IsAccountLogged", false)) {
                val url = "http://192.168.0.23:8080/api/auth/refresh"

                // Create an OkHttpClient instance
                val client = OkHttpClient()

                val currentTime = java.util.Date().time
                val storedTime = sharedPreferences.getLong("UpdateTime", 0)

                if (((currentTime - storedTime) >= 1000 * 60 * 60 * 24) && (currentTime - storedTime < 1000 * 60 * 60 * 24 * 13)) {

                    val token = sharedPreferences.getString("RefreshToken", "")

                    val request = Request.Builder()
                        .url(url)
                        .post(RequestBody.create(null, ByteArray(0)))
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

                            val editor = sharedPreferences.edit()
                            // Parse the JSON using Gson
                            val gson = Gson()
                            val tokenResponse =
                                gson.fromJson(responseBody, TokenResponse::class.java)

                            val accessToken = tokenResponse.accessToken
                            val refreshToken = tokenResponse.refreshToken

                            editor.putString("AccessToken", accessToken)
                            editor.putString("RefreshToken", refreshToken)
                            editor.putLong("UpdateTime", currentTime)

                            editor.apply()

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
                } else if ((currentTime - storedTime > 1000 * 60 * 60 * 24 * 13)) logout(
                    sharedPreferences
                )
            }
        }
    }
}

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)