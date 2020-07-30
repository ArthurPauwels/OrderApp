package com.example.orderapp.fragments.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.orderapp.R
import com.example.orderapp.databinding.FragmentMenuBinding

class Menu : Fragment() {
    val adapter = MenuAdapter()

    lateinit var binding : FragmentMenuBinding
    private val viewModel : MenuViewModel by lazy { ViewModelProvider(this).get(MenuViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val args = MenuArgs.fromBundle(requireArguments())
        viewModel.handeArgs(args.businessID, args.businessName)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        binding.menuList.adapter = adapter
        viewModel.categories.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.data = it }
        })
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        return binding.root
    }
}