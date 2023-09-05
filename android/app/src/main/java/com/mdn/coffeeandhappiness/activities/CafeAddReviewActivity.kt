package com.mdn.coffeeandhappiness.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.lifecycle.lifecycleScope
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.CafeController
import com.mdn.coffeeandhappiness.controller.FoodController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class CafeAddReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_cafe_add_review)

        val backButton = findViewById<ImageButton>(R.id.cafeAddReviewActivityBackButton)

        backButton.setOnClickListener() {
            finish()
        }


        val cafeId = intent.extras?.getInt("cafe_id")

        val addReviewButton = findViewById<AppCompatButton>(R.id.cafeAddReviewButton)

        addReviewButton.setOnClickListener() {

            val ratingBar = findViewById<AppCompatRatingBar>(R.id.cafeAddReviewRatingBar)
            val rating = ratingBar.rating.toInt()

            val comment = findViewById<AppCompatEditText>(R.id.cafeAddReviewComment).text.toString()

            val cafeController = CafeController()
            lifecycleScope.launch() {
                try {
                    cafeController.addReview(
                        cafeId!!,
                        rating,
                        comment,
                        getSharedPreferences("Account", Context.MODE_PRIVATE)
                    )
                } catch (e: NoInternetException) {
                    launch(Dispatchers.Main) {
                        val noConnection = findViewById<TextView>(R.id.cafeAddReviewNoInternet)
                        noConnection.visibility = View.VISIBLE
                    }
                }
            }
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun setLanguage() {
        val languagePreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        var languageToSet = languagePreferences.getString("Language", "uk")

        var locale = Locale(languageToSet)
        Locale.setDefault(locale)
        var configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        baseContext.resources.updateConfiguration(
            configuration,
            baseContext.resources.displayMetrics
        )
    }
}