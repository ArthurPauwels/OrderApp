package com.example.orderapp.fragments.menu

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.orderapp.domain.MenuCategory
import com.example.orderapp.domain.MenuItem

@BindingAdapter("categoryName")
fun TextView.setCategoryName(item : MenuCategory?){
    item?.let {
        text = item.name
    }
}

@BindingAdapter("menuItemName")
fun  TextView.setMenuItemName(item: MenuItem?){
    item?.let { text = item.name }
}

@BindingAdapter("menuItemPrice")
fun TextView.setMenuItemPrice(item: MenuItem?){
    item?.let { text = "€ " + item.price.toString() }
}

@BindingAdapter("menuItemDescription")
fun  TextView.setMenuItemDescription(item: MenuItem?){
    item?.let { text = item.description }
}
