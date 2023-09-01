package com.mdn.coffeeandhappiness.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.NewsController
import com.mdn.coffeeandhappiness.tools.DateConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeNewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_news)

        val backButton = findViewById<ImageButton>(R.id.homeNewsActivityBackButton)
        backButton.setOnClickListener() {
            finish()
        }

        val sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("Language", "uk")

        val newsId = intent.extras?.getInt("news_id")

        val context = this

        lifecycleScope.launch(Dispatchers.IO) {
            val news = NewsController().getNews(newsId!!)

            // Update the UI on the main thread
            launch(Dispatchers.Main) {
                val image = findViewById<ImageView>(R.id.homeNewsActivityImage)
                val title = findViewById<TextView>(R.id.homeNewsActivityTitle)
                val description = findViewById<TextView>(R.id.homeNewsActivityDescription)
                val date = findViewById<TextView>(R.id.homeNewsActivityDate)

                var titleText = ""
                var descriptionText = ""

                if (language == "uk") {
                    titleText = news!!.titleUA
                    descriptionText = news.descriptionUA
                } else {
                    titleText = news!!.titleEN
                    descriptionText = news.descriptionEN
                }

                title.text = titleText
                description.text = descriptionText

                date.text = DateConverter().formatDate(news.publishedAt)

                Glide.with(context)
                    .load(news.imageUrl)
                    .placeholder(R.drawable.default_placeholder_image)
                    .error(R.drawable.default_error_image)
                    .into(image)
            }
        }
    }
}