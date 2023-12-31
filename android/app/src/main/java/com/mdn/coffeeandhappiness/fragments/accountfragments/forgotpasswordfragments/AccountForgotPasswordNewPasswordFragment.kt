package com.mdn.coffeeandhappiness.fragments.accountfragments.forgotpasswordfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountForgotPasswordNewPasswordFragment(var email: String, var code: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_account_forgot_password_new_password,
            container,
            false
        )

        val setNewPassword =
            view.findViewById<AppCompatButton>(R.id.accountForgotPasswordNewPasswordButton)
        setNewPassword.setOnClickListener() {
            if (checkCredentials(view)) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        AccountController().forgotPasswordSetNew(
                            email,
                            code,
                            view.findViewById<TextInputEditText>(R.id.accountForgotPasswordNewPassword).text.toString()
                        )

                        launch(Dispatchers.Main) {

                            // Change fragment to successful message
                            val fragmentManager = requireActivity().supportFragmentManager
                            val transaction = fragmentManager.beginTransaction()
                            transaction.replace(
                                R.id.accountForgotPasswordActivityFrame,
                                AccountForgotPasswordSuccessFragment()
                            )
                            transaction.addToBackStack(null) // Optional: Add to back stack for navigation
                            transaction.commit()

                        }
                    } catch (e: NoInternetException) {
                        val noInternet =
                            view.findViewById<TextView>(R.id.accountForgotPasswordNewPasswordNoInternet)
                        noInternet.visibility = View.VISIBLE
                    }
                }
            }
        }

        return view
    }

    private fun checkCredentials(view: View): Boolean {
        var areCorrect = true
        val password =
            view.findViewById<AppCompatEditText>(R.id.accountForgotPasswordNewPassword)?.text.toString()
        val reEnteredPassword =
            view.findViewById<AppCompatEditText>(R.id.accountForgotPasswordReEnterPassword)?.text.toString()

        if (password.isEmpty()) {
            val textHint = view.findViewById<TextView>(R.id.accountForgotPasswordNewPasswordHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = view.findViewById<TextView>(R.id.accountForgotPasswordNewPasswordHint)
            textHint.visibility = View.GONE
        }

        if (password.length < 8) {
            val textHint =
                view.findViewById<TextView>(R.id.accountForgotPasswordNewPasswordEightHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint =
                view.findViewById<TextView>(R.id.accountForgotPasswordNewPasswordEightHint)
            textHint.visibility = View.GONE
        }



        if (!password.equals(reEnteredPassword)) {
            val textHint =
                view.findViewById<TextView>(R.id.accountForgotPasswordReEnterPasswordHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint =
                view.findViewById<TextView>(R.id.accountForgotPasswordReEnterPasswordHint)
            textHint.visibility = View.GONE
        }


        return areCorrect
    }
}