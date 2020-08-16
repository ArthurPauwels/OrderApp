package com.example.orderapp.fragments.orderPlaced

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.orderapp.R
import com.example.orderapp.databinding.FragmentOrderPlacedBinding

class OrderPlaced : Fragment() {

    lateinit var binding : FragmentOrderPlacedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_placed, container, false
        )

        activity?.findViewById<Toolbar>(R.id.action_bar)?.setTitle("Order placed")

        binding.bttnReturnHome.setOnClickListener {
            findNavController().navigate(OrderPlacedDirections.actionOrderPlacedToBusinessOverview())
        }
        return binding.root
    }
}