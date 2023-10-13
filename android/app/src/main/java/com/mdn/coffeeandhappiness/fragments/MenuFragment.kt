package com.mdn.coffeeandhappiness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.adapter.MenuViewPagerAdapter

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_menu, container, false)

        val menuTabs = rootView.findViewById<TabLayout>(R.id.menuTabs)
        val viewPager = rootView.findViewById<ViewPager2>(R.id.menu_viewPager)

        val adapter =
            MenuViewPagerAdapter(
                childFragmentManager,
                viewLifecycleOwner.lifecycle,
                menuTabs.tabCount
            )
        viewPager.adapter = adapter

        //viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(menuTabs))
        menuTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

        })

        // Add a ViewPager2 listener to update selected tab when pages are swiped
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                menuTabs.selectTab(menuTabs.getTabAt(position))
            }
        })

        return rootView
    }

}