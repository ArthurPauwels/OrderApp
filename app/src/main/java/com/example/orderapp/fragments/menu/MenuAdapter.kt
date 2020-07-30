package com.example.orderapp.fragments.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderapp.R
import com.example.orderapp.domain.MenuCategory

class MenuAdapter : RecyclerView.Adapter<TextItemViewHolder>() {
    var data = listOf<MenuCategory>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        holder.textView.text = data[position].name
    }

}

class TextItemViewHolder(val textView : TextView) : RecyclerView.ViewHolder(textView)