package com.mdn.coffeeandhappiness.fragments.accountfragments.forgotpasswordfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountForgotPasswordEmailFragment : Fragment() {

    private fun checkCredentials(email: TextInputEditText?, view: View): Boolean {
        val hint = view.findViewById<TextView>(R.id.accountForgotPasswordEmailHint)
        return if (!isEmailValid(email!!.text.toString())) {
            hint.visibility = View.VISIBLE
            false
        } else {
            hint.visibility = View.GONE
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_account_forgot_password_email, container, false)

        val email = view.findViewById<TextInputEditText>(R.id.accountForgotPasswordEmailText)

        val sendCodeButton =
            view.findViewById<AppCompatButton>(R.id.accountForgotPasswordEmailButton)
        sendCodeButton.setOnClickListener() {
            if (checkCredentials(email, view)) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val successful =
                            AccountController().forgotPasswordSendCode(email.text.toString())

                        launch(Dispatchers.Main) {
                            if (successful) {
                                // Change fragment to code

                                val fragmentManager = requireActivity().supportFragmentManager
                                val transaction = fragmentManager.beginTransaction()
                                transaction.replace(
                                    R.id.accountForgotPasswordActivityFrame,
                                    AccountForgotPasswordCodeFragment(email.text.toString())
                                )
                                transaction.addToBackStack(null) // Optional: Add to back stack for navigation
                                transaction.commit()
                            } else {
                                val wrongEmail =
                                    view.findViewById<TextView>(R.id.accountForgotPasswordEmailNoUserHint)
                                wrongEmail.visibility = View.VISIBLE
                            }
                        }
                    } catch (e: NoInternetException) {
                        val noInternet =
                            view.findViewById<TextView>(R.id.accountForgotPasswordEmailNoInternet)
                        noInternet.visibility = View.VISIBLE
                    }
                }
            }
        }

        return view
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}