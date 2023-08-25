package com.mdn.coffeeandhappiness.tools

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.model.Food
import com.mdn.coffeeandhappiness.model.News

class SectionSetter {
    public fun setFoodSection(listOfFood: MutableList<Food>, rootView: View?, layoutInflater: LayoutInflater, context: Context?, foodType: String) {

        var foodSection: LinearLayout? = null // Initialize it as null

        when (foodType) {
            "Main" -> foodSection = rootView?.findViewById(R.id.menuMainLayout)
            "Drinks" -> foodSection = rootView?.findViewById(R.id.menuDrinksLayout)
            "Coffee" -> foodSection = rootView?.findViewById(R.id.menuCoffeeLayout)
            "Salads" -> foodSection = rootView?.findViewById(R.id.menuSaladsLayout)
            "Desserts" -> foodSection = rootView?.findViewById(R.id.menuDessertsLayout)
            "IceCream" -> foodSection = rootView?.findViewById(R.id.menuIceCreamLayout)
        }

        // Check if the foodSection is null
        if (foodSection == null) {
            // Handle the case when the foodSection is not found
            return
        }

        for (food in listOfFood) {
            val cardView = layoutInflater.inflate(R.layout.dynamic_card_food, null) as CardView
            val cardImageView = cardView.findViewById<ImageView>(R.id.foodCardImageView)
            val cardTitleTextView = cardView.findViewById<TextView>(R.id.foodCardTitleTextView)
            val cardDescriptionTextView = cardView.findViewById<TextView>(R.id.foodCardDescriptionTextView)
            val cardRatingBarView = cardView.findViewById<RatingBar>(R.id.foodCardRating)
            val cardPriceTextView = cardView.findViewById<TextView>(R.id.foodCardPrice)

            //val sharedPreferences = context?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            //val language = sharedPreferences?.getString("Language", "uk")

            Glide.with(rootView!!)
                .load(food.imageUrl)
                .placeholder(R.drawable.default_placeholder_image)
                .error(R.drawable.default_error_image)
                .into(cardImageView)



            /*val text = when (language) {
                "uk" -> singleNew.nameUk
                "en" -> singleNew.nameEn
                else -> {
                    "No"
                }
            }
            cardTitleTextView.text = text // Set your title text
            cardDateTextView.text = singleNew.date // Set your date text*/

            cardTitleTextView.text = food.name
            cardDescriptionTextView.text = food.description
            cardRatingBarView.rating = food.averageRating.toFloat()
            val price = String.format("%.2f", food.price) + "₴"
            cardPriceTextView.text = price


            // Set margins if needed
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                context!!.resources.getDimensionPixelSize(R.dimen.food_card_height)
            )
            val margin = context!!.resources.getDimensionPixelSize(R.dimen.food_card_margin)

            layoutParams.setMargins(margin, margin, margin, margin) // Set margins if needed

            cardView.layoutParams = layoutParams

            foodSection?.addView(cardView)
        }
    }

}