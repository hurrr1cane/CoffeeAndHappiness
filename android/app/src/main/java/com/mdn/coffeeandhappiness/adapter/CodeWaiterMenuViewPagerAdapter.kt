package com.mdn.coffeeandhappiness.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mdn.coffeeandhappiness.fragments.codefragments.menufragments.CodeWaiterMenuCoffeeFragment
import com.mdn.coffeeandhappiness.fragments.codefragments.menufragments.CodeWaiterMenuDessertsFragment
import com.mdn.coffeeandhappiness.fragments.codefragments.menufragments.CodeWaiterMenuDrinksFragment
import com.mdn.coffeeandhappiness.fragments.codefragments.menufragments.CodeWaiterMenuIceCreamFragment
import com.mdn.coffeeandhappiness.fragments.codefragments.menufragments.CodeWaiterMenuMainFragment
import com.mdn.coffeeandhappiness.fragments.codefragments.menufragments.CodeWaiterMenuSaladsFragment
import com.mdn.coffeeandhappiness.model.Food

internal class CodeWaiterMenuViewPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int,
    var listOfFood: MutableList<Food>,
    var adapter: CodeWaiterChosenFoodRecyclerViewAdapter
) :
    FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> CodeWaiterMenuMainFragment(listOfFood, adapter)
            1 -> CodeWaiterMenuDrinksFragment(listOfFood, adapter)
            2 -> CodeWaiterMenuCoffeeFragment(listOfFood, adapter)
            3 -> CodeWaiterMenuSaladsFragment(listOfFood, adapter)
            4 -> CodeWaiterMenuDessertsFragment(listOfFood, adapter)
            5 -> CodeWaiterMenuIceCreamFragment(listOfFood, adapter)
            else -> CodeWaiterMenuMainFragment(listOfFood, adapter)
        }
    }
}