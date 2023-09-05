package com.mdn.coffeeandhappiness.fragments.mapfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.fragments.accountfragments.AccountSignupFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MapMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapMapFragment : Fragment() {
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

    private lateinit var mapView:MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map_map, container, false)

        val changeToList = view.findViewById<ImageButton>(R.id.mapChangeToList)

        changeToList.setOnClickListener {
            val listFragment = MapListFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.mapFrame, listFragment)
            transaction.addToBackStack(null) // Optional: Add to back stack for navigation
            transaction.commit()
        }

        mapView = view.findViewById<MapView>(R.id.mapMapView)
        mapView.onCreate(savedInstanceState) // Make sure to handle savedInstanceState
        mapView.getMapAsync { googleMap ->
            // Initialize GoogleMap here
            val cafeLatLng = LatLng(49.82160411211895, 23.9741344635085) // Replace with the cafe's coordinates
            val marker = googleMap.addMarker(MarkerOptions().position(cafeLatLng).title("Cafe Name"))
            marker!!.tag = 1 // You can set a tag to identify the cafe
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cafeLatLng, 14f)) // Adjust zoom level as needed
        }

        return view
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapMapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapMapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}