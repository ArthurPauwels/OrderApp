package com.example.orderapp.fragments.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.orderapp.R
import com.example.orderapp.fragments.MenuArgs

class Menu : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val args =
            MenuArgs.fromBundle(requireArguments())
        Toast.makeText(context, args.businessName, Toast.LENGTH_LONG).show()
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }
}