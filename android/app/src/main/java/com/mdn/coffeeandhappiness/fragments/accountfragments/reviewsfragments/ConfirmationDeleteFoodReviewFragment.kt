package com.mdn.coffeeandhappiness.fragments.accountfragments.reviewsfragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.controller.ReviewController
import com.mdn.coffeeandhappiness.fragments.accountfragments.AccountLoginFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmationDeleteReviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmationDeleteFoodReviewFragment(
    var reviewId: Int,
    private val onDeleteConfirmed: () -> Unit
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
            "Ви впевнені, що хочете видалити цей відгук?"
        } else {
            "Are you sure you want to delete this review?"
        }

        val positive = if (languageToSet == "uk") {
            "Так"
        } else {
            "Yes"
        }

        val negative = if (languageToSet == "uk") {
            "Ні"
        } else {
            "No"
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positive, null) // Null click listener for now
            .setNegativeButton(negative, null) // Null click listener for now
            .create()

        // Set custom colors for the buttons
        alertDialog.setOnShowListener { dialog ->
            val positiveButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            // Set custom colors here
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.default_green
                )
            )
            negativeButton.setTextColor(
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

            // Apply custom font style to positive and negative buttons
            positiveButton.setTextAppearance(requireContext(), customFontStyle)
            negativeButton.setTextAppearance(requireContext(), customFontStyle)

            // Set click listeners here if needed
            positiveButton.setOnClickListener {
                // Handle positive button click here
                val reviewController = ReviewController()
                lifecycleScope.launch(Dispatchers.IO) {
                    reviewController.deleteFoodReview(
                        requireContext().getSharedPreferences(
                            "Account",
                            Context.MODE_PRIVATE
                        ), reviewId
                    )
                }
                onDeleteConfirmed()
                dialog.dismiss()
            }

            negativeButton.setOnClickListener {
                // Handle negative button click here
                dialog.dismiss()
            }
        }

        return alertDialog
    }
}