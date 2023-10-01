package com.mdn.coffeeandhappiness.fragments.mapfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.adapter.CafeRecyclerViewAdapter
import com.mdn.coffeeandhappiness.controller.CafeController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map_list, container, false)

        val changeToMap = view.findViewById<ImageButton>(R.id.mapChangeToMap)

        changeToMap.setOnClickListener {
            val mapFragment = MapMapFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.mapFrame, mapFragment)
            transaction.addToBackStack(null) // Optional: Add to back stack for navigation
            transaction.commit()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.mapListRecyclerView)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)


        // Use lifecycleScope.launch to call getFood asynchronously
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val listOfCafe = CafeController().getCafe()

                // Update the UI on the main thread
                launch(Dispatchers.Main) {
                    val adapter =
                        CafeRecyclerViewAdapter(
                            requireContext(),
                            listOfCafe
                        ) // Provide your data here
                    recyclerView.adapter = adapter
                }
            } catch (e: NoInternetException) {
                launch(Dispatchers.Main) {
                    val noInternet = view.findViewById<LinearLayout>(R.id.mapListNoInternet)
                    noInternet.visibility = View.VISIBLE
                }
            }
        }

        return view
    }
}