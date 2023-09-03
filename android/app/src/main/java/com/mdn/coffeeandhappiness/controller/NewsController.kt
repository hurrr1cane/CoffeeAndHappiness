package com.mdn.coffeeandhappiness.controller

import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.model.News
import com.mdn.coffeeandhappiness.tools.BackendAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class NewsController {

    public suspend fun getNews(): MutableList<News> {
        return withContext(Dispatchers.IO) {
            val newsList: MutableList<News> = mutableListOf()

            // Define the URL you want to send the GET request to
            val url = "${BackendAddress().address}/api/news"

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

                    val newsArray = JSONArray(responseBody)

                    for (i in 0 until newsArray.length()) {

                        val newsObject = newsArray.getJSONObject(i)

                        newsList.add(
                            News(
                                newsObject.getInt("id"),
                                newsObject.getString("titleEN"),
                                newsObject.getString("titleUA"),
                                newsObject.getString("descriptionEN"),
                                newsObject.getString("descriptionUA"),
                                newsObject.getString("imageUrl"),
                                newsObject.getString("publishedAt")
                            )
                        )
                    }

                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
                throw NoInternetException()
            }

            newsList
        }
    }

    suspend fun getNews(id: Int): News? {
        return withContext(Dispatchers.IO) {
            var news: News? = null

            // Define the URL you want to send the GET request to
            val url = "${BackendAddress().address}/api/news/"

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

                    val newsObject = JSONObject(responseBody!!)

                    news =
                        News(
                            newsObject.getInt("id"),
                            newsObject.getString("titleEN"),
                            newsObject.getString("titleUA"),
                            newsObject.getString("descriptionEN"),
                            newsObject.getString("descriptionUA"),
                            newsObject.getString("imageUrl"),
                            newsObject.getString("publishedAt")
                        )

                }
            } catch (e: IOException) {
                // Handle failure, such as network issues
                e.printStackTrace()
                throw NoInternetException()
            }

            news
        }
    }
}