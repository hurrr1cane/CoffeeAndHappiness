package com.mdn.coffeeandhappiness.fragments

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.VideoView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.adapter.HomeNewsRecyclerViewAdapter
import com.mdn.coffeeandhappiness.controller.NewsController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.model.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var videoView: VideoView

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
        val rootView = setVideo(inflater, container)
        //setNewsSection(rootView!!)

        val recyclerView = rootView!!.findViewById<RecyclerView>(R.id.homeNewsRecyclerView)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)


        // Use lifecycleScope.launch to call getFood asynchronously
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val listOfNews = NewsController().getNews()


                // Update the UI on the main thread
                launch(Dispatchers.Main) {

                    val adapter =
                        HomeNewsRecyclerViewAdapter(
                            requireContext(),
                            listOfNews
                        ) // Provide your data here
                    recyclerView.adapter = adapter
                }
            } catch (e: NoInternetException) {
                launch (Dispatchers.Main) {
                    noInternetConnection(rootView)
                }
            }
        }

        return rootView
    }

    private fun noInternetConnection(
        rootView: View
    ) {
        val noInternet = rootView.findViewById<LinearLayout>(R.id.homeNoInternet)
            noInternet.visibility = View.VISIBLE

    }

    private fun setVideo(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        videoView = rootView.findViewById<VideoView>(R.id.homeBackgroundVideo)
        val uri =
            Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.cafe_video)
        videoView.setVideoURI(uri)
        videoView.start()

        videoView.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(mp: MediaPlayer) {
                // The video is prepared, you can start playback here if needed
                mp.isLooping = true
            }
        })
        return rootView
    }

    /*private fun setNewsSection(rootView: View) {
        val newsLayout = rootView.findViewById<LinearLayout>(R.id.homeNewsLayout)
        val listOfNews = News().getNews()

        for (singleNew in listOfNews) {
            val cardView = layoutInflater.inflate(R.layout.dynamic_card_view, null) as CardView
            val cardImageView = cardView.findViewById<ImageView>(R.id.cardImageView)
            val cardTitleTextView = cardView.findViewById<TextView>(R.id.cardTitleTextView)
            val cardDateTextView = cardView.findViewById<TextView>(R.id.cardDateTextView)

            val sharedPreferences = context?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            val language = sharedPreferences?.getString("Language", "uk")

            val resourceName =
                singleNew.photo // Replace with the name of your raw resource without file extension
            val packageName = context?.packageName // Get the package name of your app's context

            val resourceId = context?.resources?.getIdentifier(resourceName, "raw", packageName)

            // Customize the card components as needed
            // Load the image from the raw folder and set it to the ImageView
            val imageResource =
                context?.resources?.openRawResource(resourceId!!) // Assuming it's a raw resource
            val bitmap = BitmapFactory.decodeStream(imageResource)
            cardImageView.setImageBitmap(bitmap)

            val text = when (language) {
                "uk" -> singleNew.nameUk
                "en" -> singleNew.nameEn
                else -> {
                    "No"
                }
            }
            cardTitleTextView.text = text // Set your title text
            cardDateTextView.text = singleNew.date // Set your date text

            // Set margins if needed
            val layoutParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.card_width),
                resources.getDimensionPixelSize(R.dimen.card_height)
            )
            val margin = resources.getDimensionPixelSize(R.dimen.card_margin)
            layoutParams.setMargins(margin, margin, margin, margin) // Set margins if needed
            cardView.layoutParams = layoutParams

            newsLayout.addView(cardView)
        }
    }*/

    override fun onResume() {
        super.onResume()
        // to restart the video after coming from other activity like Sing up
        videoView.start();

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}