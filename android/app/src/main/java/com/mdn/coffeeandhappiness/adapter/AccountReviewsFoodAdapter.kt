package com.mdn.coffeeandhappiness.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.fragments.accountfragments.reviewsfragments.ConfirmationDeleteFoodReviewFragment
import com.mdn.coffeeandhappiness.model.FoodReviewWithFood

class AccountReviewsFoodAdapter(private val context: Context, private val reviewsList: MutableList<FoodReviewWithFood>): RecyclerView.Adapter<AccountReviewsFoodAdapter.ReviewHolder>() {

    class ReviewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.accountReviewCardView)
        val cardImageView = itemView.findViewById<ImageView>(R.id.accountReviewImageView)
        val cardTitleTextView = itemView.findViewById<TextView>(R.id.accountReviewTitleTextView)
        val cardMessageTextView = itemView.findViewById<TextView>(R.id.accountReviewMessageTextView)
        val cardRatingBarView = itemView.findViewById<RatingBar>(R.id.accountReviewRating)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.accountReviewDeleteButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.dynamic_account_review,
            parent, false)
        return ReviewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return reviewsList.size
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {

        val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("Language", "uk")


        Glide.with(context)
            .load(reviewsList[position].imageUrl)
            .placeholder(R.drawable.default_placeholder_image)
            .error(R.drawable.default_error_image)
            .into(holder.cardImageView)


        var titleText = ""

        when (language) {
            "uk" -> {
                titleText = reviewsList[position].nameUA
            }
            "en" -> {
                titleText = reviewsList[position].nameEN
            }
        }

        holder.cardTitleTextView.text = titleText
        holder.cardMessageTextView.text = reviewsList[position].comment
        holder.cardRatingBarView.rating = reviewsList[position].rating.toFloat()


        holder.deleteButton.setOnClickListener() {
            val confirmationDialog = ConfirmationDeleteFoodReviewFragment(reviewsList[position].id) {
                // This lambda function will be called when the user clicks "Yes"
                reviewsList.removeAt(position)
                notifyItemRemoved(position)
                // Handle the item removal logic here
            }
            confirmationDialog.show((context as FragmentActivity).supportFragmentManager , "ConfirmationDialog")


        }
    }
}