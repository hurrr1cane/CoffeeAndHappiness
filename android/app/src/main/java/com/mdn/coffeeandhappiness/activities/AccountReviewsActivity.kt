package com.mdn.coffeeandhappiness.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.adapter.AccountReviewsAdapter
import java.util.Locale

class AccountReviewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_account_reviews)

        val menuTabs = findViewById<TabLayout>(R.id.accountReviewsTabs)
        val viewPager = findViewById<ViewPager>(R.id.accountReviewsViewPager)

        val adapter = AccountReviewsAdapter(this, supportFragmentManager, menuTabs.tabCount)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(menuTabs))
        menuTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

        })


        val backButton = findViewById<ImageButton>(R.id.accountReviewsActivityBackButton)
        backButton.setOnClickListener() {
            finish()
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