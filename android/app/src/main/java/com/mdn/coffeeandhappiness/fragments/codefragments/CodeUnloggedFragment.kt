package com.mdn.coffeeandhappiness.fragments.codefragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.mdn.coffeeandhappiness.R

class CodeUnloggedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_code_unlogged, container, false)

        val background = view.findViewById<ImageView>(R.id.codeUnloggedBackground)

        if (requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
                .getBoolean("Night", true)
        ) {
            background.setImageResource(R.drawable.code_unlogged_black)
        } else {
            background.setImageResource(R.drawable.code_unlogged_white)
        }

        return view
    }

}