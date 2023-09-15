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
import com.mdn.coffeeandhappiness.fragments.accountfragments.AccountSignupFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountForgotPasswordEmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountForgotPasswordEmailFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_account_forgot_password_email, container, false)

        val email = view.findViewById<TextInputEditText>(R.id.accountForgotPasswordEmailText)

        val sendCodeButton = view.findViewById<AppCompatButton>(R.id.accountForgotPasswordEmailButton)
        sendCodeButton.setOnClickListener() {
            if (checkCredentials(email, view)) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        var successful =
                            AccountController().forgotPasswordSendCode(email.text.toString())

                        launch(Dispatchers.Main) {
                            if (successful) {
                                // Change fragment to code

                                val fragmentManager = requireActivity().supportFragmentManager
                                val transaction = fragmentManager.beginTransaction()
                                transaction.replace(R.id.accountForgotPasswordActivityFrame, AccountForgotPasswordCodeFragment(email.text.toString()))
                                transaction.addToBackStack(null) // Optional: Add to back stack for navigation
                                transaction.commit()
                            }
                            else {
                                val wrongEmail = view.findViewById<TextView>(R.id.accountForgotPasswordEmailNoUserHint)
                                wrongEmail.visibility = View.VISIBLE
                            }
                        }
                    } catch (e: NoInternetException) {
                        val noInternet = view.findViewById<TextView>(R.id.accountForgotPasswordEmailNoInternet)
                        noInternet.visibility = View.VISIBLE
                    }
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
         * @return A new instance of fragment AccountForgotPasswordEmailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountForgotPasswordEmailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}