package com.mdn.coffeeandhappiness.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mdn.coffeeandhappiness.fragments.accountfragments.reviewsfragments.AccountReviewsCafeFragment
import com.mdn.coffeeandhappiness.fragments.accountfragments.reviewsfragments.AccountReviewsFoodFragment

internal class AccountReviewsAdapter(var context: Context, fm: FragmentManager, var totalTabs: Int): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AccountReviewsFoodFragment()
            1 -> AccountReviewsCafeFragment()
            else -> AccountReviewsFoodFragment()
        }
    }
}