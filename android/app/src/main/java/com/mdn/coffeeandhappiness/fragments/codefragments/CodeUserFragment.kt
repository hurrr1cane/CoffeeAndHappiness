package com.mdn.coffeeandhappiness.fragments.codefragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.activities.CodeMyOrdersActivity
import com.mdn.coffeeandhappiness.activities.CodeWaiterPlaceOrderActivity
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.RuntimeException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CodeUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CodeUserFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_code_user, container, false)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                AccountController().updateMyself(
                    requireContext().getSharedPreferences(
                        "Account",
                        Context.MODE_PRIVATE
                    )
                )
            } catch (e: NoInternetException) {
                launch(Dispatchers.Main) {
                    val noInternet = view.findViewById<TextView>(R.id.codeUserNoInternet)
                    noInternet.visibility = View.VISIBLE

                    val myOrders = view.findViewById<AppCompatButton>(R.id.codeUserViewOrders)
                    myOrders.isEnabled = false
                }
            }
        }


        val sharedPreferences =
            requireContext().getSharedPreferences("Account", Context.MODE_PRIVATE)

        val userBalance = view.findViewById<TextView>(R.id.codeUserBalance)
        userBalance.text = sharedPreferences.getInt("BonusPoints", 0).toString()

        val imageView = view.findViewById<ImageView>(R.id.codeUserQR)

        val multiFormatWriter = MultiFormatWriter()

        try {
            val bitMatrix = multiFormatWriter.encode(
                sharedPreferences.getString("AccessToken", ""),
                BarcodeFormat.QR_CODE,
                800,
                800
            )

            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)

            imageView.setImageBitmap(bitmap)

        } catch (e: WriterException) {
            throw RuntimeException()
        }


        val myOrders = view.findViewById<AppCompatButton>(R.id.codeUserViewOrders)

        myOrders.setOnClickListener() {
            val intent = Intent(context, CodeMyOrdersActivity::class.java)
            requireContext().startActivity(intent)
        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CodeUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CodeUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}