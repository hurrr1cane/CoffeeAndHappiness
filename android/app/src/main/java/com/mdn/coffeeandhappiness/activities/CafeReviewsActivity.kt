package com.mdn.coffeeandhappiness.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import com.mdn.coffeeandhappiness.controller.CafeController
import com.mdn.coffeeandhappiness.controller.FoodController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.model.Cafe
import com.mdn.coffeeandhappiness.model.Food
import com.mdn.coffeeandhappiness.model.PersonInReview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class CafeReviewsActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        setLanguage()
        setContentView(R.layout.activity_cafe_reviews)

        val backButton = findViewById<ImageButton>(R.id.cafeReviewsActivityBackButton)

        backButton.setOnClickListener() {
            finish()
        }

        val cafeId = intent.extras?.getInt("cafe_id")


        val reviewsContainer = findViewById<LinearLayout>(R.id.cafeReviewsActivityContainer)
        var currentCafe: Cafe? = null
        val cafeController = CafeController()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                currentCafe = cafeController.getCafe(cafeId!!)

                val count = currentCafe!!.reviews.size

                launch() {
                    for (i in 0 until count) {
                        val accountController = AccountController()
                        var personInReview: PersonInReview? = null
                        lifecycleScope.launch() {
                            try {
                                personInReview =
                                    accountController.getById(currentCafe!!.reviews[i].userId)


                                launch(Dispatchers.Main) {
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

                                    reviewRating.rating = currentCafe!!.reviews[i].rating.toFloat()
                                    userText.text = currentCafe!!.reviews[i].comment

                                    val layoutParams = LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )
                                    cardView.layoutParams = layoutParams

                                    reviewsContainer.addView(cardView)
                                }
                            } catch (e: NoInternetException) {

                            }
                        }
                    }
                }
            } catch (e: NoInternetException) {
                launch(Dispatchers.Main) {
                    val noConnection = findViewById<LinearLayout>(R.id.cafeReviewsNoInternet)
                    noConnection.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_cafe_reviews)

        val backButton = findViewById<ImageButton>(R.id.cafeReviewsActivityBackButton)

        backButton.setOnClickListener() {
            finish()
        }

        val cafeId = intent.extras?.getInt("cafe_id")


        val reviewsContainer = findViewById<LinearLayout>(R.id.cafeReviewsActivityContainer)
        var currentCafe: Cafe? = null
        val cafeController = CafeController()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                currentCafe = cafeController.getCafe(cafeId!!)

                val count = currentCafe!!.reviews.size

                launch() {
                    for (i in 0 until count) {
                        val accountController = AccountController()
                        var personInReview: PersonInReview? = null
                        lifecycleScope.launch() {
                            try {
                                personInReview =
                                    accountController.getById(currentCafe!!.reviews[i].userId)


                                launch(Dispatchers.Main) {
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

                                    reviewRating.rating = currentCafe!!.reviews[i].rating.toFloat()
                                    userText.text = currentCafe!!.reviews[i].comment

                                    val layoutParams = LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )
                                    cardView.layoutParams = layoutParams

                                    reviewsContainer.addView(cardView)
                                }
                            } catch (e: NoInternetException) {

                            }
                        }
                    }
                }
            } catch (e: NoInternetException) {
                launch(Dispatchers.Main) {
                    val noConnection = findViewById<LinearLayout>(R.id.cafeReviewsNoInternet)
                    noConnection.visibility = View.VISIBLE
                }
            }
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