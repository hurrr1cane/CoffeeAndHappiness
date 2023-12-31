package com.mdn.coffeeandhappiness

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.databinding.ActivityMainBinding
import com.mdn.coffeeandhappiness.fragments.AccountFragment
import com.mdn.coffeeandhappiness.fragments.CodeFragment
import com.mdn.coffeeandhappiness.fragments.HomeFragment
import com.mdn.coffeeandhappiness.fragments.MapFragment
import com.mdn.coffeeandhappiness.fragments.MenuFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : LocalizationActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.selectedItemId = R.id.nav_home

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_code -> {
                    replaceFragment(CodeFragment())
                    val button = findViewById<RelativeLayout>(R.id.nav_middle)
                    val scaleUpAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
                    button.startAnimation(scaleUpAnimation)
                }

                R.id.nav_account -> replaceFragment(AccountFragment())
                R.id.nav_map -> replaceFragment(MapFragment())
                R.id.nav_menu -> replaceFragment(MenuFragment())

                else -> {}

            }
            true
        }

        /**
         * Settings
         */
        val sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)

        /**
         * Setting the mode
         */
        controllerLightDarkMode(sharedPreferences)

        /**
         * Setting the language
         */
        controllerLanguage(sharedPreferences)

        updateToken(sharedPreferences)
    }

    private fun updateToken(sharedPreferences: SharedPreferences) {
        val accountController = AccountController()
        lifecycleScope.launch(Dispatchers.IO) {
            accountController.updateToken(sharedPreferences)
        }
    }

    private fun controllerLanguage(sharedPreferences: SharedPreferences) {
        var language = sharedPreferences.getString("Language", "uk")

        val languageButton = findViewById<ImageButton>(R.id.languageButton)

        when (language) {
            "uk" -> {
                languageButton.setImageResource(R.drawable.ukrainian_flag_icon)
            }

            "en" -> {
                languageButton.setImageResource(R.drawable.english_flag_icon)
            }
        }
        if (language != null) {
            setLanguage(language)
        }

        val editor = sharedPreferences.edit()
        languageButton.setOnClickListener {
            language = sharedPreferences.getString("Language", "uk")
            when (language) {
                "uk" -> {
                    editor.putString("Language", "en")
                    editor.apply()
                }

                "en" -> {
                    editor.putString("Language", "uk")
                    editor.apply()
                }
            }
            language = sharedPreferences.getString("Language", "uk")
            setLanguage(language!!)
            restartApp()
        }
    }

    private fun controllerLightDarkMode(sharedPreferences: SharedPreferences) {
        val nightMode: Boolean = sharedPreferences.getBoolean("Night", true)
        val modeButton = findViewById<ImageButton>(R.id.modeButton)
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            modeButton.setImageResource(R.drawable.darkmode_icon)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            modeButton.setImageResource(R.drawable.lightmode_icon)
        }

        modeButton.setOnClickListener {
            val editor = sharedPreferences.edit()
            if (nightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                modeButton.setImageResource(R.drawable.lightmode_icon)
                editor.putBoolean("Night", false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                modeButton.setImageResource(R.drawable.darkmode_icon)
                editor.putBoolean("Night", true)
            }
            editor.apply()

            restartApp()
        }
    }

    private fun restartApp() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.mainFrame, fragment)
        fragmentTransaction.commit()
    }
}