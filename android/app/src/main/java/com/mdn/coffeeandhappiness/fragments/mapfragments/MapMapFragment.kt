package com.mdn.coffeeandhappiness.fragments.mapfragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.activities.CafeActivity
import com.mdn.coffeeandhappiness.controller.CafeController
import com.mdn.coffeeandhappiness.fragments.accountfragments.AccountSignupFragment
import com.mdn.coffeeandhappiness.tools.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        val sharedPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("Language", "uk")

        val nightMode = sharedPreferences.getBoolean("Night", true)

        mapView = view.findViewById<MapView>(R.id.mapMapView)
        mapView.onCreate(savedInstanceState) // Make sure to handle savedInstanceState
        mapView.getMapAsync { googleMap ->
            lifecycleScope.launch(Dispatchers.IO) {
                val listOfCafes = CafeController().getCafe()
                launch(Dispatchers.Main) {
                    for (cafe in listOfCafes) {
                        val cafeLatLng = LatLng(cafe.latitude, cafe.longitude)

                        val cafeName = when(language) {
                            "uk" -> cafe.locationUA
                            "en" -> cafe.locationEN
                            else -> cafe.locationUA
                        }

                        val customMarkerIcon = BitmapDescriptorFactory.fromResource(R.drawable.map_marker)
                        val marker = googleMap.addMarker(MarkerOptions().position(cafeLatLng).title(cafeName).icon(customMarkerIcon))
                        marker!!.tag = cafe.id
                    }

                    googleMap.setOnMarkerClickListener { marker ->
                        val cafeId = marker.tag as Int // Retrieve the cafe's ID

                        val intent = Intent(requireContext(), CafeActivity::class.java)
                        intent.putExtra(
                            "cafe_id",
                            cafeId
                        ) // Pass any data you need to the next activity
                        requireContext().startActivity(intent)

                        true // Return true to consume the click event
                    }
                }
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Constants().lvivAddress, 13f))

            // Load the appropriate map style based on nightMode
            val mapStyleResourceId = if (nightMode) R.raw.dark_map_style else R.raw.light_map_style
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), mapStyleResourceId))
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