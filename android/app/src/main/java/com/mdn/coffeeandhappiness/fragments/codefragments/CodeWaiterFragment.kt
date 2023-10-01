package com.mdn.coffeeandhappiness.fragments.codefragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.activities.CodeWaiterPlaceOrderActivity
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CodeWaiterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_code_waiter, container, false)

        val placeOrder = view.findViewById<AppCompatButton>(R.id.codeWaiterPlaceOrderButton)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                AccountController().updateMyself(
                    requireContext().getSharedPreferences(
                        "Account",
                        Context.MODE_PRIVATE
                    )
                )
            } catch (e: NoInternetException) {
                launch(Dispatchers.Main) {
                    val noInternet = view.findViewById<TextView>(R.id.codeWaiterNoInternet)
                    noInternet.visibility = View.VISIBLE
                    placeOrder.isEnabled = false
                }
            }
        }

        placeOrder.setOnClickListener() {
            val intent = Intent(context, CodeWaiterPlaceOrderActivity::class.java)
            requireContext().startActivity(intent)
        }

        return view
    }

}