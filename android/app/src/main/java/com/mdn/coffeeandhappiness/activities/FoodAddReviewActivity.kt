package com.mdn.coffeeandhappiness.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.lifecycle.lifecycleScope
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.FoodController
import kotlinx.coroutines.launch

class FoodAddReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                foodController.addReview(
                    foodId!!,
                    rating,
                    comment,
                    getSharedPreferences("Account", Context.MODE_PRIVATE)
                )
            }
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}