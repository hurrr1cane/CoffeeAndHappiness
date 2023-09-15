package com.mdn.coffeeandhappiness.controller

import android.content.Context
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.model.Person
import com.mdn.coffeeandhappiness.model.PersonInReview
import com.mdn.coffeeandhappiness.tools.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AccountController {

    suspend fun forgotPasswordValidate(email: String, code: String): Boolean {
        return withContext(Dispatchers.IO) {

            val url = "${Constants().address}/api/auth/validate-verification-code"


            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create JSON data for the request body
            val json = """{"email":"$email","verificationCode":"$code"}"""

            // Set the media type as JSON
            val mediaType = "application/json; charset=utf-8".toMediaType()

            // Create a request body
            val requestBody = json.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            var codeCorrect = false
            try {
                // Use the OkHttpClient to send the POST request
                val response: Response = client.newCall(request).execute()

                // Check if the request was successful
                codeCorrect = response.isSuccessful

                // Close the response body to release resources
                response.close()

            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
                throw NoInternetException()
            }

            codeCorrect
        }
    }

    suspend fun forgotPasswordSendCode(email: String): Boolean {
        return withContext(Dispatchers.IO) {

            val url = "${Constants().address}/api/auth/forgot-password?email=$email"


            // Create an OkHttpClient instance
            val client = OkHttpClient()

            val requestBody = RequestBody.create(null, ByteArray(0))

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            var sent = false
            try {
                // Use the OkHttpClient to send the POST request
                val response: Response = client.newCall(request).execute()

                // Check if the request was successful
                sent = response.isSuccessful

                // Close the response body to release resources
                response.close()

            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
                throw NoInternetException()
            }

            sent
        }
    }

    suspend fun getById(id: Int): PersonInReview? {
        return withContext(Dispatchers.IO) {
            var personInReview: PersonInReview? = null

            // Define the URL you want to send the GET request to
            val url = "${Constants().address}/api/user/"

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
                throw NoInternetException()
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

            val url = "${Constants().address}/api/auth/login"

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
                throw NoInternetException()
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
        editor.putString("PhoneNumber", "")
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

            val url = "${Constants().address}/api/auth/register"

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
                throw NoInternetException()
            }

            logined
        }
    }

    suspend fun updateToken(sharedPreferences: SharedPreferences) {
        return withContext(Dispatchers.IO) {

            if (sharedPreferences.getBoolean("IsAccountLogged", false)) {
                val url = "${Constants().address}/api/auth/refresh"

                // Create an OkHttpClient instance
                val client = OkHttpClient()

                val currentTime = java.util.Date().time
                val storedTime = sharedPreferences.getLong("UpdateTime", 0)

                if (((currentTime - storedTime) >= 1000 * 60 * 60 * 23) && (currentTime - storedTime < 1000 * 60 * 60 * 24 * 13)) {

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
            val url = "${Constants().address}/api/user/me"

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
                    editor.putString("PhoneNumber", person.phoneNumber)
                    editor.putString("ImageUrl", person.imageUrl)
                    editor.putString("Role", person.role)
                    editor.putInt("BonusPoints", person.bonusPoints)
                    editor.apply()

                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
                throw NoInternetException()
            }

        }
    }

    suspend fun getByToken(token: String): Person? {
        return withContext(Dispatchers.IO) {
            var person: Person? = null

            // Define the URL you want to send the GET request to
            val url = "${Constants().address}/api/user/me"

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
                throw NoInternetException()
            }
            person
        }
    }

    suspend fun updatePicture(sharedPreferences: SharedPreferences, uri: Uri, context: Context) {
        return withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver

            // Create an InputStream from the Uri
            val inputStream = contentResolver.openInputStream(uri)

            if (inputStream != null) {
                // Create a temporary file to store the image
                val tempFile = File(context.cacheDir, "temp_image.jpg")

                // Use an OutputStream to copy the contents of the InputStream to the file
                val outputStream = FileOutputStream(tempFile)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                // Now, you can use tempFile for your HTTP request

                val url = "${Constants().address}/api/user/me/image/add"
                val token = sharedPreferences.getString("AccessToken", "")

                // Create an OkHttpClient instance
                val client = OkHttpClient()

                // Create a request body with the image file
                val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), tempFile)

                // Create a multi-part request
                val request = Request.Builder()
                    .url(url) // Replace with your server URL
                    .post(
                        MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("image", tempFile.name, requestBody)
                            .build()
                    )
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("Accept-Encoding", "gzip, deflate, br")
                    .build()

                try {
                    // Execute the request
                    val response = client.newCall(request).execute()

                    // Handle the response as needed
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        // Do something with the response
                    } else {
                        // Handle the error
                    }
                } catch (e: IOException) {
                    throw NoInternetException()
                }
            }
        }
    }

    suspend fun deletePicture(sharedPreferences: SharedPreferences) {
        return withContext(Dispatchers.IO) {
            val url = "${Constants().address}/api/user/me/image/delete"

            val token = sharedPreferences.getString("AccessToken", "")

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create a multi-part request
            val request = Request.Builder()
                .url(url) // Replace with your server URL
                .delete()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            try {
                // Execute the request
                val response = client.newCall(request).execute()

                // Handle the response as needed
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    // Do something with the response
                } else {
                    // Handle the error
                }
            } catch (e: IOException) {
                throw NoInternetException()
            }

        }
    }

    suspend fun updateInformation(sharedPreferences: SharedPreferences, name: String, surname: String, phone: String) {
        return withContext(Dispatchers.IO) {
            val url = "${Constants().address}/api/user/me/edit"


            val json = """
                {
                   "firstName": "$name",
                   "lastName": "$surname",
                   "phoneNumber": "$phone"
                }
            """.trimIndent()

            // Set the media type as JSON
            val mediaType = "application/json; charset=utf-8".toMediaType()

            // Create a request body
            val requestBody = json.toRequestBody(mediaType)

            val token = sharedPreferences.getString("AccessToken", "")

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create a multi-part request
            val request = Request.Builder()
                .url(url) // Replace with your server URL
                .put(requestBody)
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            Log.d("AccountController", requestBody.toString())
            try {
                // Execute the request
                val response = client.newCall(request).execute()

                // Handle the response as needed
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("", responseBody!!)
                } else {
                    // Handle the error
                    Log.d("", "Error: ${response.code}")
                }
            } catch (e: IOException) {
                throw NoInternetException()
            }

        }
    }

    suspend fun changePassword(sharedPreferences: SharedPreferences, oldPassword: String, newPassword: String): Boolean {
        return withContext(Dispatchers.IO) {
            val url = "${Constants().address}/api/user/me/change-password"

            var result = true

            val json = """
                {
                   "oldPassword": "$oldPassword",
                   "newPassword": "$newPassword"
                }
            """.trimIndent()

            // Set the media type as JSON
            val mediaType = "application/json; charset=utf-8".toMediaType()

            // Create a request body
            val requestBody = json.toRequestBody(mediaType)

            val token = sharedPreferences.getString("AccessToken", "")

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create a multi-part request
            val request = Request.Builder()
                .url(url) // Replace with your server URL
                .put(requestBody)
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            try {
                // Execute the request
                val response = client.newCall(request).execute()

                // Handle the response as needed
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    // Do something with the response
                } else {
                    // Handle the error
                    result = false
                }
            } catch (e: IOException) {
                throw NoInternetException()
            }

            result
        }
    }

    suspend fun deleteAccount(sharedPreferences: SharedPreferences) {
        return withContext(Dispatchers.IO) {
            val url = "${Constants().address}/api/user/me/delete"

            val token = sharedPreferences.getString("AccessToken", "")

            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create a multi-part request
            val request = Request.Builder()
                .url(url) // Replace with your server URL
                .delete()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            try {
                // Execute the request
                val response = client.newCall(request).execute()

                // Handle the response as needed
                if (response.isSuccessful) {
                    logout(sharedPreferences)
                }
            } catch (e: IOException) {
                throw NoInternetException()
            }

        }
    }

    suspend fun forgotPasswordSetNew(email: String, code: String, password: String) {
        return withContext(Dispatchers.IO) {

            val url = "${Constants().address}/api/auth/reset-password"


            // Create an OkHttpClient instance
            val client = OkHttpClient()

            // Create JSON data for the request body
            val json = """{"email":"$email","verificationCode":"$code","newPassword":"$password"}"""

            // Set the media type as JSON
            val mediaType = "application/json; charset=utf-8".toMediaType()

            // Create a request body
            val requestBody = json.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build()

            try {
                // Use the OkHttpClient to send the POST request
                val response: Response = client.newCall(request).execute()

                // Close the response body to release resources
                response.close()

            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
                throw NoInternetException()
            }
        }
    }


}

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)