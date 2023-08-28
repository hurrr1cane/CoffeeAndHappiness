package com.mdn.coffeeandhappiness.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.FoodController
import kotlinx.coroutines.launch

class FoodAddReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_add_review)

        val foodId = intent.extras?.getInt("food_id")

        val addReviewButton = findViewById<AppCompatButton>(R.id.foodAddReviewButton)

        addReviewButton.setOnClickListener() {
            val foodController = FoodController()
            lifecycleScope.launch() {
                foodController.addReview(
                    foodId!!,
                    4,
                    "nonono",
                    getSharedPreferences("Account", Context.MODE_PRIVATE)
                )
            }
        }
    }
}