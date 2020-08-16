package com.example.orderapp.fragments.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.orderapp.R
import com.example.orderapp.databinding.FragmentHistoryBinding


class History : Fragment() {
    val adapter = HistoryAdapter()

    lateinit var binding : FragmentHistoryBinding
    private val viewModel : HistoryViewModel by lazy { ViewModelProvider(this).get(HistoryViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.orderHistoryList.adapter = adapter

        viewModel.orderHistory.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        activity?.findViewById<Toolbar>(R.id.action_bar)?.setTitle("History")

        return binding.root
    }
}