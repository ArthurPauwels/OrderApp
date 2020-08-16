package com.example.orderapp.fragments.history

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.orderapp.data.database.OrderHistoryDBE

@BindingAdapter("historyPlace")
fun TextView.setBusinessName(item : OrderHistoryDBE?){
    item?.let {
        text = item.businessName
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("historyPrice")
fun TextView.setPrice(item : OrderHistoryDBE?){
    item?.let {
        text = "€" + item.totalPrice
    }
}

@BindingAdapter("hisotryTime")
fun TextView.setTime(item : OrderHistoryDBE?){
    item?.let {
        text = item.time
    }
}