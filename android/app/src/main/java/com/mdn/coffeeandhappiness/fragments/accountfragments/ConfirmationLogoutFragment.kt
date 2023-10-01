import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.fragments.accountfragments.AccountLoginFragment

class ConfirmationLogoutFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val languagePreferences =
            requireContext().getSharedPreferences("Settings", AppCompatActivity.MODE_PRIVATE)
        val languageToSet = languagePreferences.getString("Language", "uk")

        val title = if (languageToSet == "uk") {
            "Підтвердження"
        } else {
            "Confirmation"
        }

        val message = if (languageToSet == "uk") {
            "Ви впевнені, що хочете вийти з облікового запису?"
        } else {
            "Are you sure you want to log out?"
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
                val accountController = AccountController()
                accountController.logout(
                    requireActivity().getSharedPreferences(
                        "Account",
                        Context.MODE_PRIVATE
                    )
                )
                val loginFragment = AccountLoginFragment()
                val fragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.accountFrame, loginFragment)
                transaction.addToBackStack(null) // Optional: Add to back stack for navigation
                transaction.commit()
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
