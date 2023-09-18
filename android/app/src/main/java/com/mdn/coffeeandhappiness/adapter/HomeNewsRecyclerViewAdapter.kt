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
import com.mdn.coffeeandhappiness.activities.HomeNewsActivity
import com.mdn.coffeeandhappiness.model.News
import com.mdn.coffeeandhappiness.tools.DateConverter

class HomeNewsRecyclerViewAdapter(
    private val context: Context,
    private val listOfNews: MutableList<News>
) : RecyclerView.Adapter<HomeNewsRecyclerViewAdapter.NewsHolder>() {

    class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.homeNewsCardView)
        val title = itemView.findViewById<TextView>(R.id.homeNewsTitleTextView)
        val date = itemView.findViewById<TextView>(R.id.homeNewsDateTextView)
        val cardImageView = itemView.findViewById<ImageView>(R.id.homeNewsImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.dynamic_card_news,
            parent, false
        )
        return NewsHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listOfNews.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {

        val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("Language", "uk")

        Glide.with(context)
            .load(listOfNews[position].imageUrl)
            .into(holder.cardImageView)


        var titleText = ""

        when (language) {
            "uk" -> {
                titleText = listOfNews[position].titleUA
            }

            "en" -> {
                titleText = listOfNews[position].titleEN
            }
        }

        holder.title.text = titleText

        holder.date.text = DateConverter().formatDate(listOfNews[position].publishedAt)


        holder.cardView.setOnClickListener() {
            // Handle the click event here, for example, open a new activity with details
            val intent = Intent(context, HomeNewsActivity::class.java)
            intent.putExtra(
                "news_id",
                listOfNews[position].id
            ) // Pass any data you need to the next activity
            context.startActivity(intent)
        }
    }
}