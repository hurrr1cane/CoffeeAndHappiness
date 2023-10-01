package com.mdn.coffeeandhappiness.fragments.mapfragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
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
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.tools.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapMapFragment : Fragment() {

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
                try {
                    val listOfCafes = CafeController().getCafe()
                    launch(Dispatchers.Main) {
                        for (cafe in listOfCafes) {
                            val cafeLatLng = LatLng(cafe.latitude, cafe.longitude)

                            val cafeName = when (language) {
                                "uk" -> cafe.locationUA
                                "en" -> cafe.locationEN
                                else -> cafe.locationUA
                            }

                            val customMarkerIcon =
                                BitmapDescriptorFactory.fromResource(R.drawable.map_marker)
                            val marker = googleMap.addMarker(
                                MarkerOptions().position(cafeLatLng).title(cafeName)
                                    .icon(customMarkerIcon)
                            )
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
                } catch (e: NoInternetException) {
                    launch(Dispatchers.Main) {
                        mapView.visibility = View.GONE

                        val noInternet = view.findViewById<LinearLayout>(R.id.mapMapNoInternet)
                        noInternet.visibility = View.VISIBLE
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

}