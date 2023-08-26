package com.mdn.coffeeandhappiness.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.activities.FoodActivity
import com.mdn.coffeeandhappiness.model.Food

class FoodRecyclerViewAdapter(private val context: Context, private val foodList: MutableList<Food>): RecyclerView.Adapter<FoodRecyclerViewAdapter.FoodHolder>() {

    class FoodHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.foodCardCardView)
        val cardImageView = itemView.findViewById<ImageView>(R.id.foodCardImageView)
        val cardTitleTextView = itemView.findViewById<TextView>(R.id.foodCardTitleTextView)
        val cardDescriptionTextView = itemView.findViewById<TextView>(R.id.foodCardDescriptionTextView)
        val cardRatingBarView = itemView.findViewById<RatingBar>(R.id.foodCardRating)
        val cardPriceTextView = itemView.findViewById<TextView>(R.id.foodCardPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.dynamic_card_food,
            parent, false)
        return FoodHolder(itemView)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        //val currentItem = foodList[position]

        val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("Language", "uk")

        Glide.with(context)
            .load(foodList[position].imageUrl)
            .placeholder(R.drawable.default_placeholder_image)
            .error(R.drawable.default_error_image)
            .into(holder.cardImageView)


        var titleText = ""
        var descriptionText = ""

        when (language) {
            "uk" -> {
                titleText = foodList[position].nameUA
                descriptionText = foodList[position].descriptionUA
            }
            "en" -> {
                titleText = foodList[position].nameEN
                descriptionText = foodList[position].descriptionEN
            }
        }

        holder.cardTitleTextView.text = titleText
        holder.cardDescriptionTextView.text = descriptionText
        holder.cardRatingBarView.rating = foodList[position].averageRating.toFloat()
        val price = String.format("%.2f", foodList[position].price) + "₴"
        holder.cardPriceTextView.text = price



        holder.cardView.setOnClickListener() {
            // Handle the click event here, for example, open a new activity with details
            val intent = Intent(context, FoodActivity::class.java)
            intent.putExtra(
                "food_id",
                foodList[position].id
            ) // Pass any data you need to the next activity
            context.startActivity(intent)
        }
    }
}