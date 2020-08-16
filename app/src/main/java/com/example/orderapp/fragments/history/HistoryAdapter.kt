package com.example.orderapp.fragments.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.orderapp.data.database.OrderHistoryDBE
import com.example.orderapp.databinding.ListItemOrderHistoryBinding

class HistoryAdapter() : ListAdapter<OrderHistoryDBE, HistoryAdapter.OrderHistoryViewHolder>(OrderHistoryDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        return OrderHistoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class OrderHistoryViewHolder private constructor(val binding: ListItemOrderHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: OrderHistoryDBE) {
            binding.historyItem = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): OrderHistoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemOrderHistoryBinding.inflate(layoutInflater, parent, false)
                return OrderHistoryViewHolder(binding)
            }
        }
    }
}

class OrderHistoryDiffCallBack : DiffUtil.ItemCallback<OrderHistoryDBE>(){
    override fun areItemsTheSame(oldItem: OrderHistoryDBE, newItem: OrderHistoryDBE): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OrderHistoryDBE, newItem: OrderHistoryDBE): Boolean {
        return oldItem == newItem
    }

}
