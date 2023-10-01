package com.mdn.coffeeandhappiness.fragments.menufragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.adapter.FoodRecyclerViewAdapter
import com.mdn.coffeeandhappiness.controller.FoodController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuSaladsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_menu_salads, container, false)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.menuSaladsRecyclerView)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)


        // Use lifecycleScope.launch to call getFood asynchronously
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val listOfFood = FoodController().getFood("salad")

                // Update the UI on the main thread
                launch(Dispatchers.Main) {
                    val adapter =
                        FoodRecyclerViewAdapter(
                            requireContext(),
                            listOfFood
                        ) // Provide your data here
                    adapter.notifyDataSetChanged()
                    recyclerView.adapter = adapter
                }
            } catch (e: NoInternetException) {
                launch(Dispatchers.Main) {
                    val noInternet = rootView.findViewById<LinearLayout>(R.id.menuSaladsNoInternet)
                    noInternet.visibility = View.VISIBLE
                }
            }
        }

        return rootView
    }

}