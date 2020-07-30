package com.example.orderapp.fragments.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderapp.R
import com.example.orderapp.domain.MenuCategory

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    var data = listOf<MenuCategory>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MenuAdapter.ViewHolder, position: Int) {
        holder.name.text = data[position].name
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.txt_category_name)
    }

}