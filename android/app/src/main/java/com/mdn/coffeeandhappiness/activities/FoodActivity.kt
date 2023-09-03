package com.mdn.coffeeandhappiness.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.controller.FoodController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.model.Food
import com.mdn.coffeeandhappiness.model.PersonInReview
import com.mdn.coffeeandhappiness.tools.FoodTypeTranslator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class FoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLanguage()
        setContentView(R.layout.activity_food)

        val backButton = findViewById<ImageButton>(R.id.foodActivityBackButton)

        backButton.setOnClickListener() {
            finish()
        }

        val accountSettings = getSharedPreferences("Account", Context.MODE_PRIVATE)
        val addReviewButton = findViewById<AppCompatButton>(R.id.foodActivityAddReviewButton)
        if (accountSettings.getBoolean("IsAccountLogged", false)) {
            addReviewButton.visibility = View.VISIBLE
        } else {
            addReviewButton.visibility = View.GONE
        }

        val sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)

        val language = sharedPreferences.getString("Language", "uk")

        val foodId = intent.extras?.getInt("food_id")

        val foodController = FoodController()

        var currentFood: Food? = null

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                currentFood = foodController.getFood(foodId!!)

                // Update the UI on the main thread
                launch(Dispatchers.Main) {
                    val foodPicture = findViewById<ImageView>(R.id.foodActivityImage)
                    val foodName = findViewById<TextView>(R.id.foodActivityName)
                    val foodType = findViewById<TextView>(R.id.foodActitityType)
                    val foodDescription = findViewById<TextView>(R.id.foodActivityDescription)
                    val foodIngredients = findViewById<TextView>(R.id.foodActivityIngredients)
                    val foodIngredientsLabel =
                        findViewById<TextView>(R.id.foodActivityIngredientsLabel)
                    val foodWeight = findViewById<TextView>(R.id.foodActivityWeight)
                    val foodPrice = findViewById<TextView>(R.id.foodActivityPrice)
                    val foodRating = findViewById<TextView>(R.id.foodActivityRating)
                    val foodRatingBar = findViewById<AppCompatRatingBar>(R.id.foodActivityRatingBar)

                    val foodTypeTranslator = FoodTypeTranslator()

                    if (language == "uk") {
                        foodName.text = currentFood?.nameUA
                        foodType.text = foodTypeTranslator.translateTo("uk", currentFood?.type!!)
                        foodDescription.text = currentFood?.descriptionUA
                        if (currentFood?.ingredientsUA == "null") {
                            foodIngredients.visibility = View.GONE
                            foodIngredientsLabel.visibility = View.GONE
                        } else {
                            foodIngredients.visibility = View.VISIBLE
                            foodIngredientsLabel.visibility = View.VISIBLE
                            foodIngredients.text = currentFood?.ingredientsUA
                        }
                        val foodWeightText = currentFood?.weight.toString() + "г"
                        foodWeight.text = foodWeightText
                    } else if (language == "en") {
                        foodName.text = currentFood?.nameEN
                        foodType.text = foodTypeTranslator.translateTo("en", currentFood?.type!!)
                        foodDescription.text = currentFood?.descriptionEN
                        if (currentFood?.ingredientsEN == "null") {
                            foodIngredients.visibility = View.GONE
                            foodIngredientsLabel.visibility = View.GONE
                        } else {
                            foodIngredients.visibility = View.VISIBLE
                            foodIngredientsLabel.visibility = View.VISIBLE
                            foodIngredients.text = currentFood?.ingredientsEN
                        }
                        val foodWeightText = currentFood?.weight.toString() + "g"
                        foodWeight.text = foodWeightText
                    }

                    val foodPriceText = String.format("%.2f", currentFood?.price) + "₴"
                    foodPrice.text = foodPriceText

                    foodRating.text = String.format("%.1f", currentFood?.averageRating)

                    foodRatingBar.rating = currentFood?.averageRating!!.toFloat()

                    Glide.with(applicationContext)
                        .load(currentFood?.imageUrl)
                        .placeholder(R.drawable.default_placeholder_image)
                        .error(R.drawable.default_error_image)
                        .into(foodPicture)


                    val viewAllReviews =
                        findViewById<AppCompatButton>(R.id.foodActivityViewAllReviewsButton)
                    if (currentFood?.reviews!!.size > 3) {
                        viewAllReviews.visibility = View.VISIBLE
                    } else {
                        viewAllReviews.visibility = View.GONE
                    }

                    val reviewsContainer =
                        findViewById<LinearLayout>(R.id.foodActivityReviewsSection)

                    val count = if (currentFood!!.reviews.size > 3) {
                        3
                    } else currentFood!!.reviews.size

                    for (i in 0 until count) {
                        val accountController = AccountController()
                        var personInReview: PersonInReview? = null
                        lifecycleScope.launch() {
                            try {
                                personInReview =
                                    accountController.getById(currentFood!!.reviews[i].userId)


                                launch() {
                                    val cardView =
                                        layoutInflater.inflate(
                                            R.layout.dynamic_card_review,
                                            null
                                        ) as CardView
                                    val userPicture =
                                        cardView.findViewById<ImageView>(R.id.reviewPicture)
                                    val userName = cardView.findViewById<TextView>(R.id.reviewName)
                                    val userText = cardView.findViewById<TextView>(R.id.reviewText)
                                    val reviewRating =
                                        cardView.findViewById<AppCompatRatingBar>(R.id.reviewRatingBar)


                                    Glide.with(applicationContext)
                                        .load(personInReview!!.imageUrl)
                                        .placeholder(R.drawable.baseline_person_white_24)
                                        .error(R.drawable.baseline_person_white_24)
                                        .into(userPicture)

                                    userName.text =
                                        personInReview!!.firstName + " " + personInReview!!.lastName

                                    reviewRating.rating = currentFood!!.reviews[i].rating.toFloat()
                                    userText.text = currentFood!!.reviews[i].comment

                                    val layoutParams = LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )
                                    cardView.layoutParams = layoutParams

                                    reviewsContainer.addView(cardView)
                                }
                            } catch (e: NoInternetException) {
                                //Do nothing
                            }
                        }
                    }

                }
            } catch (e: NoInternetException) {
                launch(Dispatchers.Main) {
                    val foodPicture = findViewById<ImageView>(R.id.foodActivityImage)
                    foodPicture.setImageResource(R.drawable.disconnected)
                    foodPicture.scaleType = ImageView.ScaleType.FIT_CENTER
                }
            }
        }


        val viewAllReviews = findViewById<AppCompatButton>(R.id.foodActivityViewAllReviewsButton)

        viewAllReviews.setOnClickListener() {
            // Handle the click event here, for example, open a new activity with details
            val intent = Intent(this, FoodReviewsActivity::class.java)
            intent.putExtra(
                "food_id",
                currentFood!!.id
            ) // Pass any data you need to the next activity
            startActivity(intent)
        }


        addReviewButton.setOnClickListener() {
            val intent = Intent(this, FoodAddReviewActivity::class.java)
            intent.putExtra(
                "food_id",
                currentFood!!.id
            ) // Pass any data you need to the next activity
            startActivityForResult(intent, 1)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                // Reload the current activity here
                // For example, you can call a function to refresh the UI or recreate the activity
                // For simplicity, I'm calling recreate() to recreate the activity
                recreate()
            }
        }
    }
}