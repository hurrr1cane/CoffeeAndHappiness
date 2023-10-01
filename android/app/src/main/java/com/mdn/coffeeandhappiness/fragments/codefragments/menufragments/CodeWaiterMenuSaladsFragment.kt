package com.mdn.coffeeandhappiness.fragments.codefragments.menufragments

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
import com.mdn.coffeeandhappiness.adapter.CodeWaiterChosenFoodRecyclerViewAdapter
import com.mdn.coffeeandhappiness.adapter.CodeWaiterFoodRecyclerViewAdapter
import com.mdn.coffeeandhappiness.controller.FoodController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.model.Food
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CodeWaiterMenuSaladsFragment(
    var listOfFood: MutableList<Food>,
    var adapter: CodeWaiterChosenFoodRecyclerViewAdapter
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_code_waiter_menu_salads, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.codeWaiterMenuSaladsRecyclerView)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)


        // Use lifecycleScope.launch to call getFood asynchronously
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val foodInMenu = FoodController().getFood("salad")

                // Update the UI on the main thread
                launch(Dispatchers.Main) {
                    val adapter =
                        CodeWaiterFoodRecyclerViewAdapter(
                            requireContext(),
                            foodInMenu,
                            listOfFood,
                            adapter
                        ) // Provide your data here
                    recyclerView.adapter = adapter
                }
            } catch (e: NoInternetException) {
                launch(Dispatchers.Main) {
                    val noConnection =
                        view.findViewById<LinearLayout>(R.id.codeWaiterMenuSaladsNoInternet)
                    noConnection.visibility = View.VISIBLE
                }
            }
        }

        return view
    }

}