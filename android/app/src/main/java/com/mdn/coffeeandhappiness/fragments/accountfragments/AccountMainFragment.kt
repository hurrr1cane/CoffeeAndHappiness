package com.mdn.coffeeandhappiness.fragments.accountfragments


import ConfirmationLogoutFragment
import android.content.Context
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
import com.mdn.coffeeandhappiness.controller.AccountController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountMainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_account_main, container, false)

        val accountController = AccountController()
        lifecycleScope.launch(Dispatchers.IO) {
            accountController.updateMyself(
                requireContext().getSharedPreferences(
                    "Account",
                    Context.MODE_PRIVATE
                )
            )
        }

        val logoutButton = view.findViewById<AppCompatButton>(R.id.accountMainLogout)

        logoutButton.setOnClickListener() {
            val confirmationDialog = ConfirmationLogoutFragment()
            confirmationDialog.show(requireActivity().supportFragmentManager, "ConfirmationDialog")

        }

        val accountPreferences = requireContext().getSharedPreferences("Account", Context.MODE_PRIVATE)

        val name = view.findViewById<TextView>(R.id.accountMainName)
        val surname = view.findViewById<TextView>(R.id.accountMainSurname)
        val email = view.findViewById<TextView>(R.id.accountMainEmail)

        name.text = accountPreferences.getString("Name", "")
        surname.text = accountPreferences.getString("Surname", "")
        email.text = accountPreferences.getString("Email", "")

        val settingsPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val nightMode: Boolean = settingsPreferences.getBoolean("Night", true)

        val defaultImage = if (nightMode) {
            R.drawable.baseline_person_white_24
        }
        else {
            R.drawable.baseline_person_black_24
        }

        val image = view.findViewById<ImageView>(R.id.accountMainPicture)

        Glide.with(requireContext())
            .load(accountPreferences.getString("ImageUrl", ""))
            .placeholder(defaultImage)
            .error(defaultImage)
            .into(image)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountMainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}