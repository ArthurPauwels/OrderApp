package com.example.orderapp.fragments.menu

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
import com.example.orderapp.databinding.FragmentMenuBinding

class Menu : Fragment() {
    val adapter = MenuAdapter(MenuItemListener { menuItemID, action ->
        when (action){
            MenuItemAction.ADD_ONE -> viewModel.handleAddMenuItem(menuItemID)
            MenuItemAction.REMOVE_ONE -> viewModel.handleRemoveMenuItem(menuItemID)
        }
    })

    lateinit var binding : FragmentMenuBinding
    private val viewModel : MenuViewModel by lazy { ViewModelProvider(this).get(MenuViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val args = MenuArgs.fromBundle(requireArguments())
        viewModel.handeArgs(args.businessID)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        binding.menuList.adapter = adapter
        viewModel.categories.observe(viewLifecycleOwner, Observer {
            //TODO never gets to this... so the view never gets updated
            it?.let { adapter.flattenCategoryListIntoMenuListItemList(it)}
            //adapter.notifyDataSetChanged()
        })
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel


        activity?.findViewById<Toolbar>(R.id.action_bar)?.setTitle(args.businessName)

        return binding.root
    }
}