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
import com.mdn.coffeeandhappiness.model.Food

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuMainFragment : Fragment() {
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
        val rootView = inflater.inflate(R.layout.fragment_menu_main, container, false)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.menuMainRecyclerView)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)


        // Use lifecycleScope.launch to call getFood asynchronously
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val listOfFood = FoodController().getFood("main")

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
                    val noInternet = rootView.findViewById<LinearLayout>(R.id.menuMainNoInternet)
                    noInternet.visibility = View.VISIBLE
                }
            }
        }

        return rootView
    }



}