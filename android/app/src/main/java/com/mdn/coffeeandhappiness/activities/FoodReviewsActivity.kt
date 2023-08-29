package com.mdn.coffeeandhappiness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.controller.FoodController
import com.mdn.coffeeandhappiness.model.Food
import com.mdn.coffeeandhappiness.model.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodReviewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_reviews)

        val backButton = findViewById<ImageButton>(R.id.foodReviewsActivityBackButton)

        backButton.setOnClickListener() {
            finish()
        }

        val foodId = intent.extras?.getInt("food_id")


        val reviewsContainer = findViewById<LinearLayout>(R.id.foodReviewsActivityContainer)
        var currentFood: Food? = null
        val foodController = FoodController()

        lifecycleScope.launch(Dispatchers.IO) {
            currentFood = foodController.getFood(foodId!!)

            val count = currentFood!!.reviews.size

            launch() {
                for (i in 0 until count) {
                    val accountController = AccountController()
                    var person: Person? = null
                    lifecycleScope.launch() {
                        person = accountController.getById(currentFood!!.reviews[i].userId)


                        launch(Dispatchers.Main) {
                            val cardView =
                                layoutInflater.inflate(
                                    R.layout.dynamic_card_review,
                                    null
                                ) as CardView
                            val userPicture = cardView.findViewById<ImageView>(R.id.reviewPicture)
                            val userName = cardView.findViewById<TextView>(R.id.reviewName)
                            val userText = cardView.findViewById<TextView>(R.id.reviewText)
                            val reviewRating =
                                cardView.findViewById<AppCompatRatingBar>(R.id.reviewRatingBar)


                            Glide.with(applicationContext)
                                .load(person!!.imageUrl)
                                .placeholder(R.drawable.baseline_person_white_24)
                                .error(R.drawable.baseline_person_white_24)
                                .into(userPicture)

                            userName.text = person!!.firstName + " " + person!!.lastName

                            reviewRating.rating = currentFood!!.reviews[i].rating.toFloat()
                            userText.text = currentFood!!.reviews[i].comment

                            val layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            cardView.layoutParams = layoutParams

                            reviewsContainer.addView(cardView)
                        }
                    }
                }
            }
        }
    }
}