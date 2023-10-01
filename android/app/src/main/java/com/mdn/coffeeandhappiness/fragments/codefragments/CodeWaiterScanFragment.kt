package com.mdn.coffeeandhappiness.fragments.codefragments

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.mdn.coffeeandhappiness.R
import com.mdn.coffeeandhappiness.controller.AccountController
import com.mdn.coffeeandhappiness.controller.OrderController
import com.mdn.coffeeandhappiness.exception.NoInternetException
import com.mdn.coffeeandhappiness.model.Food
import com.mdn.coffeeandhappiness.tools.OrderHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val CAMERA_REQUEST_CODE = 101

class CodeWaiterScanFragment(var listOfFood: MutableList<Food>) : Fragment() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_code_waiter_scan, container, false)

        setupPermissions()
        var accessToken: String

        val sumOfOrder = view.findViewById<TextView>(R.id.codeWaiterScanSum)
        val price = String.format("%.2f", OrderHelper().countTotalPrice(listOfFood)) + "₴"
        sumOfOrder.text = price


        codeScanner = CodeScanner(requireContext(), view.findViewById(R.id.codeWaiterScanScanner))
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.TWO_DIMENSIONAL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    accessToken = it.text

                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val person = AccountController().getByToken(accessToken)
                            launch(Dispatchers.Main) {
                                if (person != null) {
                                    val userAuthorized =
                                        view.findViewById<TextView>(R.id.codeWaiterScanUserAuthorized)
                                    userAuthorized.visibility = View.VISIBLE

                                    val userCanSpend =
                                        view.findViewById<TextView>(R.id.codeWaiterScanUserCanSpend)

                                    val spendPoints =
                                        view.findViewById<AppCompatButton>(R.id.codeWaiterScanSpendPoints)
                                    val placeOrder =
                                        view.findViewById<AppCompatButton>(R.id.codeWaiterScanPlaceOrder)
                                    if (person.bonusPoints >= OrderHelper().countTotalPrice(
                                            listOfFood
                                        )
                                            .toInt() * 10
                                    ) {
                                        spendPoints.isEnabled = true
                                        spendPoints.setTextColor(resources.getColor(R.color.default_green))
                                        userCanSpend.visibility = View.VISIBLE
                                    }

                                    placeOrder.isEnabled = true
                                    placeOrder.setTextColor(resources.getColor(R.color.default_green))

                                    placeOrder.setOnClickListener {
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            try {
                                                OrderController().placeOrder(
                                                    requireContext().getSharedPreferences(
                                                        "Account",
                                                        Context.MODE_PRIVATE
                                                    ),
                                                    person.id,
                                                    OrderHelper().getListOfIds(listOfFood)
                                                )

                                                val confirmationDialog =
                                                    ConfirmationOrderPlacedFragment() {
                                                        // This lambda function will be called when the user clicks "Yes"
                                                        requireActivity().finish()
                                                    }
                                                confirmationDialog.show(
                                                    (context as FragmentActivity).supportFragmentManager,
                                                    "ConfirmationDialog"
                                                )
                                            } catch (e: NoInternetException) {

                                            }
                                        }
                                    }

                                    spendPoints.setOnClickListener {

                                        lifecycleScope.launch(Dispatchers.IO) {
                                            try {
                                                OrderController().spendPoints(
                                                    requireContext().getSharedPreferences(
                                                        "Account",
                                                        Context.MODE_PRIVATE
                                                    ),
                                                    person.id,
                                                    OrderHelper().getListOfIds(listOfFood)
                                                )

                                                val confirmationDialog =
                                                    ConfirmationOrderPlacedFragment() {
                                                        // This lambda function will be called when the user clicks "Yes"
                                                        requireActivity().finish()
                                                    }
                                                confirmationDialog.show(
                                                    (context as FragmentActivity).supportFragmentManager,
                                                    "ConfirmationDialog"
                                                )
                                            } catch (e: NoInternetException) {

                                            }
                                        }
                                    }


                                } else {
                                    codeScanner.startPreview()
                                }
                            }
                        } catch (e: NoInternetException) {

                        }
                    }
                }

            }

            errorCallback = ErrorCallback {

            }
        }
        codeScanner.startPreview()


        return view
    }

    override fun onResume() {
        super.onResume()

        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()

        codeScanner.releaseResources()
    }

    private fun setupPermissions() {
        val permission =
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        requireContext(),
                        "You need the camera permission to scan qr code!",
                        Toast.LENGTH_SHORT
                    )

                }
            }
        }
    }

}