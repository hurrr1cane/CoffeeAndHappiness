package com.mdn.coffeeandhappiness.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.fragments.accountfragments.AccountLoginFragment
import com.mdn.coffeeandhappiness.fragments.accountfragments.AccountMainFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val sharedPreferences = context?.getSharedPreferences("Account", Context.MODE_PRIVATE)

        val isLogined = sharedPreferences!!.getBoolean("IsAccountLogged", false)
        if (isLogined) replaceFragment(AccountMainFragment())
        else replaceFragment(AccountLoginFragment())

        updateToken(sharedPreferences)

        return view
    }

    private fun updateToken(sharedPreferences: SharedPreferences) {
        val accountController = AccountController()
        lifecycleScope.launch(Dispatchers.IO) {
            accountController.updateToken(sharedPreferences)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.accountFrame, fragment)
        fragmentTransaction.commit()
    }
}