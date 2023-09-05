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
import com.mdn.coffeeandhappiness.controller.CafeController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.model.Cafe
import com.mdn.coffeeandhappiness.model.PersonInReview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class CafeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLanguage()
        setContentView(R.layout.activity_cafe)

        val backButton = findViewById<ImageButton>(R.id.cafeActivityBackButton)

        backButton.setOnClickListener() {
            finish()
        }

        val accountSettings = getSharedPreferences("Account", Context.MODE_PRIVATE)
        val addReviewButton = findViewById<AppCompatButton>(R.id.cafeActivityAddReviewButton)
        if (accountSettings.getBoolean("IsAccountLogged", false)) {
            addReviewButton.visibility = View.VISIBLE
        } else {
            addReviewButton.visibility = View.GONE
        }

        val sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)

        val language = sharedPreferences.getString("Language", "uk")

        val cafeId = intent.extras?.getInt("cafe_id")

        val cafeController = CafeController()

        var currentCafe: Cafe? = null

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                currentCafe = cafeController.getCafe(cafeId!!)

                // Update the UI on the main thread
                launch(Dispatchers.Main) {
                    val cafePicture = findViewById<ImageView>(R.id.cafeActivityImage)
                    val cafeName = findViewById<TextView>(R.id.cafeActivityName)
                    val cafeRating = findViewById<TextView>(R.id.cafeActivityRating)
                    val cafeRatingBar = findViewById<AppCompatRatingBar>(R.id.cafeActivityRatingBar)

                    if (language == "uk") {
                        cafeName.text = currentCafe?.locationUA
                    } else if (language == "en") {
                        cafeName.text = currentCafe?.locationEN
                    }

                    cafeRating.text = String.format("%.1f", currentCafe?.averageRating)

                    cafeRatingBar.rating = currentCafe?.averageRating!!.toFloat()

                    Glide.with(applicationContext)
                        .load(currentCafe?.imageUrl)
                        .placeholder(R.drawable.default_placeholder_image)
                        .error(R.drawable.default_error_image)
                        .into(cafePicture)


                    val viewAllReviews =
                        findViewById<AppCompatButton>(R.id.cafeActivityViewAllReviewsButton)
                    if (currentCafe?.reviews!!.size > 3) {
                        viewAllReviews.visibility = View.VISIBLE
                    } else {
                        viewAllReviews.visibility = View.GONE
                    }

                    val reviewsContainer =
                        findViewById<LinearLayout>(R.id.cafeActivityReviewsSection)

                    val count = if (currentCafe!!.reviews.size > 3) {
                        3
                    } else currentCafe!!.reviews.size

                    for (i in 0 until count) {
                        val accountController = AccountController()
                        var personInReview: PersonInReview? = null
                        lifecycleScope.launch() {
                            try {
                                personInReview =
                                    accountController.getById(currentCafe!!.reviews[i].userId)


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
                                //Do nothing
                            }
                        }
                    }

                }
            } catch (e: NoInternetException) {
                launch(Dispatchers.Main) {
                    val cafePicture = findViewById<ImageView>(R.id.cafeActivityImage)
                    cafePicture.setImageResource(R.drawable.disconnected)
                    cafePicture.scaleType = ImageView.ScaleType.FIT_CENTER
                }
            }
        }


        val viewAllReviews = findViewById<AppCompatButton>(R.id.cafeActivityViewAllReviewsButton)

        viewAllReviews.setOnClickListener() {
            // Handle the click event here, for example, open a new activity with details
            val intent = Intent(this, CafeReviewsActivity::class.java)
            intent.putExtra(
                "cafe_id",
                currentCafe!!.id
            ) // Pass any data you need to the next activity
            startActivity(intent)
        }


        addReviewButton.setOnClickListener() {
            val intent = Intent(this, CafeAddReviewActivity::class.java)
            intent.putExtra(
                "cafe_id",
                currentCafe!!.id
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