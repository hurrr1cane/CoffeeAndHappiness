package com.mdn.coffeeandhappiness.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.fragments.accountfragments.ConfirmationAccountDeleteFragment
import com.mdn.coffeeandhappiness.fragments.accountfragments.ConfirmationAccountEditedFragment
import com.mdn.coffeeandhappiness.fragments.accountfragments.reviewsfragments.ConfirmationDeleteCafeReviewFragment
import com.mdn.coffeeandhappiness.fragments.codefragments.ConfirmationOrderPlacedFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI
import java.util.Locale

class AccountEditActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private var wasImageChanged = false
    private var wasImageDeleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_account_edit)

        setResult(Activity.RESULT_OK)

        val backButton = findViewById<ImageButton>(R.id.accountEditActivityBackButton)
        backButton.setOnClickListener() {
            setResult(Activity.RESULT_OK)
            finish()
        }

        val sharedPreferences = getSharedPreferences("Account", Context.MODE_PRIVATE)

        val name = findViewById<TextInputEditText>(R.id.accountEditName)
        val surname = findViewById<TextInputEditText>(R.id.accountEditSurname)
        val phone = findViewById<TextInputEditText>(R.id.accountEditPhone)

        name.setText(sharedPreferences.getString("Name", ""))
        surname.setText(sharedPreferences.getString("Surname", ""))
        phone.setText(sharedPreferences.getString("PhoneNumber", ""))


        val settingsPreferences =
            getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val nightMode: Boolean = settingsPreferences.getBoolean("Night", true)

        val defaultImage = if (nightMode) {
            R.drawable.baseline_person_white_24
        } else {
            R.drawable.baseline_person_black_24
        }

        val image = findViewById<ImageView>(R.id.accountEditPicture)
        Glide.with(this)
            .load(sharedPreferences.getString("ImageUrl", ""))
            .placeholder(defaultImage)
            .error(defaultImage)
            .into(image)


        val imagePicker = findViewById<ImageView>(R.id.accountEditEditIcon)
        imagePicker.setOnClickListener() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        val contextext = this

        val deleteImage = findViewById<ImageButton>(R.id.accountEditDeletePicture)
        deleteImage.setOnClickListener() {
            wasImageDeleted = true
            wasImageChanged = false

            Glide.with(contextext)
                .load(defaultImage)
                .placeholder(defaultImage)
                .error(defaultImage)
                .into(image)

        }

        val submitChanges = findViewById<AppCompatButton>(R.id.accountEditSaveChanges)
        submitChanges.setOnClickListener() {
            if (checkCredentials()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        AccountController().updateInformation(
                            getSharedPreferences(
                                "Account",
                                Context.MODE_PRIVATE
                            ), name.text.toString(), surname.text.toString(), phone.text.toString()
                        )

                        if (wasImageChanged) {
                            AccountController().updatePicture(
                                getSharedPreferences(
                                    "Account",
                                    Context.MODE_PRIVATE
                                ), imageUri!!, contextext
                            )
                        }

                        if (wasImageDeleted) {
                            AccountController().deletePicture(
                                getSharedPreferences(
                                    "Account",
                                    Context.MODE_PRIVATE
                                )
                            )
                        }

                        val confirmationDialog =
                            ConfirmationAccountEditedFragment() {
                                // This lambda function will be called when the user clicks "Yes"
                                try {
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        AccountController().updateMyself(sharedPreferences)
                                    }
                                } catch (e: NoInternetException) {

                                }
                            }
                        confirmationDialog.show(
                            (contextext as FragmentActivity).supportFragmentManager,
                            "ConfirmationDialog"
                        )
                    } catch (e: NoInternetException) {
                        launch(Dispatchers.Main) {
                            showNoInternetError()
                        }
                    }
                }
            }
        }

        val changePassword = findViewById<AppCompatButton>(R.id.accountEditChangePassword)
        changePassword.setOnClickListener() {
            val intent = Intent(this, AccountChangePasswordActivity::class.java)
            startActivity(intent)
        }

        val deleteAccount = findViewById<AppCompatButton>(R.id.accountEditDeleteAccount)
        deleteAccount.setOnClickListener() {
            val confirmationDialog =
                ConfirmationAccountDeleteFragment(sharedPreferences) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            confirmationDialog.show(
                (this as FragmentActivity).supportFragmentManager,
                "ConfirmationDialog"
            )

        }
    }

    private fun showNoInternetError() {
        val noInternetError = findViewById<TextView>(R.id.accountEditNoInternet)
        noInternetError.visibility = View.VISIBLE
    }

    private fun checkCredentials(): Boolean {
        var areCorrect = true
        val name = findViewById<AppCompatEditText>(R.id.accountEditName)?.text
        val surname = findViewById<AppCompatEditText>(R.id.accountEditSurname)?.text
        val phone = findViewById<AppCompatEditText>(R.id.accountEditPhone)?.text

        if (!isPhoneNumberValid(phone.toString()) && phone!!.isNotEmpty()) {
            val textHint = findViewById<TextView>(R.id.accountEditPhoneHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = findViewById<TextView>(R.id.accountEditPhoneHint)
            textHint.visibility = View.GONE
        }

        if (name!!.isEmpty()) {
            val textHint = findViewById<TextView>(R.id.accountEditNameHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = findViewById<TextView>(R.id.accountEditNameHint)
            textHint.visibility = View.GONE
        }

        if (surname!!.isEmpty()) {
            val textHint = findViewById<TextView>(R.id.accountEditSurnameHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = findViewById<TextView>(R.id.accountEditSurnameHint)
            textHint.visibility = View.GONE
        }

        return areCorrect
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        // Define a regex pattern for the expected format
        val pattern1 = Regex("^(?:\\+?3?8?0?)([1-9]\\d{8})\$")
        val pattern2 = Regex("^(0\\d{9})\$")

        // Use the matches function to check if the phoneNumber matches the pattern
        return (pattern1.matches(phoneNumber) || pattern2.matches(phoneNumber))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val settingsPreferences =
            getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val nightMode: Boolean = settingsPreferences.getBoolean("Night", true)

        val defaultImage = if (nightMode) {
            R.drawable.baseline_person_white_24
        } else {
            R.drawable.baseline_person_black_24
        }

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageUri = data.data
                wasImageChanged = true
                wasImageDeleted = false

                val image = findViewById<ImageView>(R.id.accountEditPicture)
                Glide.with(this)
                    .load(imageUri)
                    .placeholder(defaultImage)
                    .error(defaultImage)
                    .into(image)


            }
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