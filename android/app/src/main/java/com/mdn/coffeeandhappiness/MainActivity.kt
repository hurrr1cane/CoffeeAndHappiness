package com.mdn.coffeeandhappiness

import android.content.Context
import android.content.SharedPreferences
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.mdn.coffeeandhappiness.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

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
                R.id.nav_info -> replaceFragment(InfoFragment())
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
        var sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)

        /**
         * Setting the mode
         */
        controllerLightDarkMode(sharedPreferences)


    }

    private fun controllerLightDarkMode(sharedPreferences: SharedPreferences) {
        val nightMode: Boolean = sharedPreferences.getBoolean("Night", true)
        val modeButton = findViewById<ImageButton>(R.id.modeButton)
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            modeButton.setImageResource(R.drawable.darkmode_icon)
        }
        modeButton.setOnClickListener() {
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
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.mainFrame, fragment)
        fragmentTransaction.commit()
    }
}