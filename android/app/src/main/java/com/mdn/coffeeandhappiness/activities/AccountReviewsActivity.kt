package com.mdn.coffeeandhappiness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.tools.AccountReviewsAdapter
import com.mdn.coffeeandhappiness.tools.ViewPagerAdapter

class AccountReviewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_reviews)

        val menuTabs = findViewById<TabLayout>(R.id.accountReviewsTabs)
        val viewPager = findViewById<ViewPager>(R.id.accountReviewsViewPager)

        val adapter = AccountReviewsAdapter(this, supportFragmentManager, menuTabs.tabCount)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(menuTabs))
        menuTabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
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
}