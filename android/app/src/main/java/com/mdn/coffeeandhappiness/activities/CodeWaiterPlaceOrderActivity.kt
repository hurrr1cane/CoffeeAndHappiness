package com.mdn.coffeeandhappiness.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.adapter.CodeWaiterChosenFoodRecyclerViewAdapter
import com.mdn.coffeeandhappiness.fragments.codefragments.CodeWaiterMenuFragment
import com.mdn.coffeeandhappiness.fragments.codefragments.CodeWaiterScanFragment
import com.mdn.coffeeandhappiness.model.Food
import java.util.Locale

class CodeWaiterPlaceOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setLanguage()
        setContentView(R.layout.activity_code_waiter_place_order)


        val sheet = findViewById<FrameLayout>(R.id.codeWaiterPlaceOrderActivityBottomSheet)

        BottomSheetBehavior.from(sheet).apply {
            peekHeight = 200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val backButton = findViewById<ImageButton>(R.id.codeWaiterPlaceOrderActivityBackButton)
        backButton.setOnClickListener() {
            finish()
        }


        val foodToPlace = mutableListOf<Food>()

        val chosenFoodRecyclerView =
            findViewById<RecyclerView>(R.id.codeWaiterPlaceOrderActivityBottomSheetRecyclerView)

        val layoutManager = LinearLayoutManager(this)
        chosenFoodRecyclerView.layoutManager = layoutManager
        chosenFoodRecyclerView.setHasFixedSize(true)

        val adapter =
            CodeWaiterChosenFoodRecyclerViewAdapter(
                this,
                foodToPlace

            ) // Provide your data here
        chosenFoodRecyclerView.adapter = adapter

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(
            R.id.codeWaiterPlaceOrderFrame,
            CodeWaiterMenuFragment(foodToPlace, adapter)
        )
        fragmentTransaction.commit()


        val placeOrderButton =
            findViewById<AppCompatButton>(R.id.codeWaiterPlaceOrderPlaceOrderButton)
        placeOrderButton.setOnClickListener() {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(
                R.id.codeWaiterPlaceOrderFrame,
                CodeWaiterScanFragment(foodToPlace)
            )
            fragmentTransaction.commit()
            sheet.visibility = View.GONE

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
