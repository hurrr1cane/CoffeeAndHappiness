package com.mdn.coffeeandhappiness.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.fragments.accountfragments.reviewsfragments.ConfirmationDeleteCafeReviewFragment
import com.mdn.coffeeandhappiness.model.CafeReviewWithCafe

class AccountReviewsCafeAdapter(
    private val context: Context,
    private val reviewsList: MutableList<CafeReviewWithCafe>
) : RecyclerView.Adapter<AccountReviewsCafeAdapter.ReviewHolder>() {

    class ReviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardImageView = itemView.findViewById<ImageView>(R.id.accountReviewImageView)
        val cardTitleTextView = itemView.findViewById<TextView>(R.id.accountReviewTitleTextView)
        val cardMessageTextView = itemView.findViewById<TextView>(R.id.accountReviewMessageTextView)
        val cardRatingBarView = itemView.findViewById<RatingBar>(R.id.accountReviewRating)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.accountReviewDeleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.dynamic_account_review,
            parent, false
        )
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
            .error(R.drawable.cafe_icon)
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
            val confirmationDialog =
                ConfirmationDeleteCafeReviewFragment(reviewsList[position].id) {
                    // This lambda function will be called when the user clicks "Yes"
                    reviewsList.removeAt(position)
                    notifyItemRemoved(position)
                    // Handle the item removal logic here
                }
            confirmationDialog.show(
                (context as FragmentActivity).supportFragmentManager,
                "ConfirmationDialog"
            )
        }
    }
}