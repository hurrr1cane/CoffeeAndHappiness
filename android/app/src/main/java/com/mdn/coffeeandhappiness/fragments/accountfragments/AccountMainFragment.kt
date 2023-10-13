package com.mdn.coffeeandhappiness.fragments.accountfragments


import ConfirmationLogoutFragment
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.activities.AccountEditActivity
import com.mdn.coffeeandhappiness.activities.AccountFAQActivity
import com.mdn.coffeeandhappiness.activities.AccountFeedbackActivity
import com.mdn.coffeeandhappiness.activities.AccountPrivacyPolicyActivity
import com.mdn.coffeeandhappiness.activities.AccountReviewsActivity
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountMainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account_main, container, false)

        val accountController = AccountController()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                accountController.updateMyself(
                    requireContext().getSharedPreferences(
                        "Account",
                        Context.MODE_PRIVATE
                    )
                )
            } catch (e: NoInternetException) {

            }
        }

        val logoutButton = view.findViewById<AppCompatButton>(R.id.accountMainLogout)

        logoutButton.setOnClickListener() {
            val confirmationDialog = ConfirmationLogoutFragment()
            confirmationDialog.show(requireActivity().supportFragmentManager, "ConfirmationDialog")

        }

        val accountPreferences =
            requireContext().getSharedPreferences("Account", Context.MODE_PRIVATE)

        val name = view.findViewById<TextView>(R.id.accountMainName)
        val surname = view.findViewById<TextView>(R.id.accountMainSurname)
        val email = view.findViewById<TextView>(R.id.accountMainEmail)

        name.text = accountPreferences.getString("Name", "")
        surname.text = accountPreferences.getString("Surname", "")
        email.text = accountPreferences.getString("Email", "")

        val settingsPreferences =
            requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val nightMode: Boolean = settingsPreferences.getBoolean("Night", true)

        val defaultImage = if (nightMode) {
            R.drawable.baseline_person_white_24
        } else {
            R.drawable.baseline_person_black_24
        }

        val image = view.findViewById<ImageView>(R.id.accountMainPicture)

        Glide.with(requireContext())
            .load(accountPreferences.getString("ImageUrl", ""))
            .placeholder(defaultImage)
            .error(defaultImage)
            .into(image)


        val myReviews = view.findViewById<AppCompatButton>(R.id.accountMainReviews)

        myReviews.setOnClickListener() {
            // Handle the click event here, for example, open a new activity with details
            val intent = Intent(context, AccountReviewsActivity::class.java)
            requireActivity().startActivity(intent)
        }


        val editAccount = view.findViewById<AppCompatButton>(R.id.accountMainEditAccount)

        editAccount.setOnClickListener() {
            val intent = Intent(context, AccountEditActivity::class.java)
            startActivityForResult(intent, 2)
        }

        val feedbackButton = view.findViewById<AppCompatButton>(R.id.accountMainFeedback)
        feedbackButton.setOnClickListener() {
            // Handle the click event here, for example, open a new activity with details
            val intent = Intent(context, AccountFeedbackActivity::class.java)
            requireActivity().startActivity(intent)
        }

        val privacyPolicyButton = view.findViewById<AppCompatButton>(R.id.accountMainPrivacyPolicy)
        privacyPolicyButton.setOnClickListener() {
            // Handle the click event here, for example, open a new activity with details
            val intent = Intent(context, AccountPrivacyPolicyActivity::class.java)
            requireActivity().startActivity(intent)
        }

        val faqButton = view.findViewById<AppCompatButton>(R.id.accountMainFAQ)
        faqButton.setOnClickListener() {
            // Handle the click event here, for example, open a new activity with details
            val intent = Intent(context, AccountFAQActivity::class.java)
            requireActivity().startActivity(intent)
        }

        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            if (!requireContext().getSharedPreferences("Account", Context.MODE_PRIVATE)
                    .getBoolean("IsAccountLogged", false)
            ) {
                replaceFragment(AccountLoginFragment())
            } else {
                replaceFragment(AccountMainFragment())
            }

        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.accountFrame, fragment)
        fragmentTransaction.commit()
    }

}