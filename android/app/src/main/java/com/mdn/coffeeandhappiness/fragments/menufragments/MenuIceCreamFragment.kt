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
import com.mdn.coffeeandhappiness.model.Food
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuIceCreamFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuIceCreamFragment : Fragment() {
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
        val rootView = inflater.inflate(R.layout.fragment_menu_ice_cream, container, false)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.menuIceCreamRecyclerView)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)


        // Use lifecycleScope.launch to call getFood asynchronously
        lifecycleScope.launch(Dispatchers.IO) {
            val listOfFood = FoodController().getFood("ice_cream")

            // Update the UI on the main thread
            launch(Dispatchers.Main) {

                noInternetConnection(rootView, listOfFood)

                val adapter =
                    FoodRecyclerViewAdapter(requireContext(), listOfFood) // Provide your data here
                recyclerView.adapter = adapter
            }
        }

        return rootView
    }

    private fun noInternetConnection(
        rootView: View,
        listOfFood: MutableList<Food>
    ) {
        val noInternet = rootView.findViewById<LinearLayout>(R.id.menuIceCreamNoInternet)
        if (listOfFood.size == 0) {
            noInternet.visibility = View.VISIBLE
        } else {
            noInternet.visibility = View.GONE
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuIceCreamFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuIceCreamFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}