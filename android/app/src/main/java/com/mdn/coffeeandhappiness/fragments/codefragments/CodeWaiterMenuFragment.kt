package com.mdn.coffeeandhappiness.fragments.codefragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.adapter.CodeWaiterChosenFoodRecyclerViewAdapter
import com.mdn.coffeeandhappiness.adapter.CodeWaiterMenuViewPagerAdapter
import com.mdn.coffeeandhappiness.model.Food

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CodeWaiterMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CodeWaiterMenuFragment(
    var foodToPlace: MutableList<Food>,
    var adapter: CodeWaiterChosenFoodRecyclerViewAdapter
) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_code_waiter_menu, container, false)

        val menuTabs = view.findViewById<TabLayout>(R.id.codeWaiterPlaceOrderMenuTabLayout)
        val viewPager = view.findViewById<ViewPager>(R.id.codeWaiterPlaceOrderMenuViewPager)

        val adapter =
            CodeWaiterMenuViewPagerAdapter(
                requireContext(),
                requireFragmentManager(),
                menuTabs.tabCount,
                foodToPlace,
                adapter
            )
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

        return view
    }


}