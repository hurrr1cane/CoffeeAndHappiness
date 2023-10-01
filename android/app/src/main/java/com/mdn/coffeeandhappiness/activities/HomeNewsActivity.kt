package com.mdn.coffeeandhappiness.activities

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.NewsController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.tools.DateConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class HomeNewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
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
            val image = findViewById<ImageView>(R.id.homeNewsActivityImage)
            try {
                val news = NewsController().getNews(newsId!!)

                // Update the UI on the main thread
                launch(Dispatchers.Main) {

                    val title = findViewById<TextView>(R.id.homeNewsActivityTitle)
                    val description = findViewById<TextView>(R.id.homeNewsActivityDescription)
                    val date = findViewById<TextView>(R.id.homeNewsActivityDate)

                    val titleText: String
                    val descriptionText: String

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
                        .into(image)
                }
            } catch (e: NoInternetException) {
                launch(Dispatchers.Main) {
                    image.setImageResource(R.drawable.disconnected)
                    image.scaleType = ImageView.ScaleType.FIT_CENTER
                }
            }
        }
    }

    private fun setLanguage() {
        val languagePreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val languageToSet = languagePreferences.getString("Language", "uk")

        val locale = Locale(languageToSet)
        Locale.setDefault(locale)
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        baseContext.resources.updateConfiguration(
            configuration,
            baseContext.resources.displayMetrics
        )
    }
}