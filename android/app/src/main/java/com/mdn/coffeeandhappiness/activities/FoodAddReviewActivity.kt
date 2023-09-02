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
import com.mdn.coffeeandhappiness.controller.FoodController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class FoodAddReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_food_add_review)

        val backButton = findViewById<ImageButton>(R.id.foodAddReviewActivityBackButton)

        backButton.setOnClickListener() {
            finish()
        }


        val foodId = intent.extras?.getInt("food_id")

        val addReviewButton = findViewById<AppCompatButton>(R.id.foodAddReviewButton)

        addReviewButton.setOnClickListener() {

            val ratingBar = findViewById<AppCompatRatingBar>(R.id.foodAddReviewRatingBar)
            val rating = ratingBar.rating.toInt()

            val comment = findViewById<AppCompatEditText>(R.id.foodAddReviewComment).text.toString()

            val foodController = FoodController()
            lifecycleScope.launch() {
                try {
                    foodController.addReview(
                        foodId!!,
                        rating,
                        comment,
                        getSharedPreferences("Account", Context.MODE_PRIVATE)
                    )
                } catch (e: NoInternetException) {
                    launch(Dispatchers.Main) {
                        val noConnection = findViewById<TextView>(R.id.foodAddReviewNoInternet)
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