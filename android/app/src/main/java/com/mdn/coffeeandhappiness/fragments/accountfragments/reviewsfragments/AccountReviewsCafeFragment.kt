package com.mdn.coffeeandhappiness.fragments.accountfragments.reviewsfragments

import android.content.Context
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
import com.mdn.coffeeandhappiness.adapter.AccountReviewsCafeAdapter
import com.mdn.coffeeandhappiness.adapter.AccountReviewsFoodAdapter
import com.mdn.coffeeandhappiness.controller.ReviewController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountReviewsCafeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountReviewsCafeFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_account_reviews_cafe, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.accountReviewsCafeRecyclerView)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)


        // Use lifecycleScope.launch to call getFood asynchronously
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val listOfCafeReviews = ReviewController().getCafeReviewsWithCafe(
                    requireContext().getSharedPreferences(
                        "Account",
                        Context.MODE_PRIVATE
                    )
                )

                // Update the UI on the main thread
                launch(Dispatchers.Main) {
                    if (listOfCafeReviews.size == 0) {
                        val empty = view.findViewById<LinearLayout>(R.id.accountReviewsCafeEmpty)
                        empty.visibility = View.VISIBLE
                    }

                    val adapter = AccountReviewsCafeAdapter(
                        requireContext(),
                        listOfCafeReviews
                    ) // Provide your data here
                    recyclerView.adapter = adapter
                }
            } catch (e: NoInternetException) {
                launch (Dispatchers.Main) {
                    val disconnected = view.findViewById<LinearLayout>(R.id.accountReviewsCafeNoInternet)
                    disconnected.visibility = View.VISIBLE
                }
            }
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountReviewsCafeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountReviewsCafeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}