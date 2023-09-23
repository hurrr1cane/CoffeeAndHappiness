package com.mdn.coffeeandhappiness.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.model.Question


class AccountFAQRecyclerViewAdapter(
    private val context: Context,
    private val questionList: MutableList<Question>
) : RecyclerView.Adapter<AccountFAQRecyclerViewAdapter.QuestionHolder>() {

    class QuestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.findViewById<CardView>(R.id.cardAccountFaqQuestionCard)
        val title = itemView.findViewById<TextView>(R.id.cardAccountFaqQuestionTitle)
        val answer = itemView.findViewById<TextView>(R.id.cardAccountFaqQuestionAnswer)
        val arrow = itemView.findViewById<ImageView>(R.id.cardAccountFaqQuestionArrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.dynamic_card_account_faq_question,
            parent, false
        )
        return QuestionHolder(itemView)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun onBindViewHolder(holder: QuestionHolder, position: Int) {

        val inflater = LayoutInflater.from(context)
        val cardView =
            inflater.inflate(R.layout.dynamic_card_account_faq_question, null) as CardView

        val settingsPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = settingsPreferences?.getString("Language", "uk")

        val mode = settingsPreferences!!.getBoolean("Night", true)

        holder.arrow.setImageResource(R.drawable.baseline_keyboard_arrow_down_white_24)
        if (mode) {
            ImageViewCompat.setImageTintList(holder.arrow, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white_text)))
        }
        else {
            ImageViewCompat.setImageTintList(holder.arrow, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black_text)))
        }

        var question: String = ""
        var answer: String = ""


        when (language) {
            "uk" -> {
                question = questionList[position].titleUk
                answer = questionList[position].answerUk
            }

            "en" -> {
                question = questionList[position].titleEn
                answer = questionList[position].answerEn
            }
        }
        holder.title.text = question
        holder.answer.text = answer


        // Set margins if needed
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        cardView.layoutParams = layoutParams




        holder.card.setOnClickListener() {
            if (holder.answer.visibility == View.GONE) {
                holder.answer.visibility = View.VISIBLE
                holder.arrow.setImageResource(R.drawable.baseline_keyboard_arrow_up_white_24)
            } else if (holder.answer.visibility == View.VISIBLE) {
                holder.answer.visibility = View.GONE
                holder.arrow.setImageResource(R.drawable.baseline_keyboard_arrow_down_white_24)
            }

        }
    }

}