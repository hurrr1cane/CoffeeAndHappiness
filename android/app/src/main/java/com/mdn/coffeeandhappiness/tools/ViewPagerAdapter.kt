package com.mdn.coffeeandhappiness.tools

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mdn.coffeeandhappiness.fragments.menufragments.MenuCoffeeFragment
import com.mdn.coffeeandhappiness.fragments.menufragments.MenuDessertsFragment
import com.mdn.coffeeandhappiness.fragments.menufragments.MenuDrinksFragment
import com.mdn.coffeeandhappiness.fragments.menufragments.MenuIceCreamFragment
import com.mdn.coffeeandhappiness.fragments.menufragments.MenuMainFragment
import com.mdn.coffeeandhappiness.fragments.menufragments.MenuSaladsFragment

internal class ViewPagerAdapter(var context: Context, fm: FragmentManager, var totalTabs: Int): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MenuMainFragment()
            1 -> MenuDrinksFragment()
            2 -> MenuCoffeeFragment()
            3 -> MenuSaladsFragment()
            4 -> MenuDessertsFragment()
            5 -> MenuIceCreamFragment()
            else -> MenuMainFragment()
        }
    }
}