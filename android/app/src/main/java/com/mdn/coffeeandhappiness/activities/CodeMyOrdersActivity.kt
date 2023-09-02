package com.mdn.coffeeandhappiness.activities

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.adapter.CodeMyOrdersOrdersRecyclerViewAdapter
import com.mdn.coffeeandhappiness.controller.OrderController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class CodeMyOrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_code_my_orders)


        val backButton = findViewById<ImageButton>(R.id.codeMyOrdersActivityBackButton)
        backButton.setOnClickListener() {
            finish()
        }

        val context = this

        val recyclerView = findViewById<RecyclerView>(R.id.codeMyOrdersActivityRecyclerView)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)


        // Use lifecycleScope.launch to call getFood asynchronously
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val listOfOrders = OrderController().getMyOrders(
                    getSharedPreferences(
                        "Account",
                        Context.MODE_PRIVATE
                    )
                )

                // Update the UI on the main thread
                launch(Dispatchers.Main) {

                    checkEmpty(listOfOrders)
                    val adapter =
                        CodeMyOrdersOrdersRecyclerViewAdapter(
                            context,
                            listOfOrders
                        ) // Provide your data here
                    recyclerView.adapter = adapter
                }
            } catch (e: NoInternetException) {
                launch(Dispatchers.Main) {
                    val noConnection = findViewById<LinearLayout>(R.id.codeMyOrdersActivityNoInternet)
                    noConnection.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun checkEmpty(
        listOfFood: MutableList<Order>
    ) {
        val empty = findViewById<LinearLayout>(R.id.codeMyOrdersActivityEmpty)
        if (listOfFood.size == 0) {
            empty.visibility = View.VISIBLE
        } else {
            empty.visibility = View.GONE
        }
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