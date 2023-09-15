package com.mdn.coffeeandhappiness.activities

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.fragments.accountfragments.ConfirmationAccountEditedFragment
import com.mdn.coffeeandhappiness.fragments.accountfragments.ConfirmationAccountPasswordChangedFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class AccountChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_account_change_password)

        val backButton = findViewById<ImageButton>(R.id.accountChangePasswordActivityBackButton)
        backButton.setOnClickListener() {
            finish()
        }

        val sharedPreferences = getSharedPreferences("Account", Context.MODE_PRIVATE)
        val contextext = this

        val submitButton =
            findViewById<AppCompatButton>(R.id.accountChangePasswordActivitySubmitButton)
        submitButton.setOnClickListener() {
            val result = checkCredentials()

            val wrongPassword = findViewById<TextView>(R.id.accountChangePasswordActivityPasswordWrongHint)
            wrongPassword.visibility = View.GONE

            val oldPassword =
                findViewById<TextInputEditText>(R.id.accountChangePasswordActivityOldPassword).text
            val newPassword =
                findViewById<TextInputEditText>(R.id.accountChangePasswordActivityNewPassword).text

            if (result) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        var isCorrectPassword = AccountController().changePassword(
                            getSharedPreferences(
                                "Account",
                                Context.MODE_PRIVATE
                            ), oldPassword.toString(), newPassword.toString()
                        )
                        launch(Dispatchers.Main) {
                            if (isCorrectPassword) {
                                val confirmationDialog =
                                    ConfirmationAccountPasswordChangedFragment() {
                                        // This lambda function will be called when the user clicks "Yes"
                                        try {
                                            lifecycleScope.launch(Dispatchers.IO) {
                                                AccountController().updateMyself(sharedPreferences)
                                            }
                                            val editor = sharedPreferences.edit()
                                            editor.putString("Password", newPassword.toString())
                                            editor.apply()
                                            finish()
                                        } catch (e: NoInternetException) {

                                        }
                                    }
                                confirmationDialog.show(
                                    (contextext as FragmentActivity).supportFragmentManager,
                                    "ConfirmationDialog"
                                )
                            }
                            else {
                                val wrongPassword = findViewById<TextView>(R.id.accountChangePasswordActivityPasswordWrongHint)
                                wrongPassword.visibility = View.VISIBLE
                            }
                        }
                    } catch (e: NoInternetException) {

                        val noInternet = findViewById<TextView>(R.id.accountChangePasswordActivityNoInternet)
                        noInternet.visibility = View.VISIBLE
                    }
                }
            }

        }
    }

    private fun checkCredentials(): Boolean {
        var result = true

        val oldPassword =
            findViewById<TextInputEditText>(R.id.accountChangePasswordActivityOldPassword)
        if (oldPassword.text!!.isEmpty()) {
            result = false

            val oldPasswordHint =
                findViewById<TextView>(R.id.accountChangePasswordActivityOldPasswordHint)
            oldPasswordHint.visibility = View.VISIBLE
        } else {
            val oldPasswordHint =
                findViewById<TextView>(R.id.accountChangePasswordActivityOldPasswordHint)
            oldPasswordHint.visibility = View.GONE
        }

        val newPassword =
            findViewById<TextInputEditText>(R.id.accountChangePasswordActivityNewPassword)
        if (newPassword.text!!.isEmpty()) {
            result = false

            val newPasswordHint =
                findViewById<TextView>(R.id.accountChangePasswordActivityNewPasswordHint)
            newPasswordHint.visibility = View.VISIBLE
        } else {
            val newPasswordHint =
                findViewById<TextView>(R.id.accountChangePasswordActivityNewPasswordHint)
            newPasswordHint.visibility = View.GONE
        }

        if (newPassword.text!!.length < 8) {
            result = false

            val newPasswordHint =
                findViewById<TextView>(R.id.accountChangePasswordActivityNewPasswordEightHint)
            newPasswordHint.visibility = View.VISIBLE
        } else {
            val newPasswordHint =
                findViewById<TextView>(R.id.accountChangePasswordActivityNewPasswordEightHint)
            newPasswordHint.visibility = View.GONE
        }

        val repeatedPassword =
            findViewById<TextInputEditText>(R.id.accountChangePasswordActivityReEnterPassword)
        if (repeatedPassword.text.toString() != newPassword.text.toString()) {
            result = false

            val repeatedPasswordHint =
                findViewById<TextView>(R.id.accountChangePasswordActivityReEnterPasswordHint)
            repeatedPasswordHint.visibility = View.VISIBLE
        } else {
            val repeatedPasswordHint =
                findViewById<TextView>(R.id.accountChangePasswordActivityReEnterPasswordHint)
            repeatedPasswordHint.visibility = View.GONE
        }

        return result
    }

    private fun setLanguage() {
        val languagePreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        var languageToSet = languagePreferences.getString("Language", "uk")

        var locale = Locale(languageToSet)
        Locale.setDefault(locale)
        var configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        baseContext.resources.updateConfiguration(
            configuration,
            baseContext.resources.displayMetrics
        )
    }
}