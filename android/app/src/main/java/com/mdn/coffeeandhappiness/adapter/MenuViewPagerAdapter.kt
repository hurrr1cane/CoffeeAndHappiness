package com.mdn.coffeeandhappiness.adapter

import android.content.Context
import androidx.fragment.app.Fragment
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

internal class MenuViewPagerAdapter(fragmentManager: FragmentManager,
                                    lifecycle: Lifecycle,
                                    var totalTabs: Int) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
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