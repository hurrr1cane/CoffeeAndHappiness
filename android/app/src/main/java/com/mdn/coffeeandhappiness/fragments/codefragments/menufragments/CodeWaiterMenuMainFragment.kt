package com.mdn.coffeeandhappiness.fragments.codefragments.menufragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.adapter.CodeWaiterChosenFoodRecyclerViewAdapter
import com.mdn.coffeeandhappiness.adapter.CodeWaiterFoodRecyclerViewAdapter
import com.mdn.coffeeandhappiness.controller.FoodController
import com.mdn.coffeeandhappiness.model.Food
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CodeWaiterMenuMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CodeWaiterMenuMainFragment(var listOfFood: MutableList<Food>, var adapter: CodeWaiterChosenFoodRecyclerViewAdapter) : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_code_waiter_menu_main, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.codeWaiterMenuMainRecyclerView)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)


        // Use lifecycleScope.launch to call getFood asynchronously
        lifecycleScope.launch(Dispatchers.IO) {
            val foodInMenu = FoodController().getFood("main")

            // Update the UI on the main thread
            launch(Dispatchers.Main) {
                val adapter =
                    CodeWaiterFoodRecyclerViewAdapter(requireContext(), foodInMenu, listOfFood, adapter) // Provide your data here
                recyclerView.adapter = adapter
            }
        }

        return view
    }

}