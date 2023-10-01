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
import com.mdn.coffeeandhappiness.adapter.AccountReviewsFoodAdapter
import com.mdn.coffeeandhappiness.controller.ReviewController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountReviewsFoodFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account_reviews_food, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.accountReviewsFoodRecyclerView)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)


        // Use lifecycleScope.launch to call getFood asynchronously
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val listOfFoodReviews = ReviewController().getFoodReviewsWithFood(
                    requireContext().getSharedPreferences(
                        "Account",
                        Context.MODE_PRIVATE
                    )
                )

                println(listOfFoodReviews)
                // Update the UI on the main thread
                launch(Dispatchers.Main) {
                    if (listOfFoodReviews.size == 0) {
                        val empty = view.findViewById<LinearLayout>(R.id.accountReviewsFoodEmpty)
                        empty.visibility = View.VISIBLE
                    }

                    val adapter = AccountReviewsFoodAdapter(
                        requireContext(),
                        listOfFoodReviews
                    ) // Provide your data here
                    recyclerView.adapter = adapter
                }
            } catch (e: NoInternetException) {
                launch(Dispatchers.Main) {
                    val disconnected =
                        view.findViewById<LinearLayout>(R.id.accountReviewsFoodNoInternet)
                    disconnected.visibility = View.VISIBLE
                }
            }
        }

        return view
    }
}