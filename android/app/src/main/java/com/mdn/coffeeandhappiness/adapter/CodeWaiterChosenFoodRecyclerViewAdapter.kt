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


class CodeWaiterChosenFoodRecyclerViewAdapter(
    private val context: Context,
    private val foodList: MutableList<Food>
) : RecyclerView.Adapter<CodeWaiterChosenFoodRecyclerViewAdapter.FoodHolder>() {

    class FoodHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardImageView = itemView.findViewById<ImageView>(R.id.codeWaiterChosenFoodCardImageView)
        val cardTitleTextView =
            itemView.findViewById<TextView>(R.id.codeWaiterChosenFoodCardTitleTextView)
        val deleteButton =
            itemView.findViewById<ImageButton>(R.id.codeWaiterChosenFoodCardDeleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.dynamic_card_code_waiter_chosen_food,
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
            .error(R.drawable.food_icon)
            .into(holder.cardImageView)


        var titleText = ""

        when (language) {
            "uk" -> {
                titleText = foodList[position].nameUA
            }

            "en" -> {
                titleText = foodList[position].nameEN
            }
        }

        holder.cardTitleTextView.text = titleText



        holder.deleteButton.setOnClickListener() {
            foodList.removeAt(position)
            notifyDataSetChanged()
        }
    }


}