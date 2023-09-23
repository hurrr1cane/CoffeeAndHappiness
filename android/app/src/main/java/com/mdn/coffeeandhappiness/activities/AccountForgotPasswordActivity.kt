package com.mdn.coffeeandhappiness.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.fragments.accountfragments.AccountSignupFragment
import com.mdn.coffeeandhappiness.fragments.accountfragments.forgotpasswordfragments.AccountForgotPasswordEmailFragment
import java.util.Locale

class AccountForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_account_forgot_password)

        val backButton = findViewById<ImageButton>(R.id.accountForgotPasswordActivityBackButton)
        backButton.setOnClickListener() {
            finish()
        }

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(
            R.id.accountForgotPasswordActivityFrame,
            AccountForgotPasswordEmailFragment()
        )
        transaction.commit()



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