package com.mdn.coffeeandhappiness.controller

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.mdn.coffeeandhappiness.model.Person
import com.mdn.coffeeandhappiness.model.PersonInReview
import com.mdn.coffeeandhappiness.tools.BackendAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

class AccountController {

    suspend fun getById(id: Int): PersonInReview? {
        return withContext(Dispatchers.IO) {
            var personInReview: PersonInReview? = null

            // Define the URL you want to send the GET request to
            val url = "${BackendAddress().address}/api/user/"

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
                    personInReview = gson.fromJson(responseBody, PersonInReview::class.java)

                    //val jsonItem = JSONObject(responseBody)
                    //val imageUrl = jsonItem.getString("imageUrl")
                    //val firstName = jsonItem.getString("firstName")
                    //val lastName = jsonItem.getString("lastName")


                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

            personInReview
        }
    }

    suspend fun login(
        email: String,
        password: String,
        sharedPreferences: SharedPreferences
    ): Boolean {
        return withContext(Dispatchers.IO) {

            val url = "${BackendAddress().address}/api/auth/login"

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
                    editor.putString("Password", password)

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

    fun logout(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.putString("AccessToken", "")
        editor.putString("RefreshToken", "")
        editor.putString("Email", "")
        editor.putString("Name", "")
        editor.putString("Surname", "")
        editor.putString("Password", "")
        editor.putBoolean("IsAccountLogged", false)
        editor.putInt("Id", 0)
        editor.putString("ImageUrl", "")
        editor.putString("Role", "")
        editor.putInt("BonusPoints", 0)
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

            val url = "${BackendAddress().address}/api/auth/register"

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
                    editor.putString("Password", password)

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
                val url = "${BackendAddress().address}/api/auth/refresh"

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
                } else if ((currentTime - storedTime > 1000 * 60 * 60 * 24 * 13)) {
                    login(
                        sharedPreferences.getString("Email", "")!!,
                        sharedPreferences.getString("Password", "")!!,
                        sharedPreferences
                    )
                }
            }
        }
    }

    suspend fun updateMyself(sharedPreferences: SharedPreferences) {
        return withContext(Dispatchers.IO) {
            var person: Person? = null

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

                    // Use Gson to parse the JSON response
                    val gson = Gson()
                    person = gson.fromJson(responseBody, Person::class.java)

                    val editor = sharedPreferences.edit()
                    editor.putInt("Id", person.id)
                    editor.putString("Name", person.firstName)
                    editor.putString("Surname", person.lastName)
                    editor.putString("Email", person.email)
                    editor.putString("ImageUrl", person.imageUrl)
                    editor.putString("Role", person.role)
                    editor.putInt("BonusPoints", person.bonusPoints)
                    editor.apply()

                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }

        }
    }

    suspend fun getByToken(token: String): Person? {
        return withContext(Dispatchers.IO) {
            var person: Person? = null

            // Define the URL you want to send the GET request to
            val url = "${BackendAddress().address}/api/user/me"

            // Create an OkHttpClient instance
            val client = OkHttpClient()


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

                    // Use Gson to parse the JSON response
                    val gson = Gson()
                    person = gson.fromJson(responseBody, Person::class.java)

                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
            }
            person
        }
    }


}

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)