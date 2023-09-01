package com.mdn.coffeeandhappiness.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.model.Food

class CodeWaiterFoodRecyclerViewAdapter(
    private val context: Context,
    private val foodList: MutableList<Food>,
    private val foodToPlace: MutableList<Food>,
    var adapter: CodeWaiterChosenFoodRecyclerViewAdapter
) : RecyclerView.Adapter<CodeWaiterFoodRecyclerViewAdapter.FoodHolder>() {

    class FoodHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardImageView = itemView.findViewById<ImageView>(R.id.codeWaiterFoodCardImageView)
        val cardTitleTextView =
            itemView.findViewById<TextView>(R.id.codeWaiterFoodCardTitleTextView)
        val cardDescriptionTextView =
            itemView.findViewById<TextView>(R.id.codeWaiterFoodCardDescriptionTextView)
        val cardPriceTextView = itemView.findViewById<TextView>(R.id.codeWaiterFoodCardPrice)
        val addButton = itemView.findViewById<ImageButton>(R.id.codeWaiterFoodCardAddButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.dynamic_card_code_waiter_food,
            parent, false
        )
        return FoodHolder(itemView)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {

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
        val price = String.format("%.2f", foodList[position].price) + "₴"
        holder.cardPriceTextView.text = price



        holder.addButton.setOnClickListener() {
            foodToPlace.add(foodList[position])
            adapter.notifyDataSetChanged()
        }
    }

}