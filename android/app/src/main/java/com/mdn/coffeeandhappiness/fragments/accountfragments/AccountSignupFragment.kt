package com.mdn.coffeeandhappiness.fragments.accountfragments

import android.content.Context
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
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountSignupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account_signup, container, false)

        val replaceButton = view.findViewById<TextView>(R.id.changeFragmentToLogin)
        replaceButton.setOnClickListener {
            val loginFragment = AccountLoginFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.accountFrame, loginFragment)
            transaction.addToBackStack(null) // Optional: Add to back stack for navigation
            transaction.commit()
        }

        val registerButton = view.findViewById<AppCompatButton>(R.id.accountRegisterButton)
        registerButton.setOnClickListener() {
            if (checkCredentials(view)) {
                val email =
                    view.findViewById<AppCompatEditText>(R.id.accountRegisterEmail).text.toString()
                val password =
                    view.findViewById<AppCompatEditText>(R.id.accountRegisterPassword).text.toString()
                val name =
                    view.findViewById<AppCompatEditText>(R.id.accountRegisterName).text.toString()
                val surname =
                    view.findViewById<AppCompatEditText>(R.id.accountRegisterSurname).text.toString()
                val sharedPreferences =
                    requireActivity().getSharedPreferences("Account", Context.MODE_PRIVATE)
                val accountController = AccountController()
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val isLogged = accountController.register(
                            email,
                            password,
                            name,
                            surname,
                            sharedPreferences
                        )
                        launch(Dispatchers.Main) {
                            val noConnection =
                                view.findViewById<TextView>(R.id.accountSignupNoInternet)
                            noConnection.visibility = View.GONE

                            if (isLogged) {
                                val mainFragment = AccountMainFragment()
                                val fragmentManager = requireActivity().supportFragmentManager
                                val transaction = fragmentManager.beginTransaction()
                                transaction.replace(R.id.accountFrame, mainFragment)
                                transaction.addToBackStack(null) // Optional: Add to back stack for navigation
                                transaction.commit()
                                val editor = sharedPreferences.edit()
                                editor.putBoolean("IsAccountLogged", true)
                                editor.apply()

                                lifecycleScope.launch(Dispatchers.IO) {
                                    accountController.updateMyself(
                                        requireContext().getSharedPreferences(
                                            "Account",
                                            Context.MODE_PRIVATE
                                        )
                                    )
                                }
                            } else {
                                val userExists =
                                    view.findViewById<TextView>(R.id.accountRegisterUserExistsHint)
                                userExists.visibility = View.VISIBLE
                            }
                        }
                    } catch (e: NoInternetException) {
                        launch(Dispatchers.Main) {
                            val noConnection =
                                view.findViewById<TextView>(R.id.accountSignupNoInternet)
                            noConnection.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        return view
    }

    private fun checkCredentials(view: View?): Boolean {
        var areCorrect = true
        val name = view?.findViewById<AppCompatEditText>(R.id.accountRegisterName)?.text
        val surname = view?.findViewById<AppCompatEditText>(R.id.accountRegisterSurname)?.text
        val email = view?.findViewById<AppCompatEditText>(R.id.accountRegisterEmail)?.text
        val password =
            view?.findViewById<AppCompatEditText>(R.id.accountRegisterPassword)?.text.toString()
        val reEnteredPassword =
            view?.findViewById<AppCompatEditText>(R.id.accountRegisterReEnterPassword)?.text.toString()
        if (email!!.isEmpty() || !isEmailValid(email.toString())) {
            val textHint = view.findViewById<TextView>(R.id.accountRegisterEmailHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = view.findViewById<TextView>(R.id.accountRegisterEmailHint)
            textHint.visibility = View.GONE
        }

        if (password.isEmpty()) {
            val textHint = view.findViewById<TextView>(R.id.accountRegisterPasswordHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = view.findViewById<TextView>(R.id.accountRegisterPasswordHint)
            textHint.visibility = View.GONE
        }

        if (password.length < 8) {
            val textHint = view.findViewById<TextView>(R.id.accountRegisterPasswordEightHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = view.findViewById<TextView>(R.id.accountRegisterPasswordEightHint)
            textHint.visibility = View.GONE
        }

        if (name!!.isEmpty()) {
            val textHint = view.findViewById<TextView>(R.id.accountRegisterNameHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = view.findViewById<TextView>(R.id.accountRegisterNameHint)
            textHint.visibility = View.GONE
        }

        if (surname!!.isEmpty()) {
            val textHint = view.findViewById<TextView>(R.id.accountRegisterSurnameHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = view.findViewById<TextView>(R.id.accountRegisterSurnameHint)
            textHint.visibility = View.GONE
        }

        if (!password.equals(reEnteredPassword)) {
            val textHint = view.findViewById<TextView>(R.id.accountRegisterReEnterPasswordHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = view.findViewById<TextView>(R.id.accountRegisterReEnterPasswordHint)
            textHint.visibility = View.GONE
        }

        val userExists = view.findViewById<TextView>(R.id.accountRegisterUserExistsHint)
        userExists.visibility = View.GONE

        return areCorrect
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}