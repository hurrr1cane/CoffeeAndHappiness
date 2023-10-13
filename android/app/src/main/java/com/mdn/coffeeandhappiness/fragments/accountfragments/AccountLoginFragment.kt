package com.mdn.coffeeandhappiness.fragments.accountfragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.activities.AccountForgotPasswordActivity
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountLoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account_login, container, false)

        val replaceButton = view.findViewById<TextView>(R.id.changeFragmentToSignup)
        replaceButton.setOnClickListener {
            val signUpFragment = AccountSignupFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.accountFrame, signUpFragment)
            transaction.addToBackStack(null) // Optional: Add to back stack for navigation
            transaction.commit()
        }

        val forgotPassword = view.findViewById<TextView>(R.id.accountLoginForgotPassword)
        forgotPassword.setOnClickListener() {
            val intent = Intent(requireContext(), AccountForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        val loginButton = view.findViewById<AppCompatButton>(R.id.accountLoginButton)
        loginButton.setOnClickListener() {
            if (checkCredentials(view)) {
                val email =
                    view.findViewById<AppCompatEditText>(R.id.accountLoginEmail).text.toString()
                val password =
                    view.findViewById<AppCompatEditText>(R.id.accountLoginPassword).text.toString()
                val sharedPreferences =
                    requireActivity().getSharedPreferences("Account", Context.MODE_PRIVATE)
                val accountController = AccountController()
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val isLogged = accountController.login(email, password, sharedPreferences)
                        launch(Dispatchers.Main) {
                            val noInternet =
                                view.findViewById<TextView>(R.id.accountLoginNoInternet)
                            noInternet.visibility = View.GONE

                            if (isLogged) {
                                try {
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        AccountController().updateMyself(sharedPreferences)
                                        launch(Dispatchers.Main) {
                                            val mainFragment = AccountMainFragment()
                                            val fragmentManager =
                                                requireActivity().supportFragmentManager
                                            val transaction = fragmentManager.beginTransaction()
                                            transaction.replace(R.id.accountFrame, mainFragment)
                                            transaction.addToBackStack(null) // Optional: Add to back stack for navigation
                                            transaction.commit()
                                            val editor = sharedPreferences.edit()
                                            editor.putBoolean("IsAccountLogged", true)
                                            editor.apply()
                                            val wrongCredentials =
                                                view.findViewById<TextView>(R.id.accountLoginWrong)
                                            wrongCredentials.visibility = View.GONE
                                        }
                                    }
                                } catch (e: NoInternetException) {
                                    launch(Dispatchers.Main) {
                                        noInternet.visibility = View.VISIBLE
                                    }
                                }


                            } else {
                                val wrongCredentials =
                                    view.findViewById<TextView>(R.id.accountLoginWrong)
                                wrongCredentials.visibility = View.VISIBLE
                            }
                        }
                    } catch (e: NoInternetException) {
                        // Handle network-related error (no internet connection)
                        launch(Dispatchers.Main) {
                            val noInternet =
                                view.findViewById<TextView>(R.id.accountLoginNoInternet)
                            noInternet.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        return view
    }

    private fun checkCredentials(view: View?): Boolean {
        var areCorrect = true
        val email = view?.findViewById<AppCompatEditText>(R.id.accountLoginEmail)?.text
        val password = view?.findViewById<AppCompatEditText>(R.id.accountLoginPassword)?.text
        if (email!!.isEmpty() || !isEmailValid(email.toString())) {
            val textHint = view.findViewById<TextView>(R.id.accountLoginEmailHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = view.findViewById<TextView>(R.id.accountLoginEmailHint)
            textHint.visibility = View.GONE
        }

        if (password!!.isEmpty()) {
            val textHint = view.findViewById<TextView>(R.id.accountLoginPasswordHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = view.findViewById<TextView>(R.id.accountLoginPasswordHint)
            textHint.visibility = View.GONE
        }
        return areCorrect
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

}