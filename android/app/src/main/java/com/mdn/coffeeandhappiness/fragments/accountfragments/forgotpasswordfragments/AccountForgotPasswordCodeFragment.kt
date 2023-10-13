package com.mdn.coffeeandhappiness.fragments.accountfragments.forgotpasswordfragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountForgotPasswordCodeFragment(var email: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_account_forgot_password_code, container, false)

        val emailField = view.findViewById<TextView>(R.id.accountForgotPasswordCodeEmail)
        emailField.text = email

        val editText1 = view.findViewById<EditText>(R.id.accountForgotPasswordEnterCode1)
        val editText2 = view.findViewById<EditText>(R.id.accountForgotPasswordEnterCode2)
        val editText3 = view.findViewById<EditText>(R.id.accountForgotPasswordEnterCode3)
        val editText4 = view.findViewById<EditText>(R.id.accountForgotPasswordEnterCode4)
        val editText5 = view.findViewById<EditText>(R.id.accountForgotPasswordEnterCode5)
        val editText6 = view.findViewById<EditText>(R.id.accountForgotPasswordEnterCode6)

        // Set up TextWatcher for each EditText
        editText1.addTextChangedListener(createTextWatcher(editText1, editText2, null))
        editText2.addTextChangedListener(createTextWatcher(editText2, editText3, editText1))
        editText3.addTextChangedListener(createTextWatcher(editText3, editText4, editText2))
        editText4.addTextChangedListener(createTextWatcher(editText4, editText5, editText3))
        editText5.addTextChangedListener(createTextWatcher(editText5, editText6, editText4))
        editText6.addTextChangedListener(createTextWatcher(editText6, null, editText5))

        val verifyCodeButton =
            view.findViewById<AppCompatButton>(R.id.accountForgotPasswordCodeButton)
        verifyCodeButton.setOnClickListener() {
            if (checkCredentials(view)) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val successful =
                            AccountController().forgotPasswordValidate(email, getCode(view))

                        launch(Dispatchers.Main) {
                            if (successful) {
                                // Change fragment to set password

                                val fragmentManager = requireActivity().supportFragmentManager
                                val transaction = fragmentManager.beginTransaction()
                                transaction.replace(
                                    R.id.accountForgotPasswordActivityFrame,
                                    AccountForgotPasswordNewPasswordFragment(email, getCode(view))
                                )
                                transaction.addToBackStack(null) // Optional: Add to back stack for navigation
                                transaction.commit()
                            } else {
                                val wrongCode =
                                    view.findViewById<TextView>(R.id.accountForgotPasswordCodeWrongHint)
                                wrongCode.visibility = View.VISIBLE
                            }
                        }
                    } catch (e: NoInternetException) {
                        val noInternet =
                            view.findViewById<TextView>(R.id.accountForgotPasswordCodeNoInternet)
                        noInternet.visibility = View.VISIBLE
                    }
                }
            }
        }


        return view
    }

    private fun checkCredentials(view: View): Boolean {

        val finalCode = getCode(view)

        val wrongCode = view.findViewById<TextView>(R.id.accountForgotPasswordCodeHint)
        if (finalCode.length == 6) {
            wrongCode.visibility = View.GONE
        } else {
            wrongCode.visibility = View.VISIBLE
        }

        return (finalCode.length == 6)
    }

    private fun getCode(view: View): String {
        val editText1 = view.findViewById<EditText>(R.id.accountForgotPasswordEnterCode1)
        val editText2 = view.findViewById<EditText>(R.id.accountForgotPasswordEnterCode2)
        val editText3 = view.findViewById<EditText>(R.id.accountForgotPasswordEnterCode3)
        val editText4 = view.findViewById<EditText>(R.id.accountForgotPasswordEnterCode4)
        val editText5 = view.findViewById<EditText>(R.id.accountForgotPasswordEnterCode5)
        val editText6 = view.findViewById<EditText>(R.id.accountForgotPasswordEnterCode6)

        return editText1.text.toString() + editText2.text.toString() +
                editText3.text.toString() + editText4.text.toString() +
                editText5.text.toString() + editText6.text.toString()
    }

    private fun createTextWatcher(
        currentEditText: EditText,
        nextEditText: EditText?,
        previousEditText: EditText?  // Add this parameter
    ): TextWatcher {
        // Set up an OnKeyListener for the currentEditText
        currentEditText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (currentEditText.text.isEmpty() && previousEditText != null) {
                    // Clear the previous EditText and move the focus to it
                    previousEditText.text.clear()
                    previousEditText.requestFocus()
                    return@OnKeyListener true
                }
            }
            false
        })

        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if the user has entered a digit
                if (s?.isNotEmpty() == true) {
                    // Move the focus to the next EditText (if available)
                    nextEditText?.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }
}