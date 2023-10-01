package com.mdn.coffeeandhappiness.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.fragments.codefragments.CodeWaiterFragment
import com.mdn.coffeeandhappiness.fragments.codefragments.CodeUnloggedFragment
import com.mdn.coffeeandhappiness.fragments.codefragments.CodeUserFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CodeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_code, container, false)

        val accountController = AccountController()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                accountController.updateMyself(
                    requireContext().getSharedPreferences(
                        "Account",
                        Context.MODE_PRIVATE
                    )
                )
            } catch (e: NoInternetException) {
            }
        }

        val sharedPreferences =
            requireContext().getSharedPreferences("Account", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("IsAccountLogged", false)) {
            if (sharedPreferences.getString("Role", "USER").equals("USER")) {
                replaceFragment(CodeUserFragment())
            } else if (sharedPreferences.getString("Role", "USER").equals("WAITER")) {
                replaceFragment(CodeWaiterFragment())
            }
        } else {
            replaceFragment(CodeUnloggedFragment())
        }



        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.codeFrame, fragment)
        fragmentTransaction.commit()
    }
}