package com.mdn.coffeeandhappiness.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.mdn.coffeeandhappiness.R
import java.util.Locale

class AccountFeedbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_account_feedback)

        val backButton = findViewById<ImageButton>(R.id.accountFeedbackActivityBackButton)
        backButton.setOnClickListener() {
            finish()
        }
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