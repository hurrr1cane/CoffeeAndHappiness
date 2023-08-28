package com.mdn.coffeeandhappiness.fragments.accountfragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.AccountController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountLoginFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_account_login, container, false)

        val replaceButton = view.findViewById<TextView>(R.id.changeFragmentToSignup)
        replaceButton.setOnClickListener {
            val signUpFragment = AccountSignupFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.accountFrame, signUpFragment)
            transaction.addToBackStack(null) // Optional: Add to back stack for navigation
            transaction.commit()
        }

        val loginButton = view.findViewById<AppCompatButton>(R.id.accountLoginButton)
        loginButton.setOnClickListener() {
            if (checkCredentials(view)) {
                val email = view.findViewById<AppCompatEditText>(R.id.accountLoginEmail).text.toString()
                val password = view.findViewById<AppCompatEditText>(R.id.accountLoginPassword).text.toString()
                val sharedPreferences = requireActivity().getSharedPreferences("Account", Context.MODE_PRIVATE)
                val accountController = AccountController()
                lifecycleScope.launch(Dispatchers.IO) {
                    val isLogged = accountController.login(email, password, sharedPreferences)
                    launch(Dispatchers.Main) {
                        if (isLogged) {
                            val mainFragment = AccountMainFragment()
                            val fragmentManager = requireActivity().supportFragmentManager
                            val transaction = fragmentManager.beginTransaction()
                            transaction.replace(R.id.accountFrame, mainFragment)
                            transaction.addToBackStack(null) // Optional: Add to back stack for navigation
                            transaction.commit()
                            var editor = sharedPreferences.edit()
                            editor.putBoolean("IsAccountLogged", true)
                            editor.apply()
                            val wrongCredentials = view.findViewById<TextView>(R.id.accountLoginWrong)
                            wrongCredentials.visibility = View.GONE
                        } else {
                            val wrongCredentials = view.findViewById<TextView>(R.id.accountLoginWrong)
                            wrongCredentials.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        return view
    }

    private fun checkCredentials(view: View?): Boolean {
        var areCorrect = true
        val email = view?.findViewById<AppCompatEditText>(R.id.accountLoginEmail)?.text
        val password = view?.findViewById<AppCompatEditText>(R.id.accountLoginPassword)?.text
        if (email!!.isEmpty()) {
            val textHint = view.findViewById<TextView>(R.id.accountLoginEmailHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = view.findViewById<TextView>(R.id.accountLoginEmailHint)
            textHint.visibility = View.GONE
        }

        if (password!!.isEmpty()) {
            val textHint = view.findViewById<TextView>(R.id.accountLoginPasswordHint)
            textHint.visibility = View.VISIBLE
            areCorrect = false
        } else {
            val textHint = view.findViewById<TextView>(R.id.accountLoginPasswordHint)
            textHint.visibility = View.GONE
        }
        return areCorrect
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountLogin.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                AccountLoginFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}