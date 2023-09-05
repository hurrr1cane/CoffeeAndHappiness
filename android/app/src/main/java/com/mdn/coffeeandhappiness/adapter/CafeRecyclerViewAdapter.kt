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
import com.mdn.coffeeandhappiness.activities.CafeActivity
import com.mdn.coffeeandhappiness.model.Cafe

class CafeRecyclerViewAdapter(
    private val context: Context,
    private val cafeList: MutableList<Cafe>
) : RecyclerView.Adapter<CafeRecyclerViewAdapter.CafeHolder>() {

    class CafeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.cafeCardCardView)
        val cardImageView = itemView.findViewById<ImageView>(R.id.cafeCardImageView)
        val cardTitleTextView = itemView.findViewById<TextView>(R.id.cafeCardTitleTextView)
        val cardRatingBarView = itemView.findViewById<RatingBar>(R.id.cafeCardRating)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.dynamic_card_cafe,
            parent, false
        )
        return CafeHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cafeList.size
    }

    override fun onBindViewHolder(holder: CafeHolder, position: Int) {
        //val currentItem = foodList[position]

        val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("Language", "uk")

        Glide.with(context)
            .load(cafeList[position].imageUrl)
            .placeholder(R.drawable.default_placeholder_image)
            .error(R.drawable.default_error_image)
            .into(holder.cardImageView)


        var titleText = ""

        when (language) {
            "uk" -> {
                titleText = cafeList[position].locationUA
            }

            "en" -> {
                titleText = cafeList[position].locationEN
            }
        }

        holder.cardTitleTextView.text = titleText
        holder.cardRatingBarView.rating = cafeList[position].averageRating.toFloat()



        holder.cardView.setOnClickListener() {
            // Handle the click event here, for example, open a new activity with details
            val intent = Intent(context, CafeActivity::class.java)
            intent.putExtra(
                "cafe_id",
                cafeList[position].id
            ) // Pass any data you need to the next activity
            context.startActivity(intent)
        }
    }
}