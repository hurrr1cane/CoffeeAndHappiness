package com.mdn.coffeeandhappiness.activities

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.adapter.AccountFAQRecyclerViewAdapter
import com.mdn.coffeeandhappiness.adapter.CodeMyOrdersOrdersRecyclerViewAdapter
import com.mdn.coffeeandhappiness.controller.OrderController
import com.mdn.coffeeandhappiness.tools.QuestionsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class AccountFAQActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_account_faq_activity)

        val backButton = findViewById<ImageButton>(R.id.accountFAQActivityBackButton)
        backButton.setOnClickListener() {
            finish()
        }

        val context = this

        val recyclerView = findViewById<RecyclerView>(R.id.accountFAQActivityRecyclerView)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)


        val listOfQuestions = QuestionsList().getListOfQuestions()

        val adapter =
            AccountFAQRecyclerViewAdapter(
                context,
                listOfQuestions
            ) // Provide your data here
        recyclerView.adapter = adapter


    }

    private fun setLanguage() {
        val languagePreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        var languageToSet = languagePreferences.getString("Language", "uk")

        var locale = Locale(languageToSet)
        Locale.setDefault(locale)
        var configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        baseContext.resources.updateConfiguration(
            configuration,
            baseContext.resources.displayMetrics
        )
    }
}