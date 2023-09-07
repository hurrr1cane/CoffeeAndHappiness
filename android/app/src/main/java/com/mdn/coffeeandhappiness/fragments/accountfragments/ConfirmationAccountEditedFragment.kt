package com.mdn.coffeeandhappiness.fragments.accountfragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.mdn.coffeeandhappiness.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmationDeleteReviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmationAccountEditedFragment(
    private val onYesClicked: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val languagePreferences =
            requireContext().getSharedPreferences("Settings", AppCompatActivity.MODE_PRIVATE)
        var languageToSet = languagePreferences.getString("Language", "uk")

        val title = if (languageToSet == "uk") {
            "Підтвердження"
        } else {
            "Confirmation"
        }

        val message = if (languageToSet == "uk") {
            "Інформація про акаунт була успішно оновлена"
        } else {
            "An information about account has successfully changed"
        }

        val positive = if (languageToSet == "uk") {
            "Гаразд"
        } else {
            "Ok"
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positive, null) // Null click listener for now
            .create()

        // Set custom colors for the buttons
        alertDialog.setOnShowListener { dialog ->
            val positiveButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)

            // Set custom colors here
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.default_green
                )
            )

            // Set custom text style
            val customFontStyle =
                R.style.ACCOUNT_MAIN_DIALOG_TEXT // Use the custom style defined in styles.xml

            // Apply custom font style to message
            (dialog.findViewById<View>(android.R.id.message) as TextView).setTextAppearance(
                requireContext(),
                customFontStyle
            )

            // Apply custom font style to positive button
            positiveButton.setTextAppearance(requireContext(), customFontStyle)

            // Set click listeners here if needed
            positiveButton.setOnClickListener {
                onYesClicked()
                dialog.dismiss()
            }

        }

        return alertDialog
    }
}