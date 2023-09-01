package com.mdn.coffeeandhappiness.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.model.Order
import com.mdn.coffeeandhappiness.tools.DateConverter

class  CodeMyOrdersOrdersRecyclerViewAdapter(
    private val context: Context,
    private val orderList: MutableList<Order>
) : RecyclerView.Adapter<CodeMyOrdersOrdersRecyclerViewAdapter.OrderHolder>() {

    class OrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date = itemView.findViewById<TextView>(R.id.cardCodeOrderDate)
        val pointsGained = itemView.findViewById<TextView>(R.id.cardCodeOrderPoints)
        val totalPrice = itemView.findViewById<TextView>(R.id.cardCodeOrderPrice)
        val foodContainer = itemView.findViewById<LinearLayout>(R.id.cardCodeOrderFoodContainer)
        val card = itemView.findViewById<CardView>(R.id.cardCodeOrderCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.dynamic_card_code_order,
            parent, false
        )
        return OrderHolder(itemView)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {

        holder.date.text = DateConverter().formatDate(orderList[position].orderDate)
        holder.pointsGained.text = orderList[position].bonusPointsEarned.toString()

        if (orderList[position].bonusPointsEarned > 0) {
            val color = ContextCompat.getColor(context, R.color.default_green)
            holder.pointsGained.setTextColor(color)
        } else {
            val color = ContextCompat.getColor(context, R.color.default_red)
            holder.pointsGained.setTextColor(color)
        }


        for (singleFood in orderList[position].foods) {
            val inflater = LayoutInflater.from(context)
            val cardView = inflater.inflate(R.layout.dynamic_card_code_orders_food, null) as CardView

            val cardImageView = cardView.findViewById<ImageView>(R.id.codeOrdersFoodCardImageView)
            val cardTitleTextView = cardView.findViewById<TextView>(R.id.codeOrdersFoodCardTitleTextView)
            val cardPriceTextView = cardView.findViewById<TextView>(R.id.codeOrdersFoodCardPrice)

            val settingsPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            val language = settingsPreferences?.getString("Language", "uk")

            Glide.with(context)
                .load(singleFood.imageUrl)
                .placeholder(R.drawable.default_placeholder_image)
                .error(R.drawable.default_error_image)
                .into(cardImageView)

            val text = when (language) {
                "uk" -> singleFood.nameUA
                "en" -> singleFood.nameEN
                else -> {
                    "No"
                }
            }
            cardTitleTextView.text = text // Set your title text

            val price = String.format("%.2f", singleFood.price) + "₴"
            cardPriceTextView.text = price

            // Set margins if needed
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            cardView.layoutParams = layoutParams

            holder.foodContainer.addView(cardView)
        }

        val price = String.format("%.2f", orderList[position].totalPrice) + "₴"
        holder.totalPrice.text = price



        holder.card.setOnClickListener() {
            if (holder.foodContainer.visibility == View.GONE) {
                holder.foodContainer.visibility = View.VISIBLE
            }
            else if (holder.foodContainer.visibility == View.VISIBLE) {
                holder.foodContainer.visibility = View.GONE
            }

        }
    }

}