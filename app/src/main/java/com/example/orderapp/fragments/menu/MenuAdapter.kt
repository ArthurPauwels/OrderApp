package com.example.orderapp.fragments.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.orderapp.databinding.ListItemCategoryBinding
import com.example.orderapp.databinding.ListItemMenuItemBinding
import com.example.orderapp.domain.MenuCategory
import com.example.orderapp.domain.MenuItem
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.ClassCastException
import java.lang.Exception

private const val ITEM_VIEW_TYPE_CATEGORY = 0
private const val ITEM_VIEW_TYPE_MENU_ITEM = 1

class MenuAdapter(val clickListener: MenuItemListener) : ListAdapter<MenuListItem, RecyclerView.ViewHolder>(MenuDiffCallBack()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            ITEM_VIEW_TYPE_CATEGORY -> CategoryViewHolder.from(parent)
            ITEM_VIEW_TYPE_MENU_ITEM -> MenuItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MenuListItem.CategoryListItem -> ITEM_VIEW_TYPE_CATEGORY
            is MenuListItem.MenuItemListItem -> ITEM_VIEW_TYPE_MENU_ITEM
        }
    }

    fun flattenCategoryListIntoMenuListItemList(list : List<MenuCategory>){
        adapterScope.launch {
            val items = mutableListOf<MenuListItem>()
            list.forEach { category ->
                items.add(MenuListItem.CategoryListItem(category))
                items.addAll(category.menuItems.map { menuItem -> MenuListItem.MenuItemListItem(menuItem) })
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> {
                val category = getItem(position) as MenuListItem.CategoryListItem
                holder.bind(category.category)
            }
            is MenuItemViewHolder -> {
                val menuItem = getItem(position) as  MenuListItem.MenuItemListItem
                holder.bind(menuItem.menuItem, clickListener)
            }
        }
    }

    class CategoryViewHolder private constructor(val binding: ListItemCategoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: MenuCategory) {
            binding.category = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCategoryBinding.inflate(layoutInflater, parent, false)
                return CategoryViewHolder(binding)
            }
        }
    }

    class MenuItemViewHolder private constructor(val binding: ListItemMenuItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(
            item: MenuItem,
            clickListener: MenuItemListener
        ) {
            binding.menuItem = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MenuItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMenuItemBinding.inflate(layoutInflater, parent, false)
                return MenuItemViewHolder(binding)
            }
        }
    }
}

class MenuDiffCallBack : DiffUtil.ItemCallback<MenuListItem>(){
    override fun areItemsTheSame(oldItem: MenuListItem, newItem: MenuListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MenuListItem, newItem: MenuListItem): Boolean {
        return oldItem == newItem
    }

}

class MenuItemListener(val clickListener: (menuItemID : String, action : MenuItemAction) -> Unit){
    fun onClickRemove(item : MenuItem) = clickListener(item.menuItemId, MenuItemAction.REMOVE_ONE)
    fun onClickAdd(item: MenuItem) = clickListener(item.menuItemId, MenuItemAction.ADD_ONE)
}

enum class MenuItemAction{
    ADD_ONE, REMOVE_ONE
}

sealed class MenuListItem{
    data class CategoryListItem(val category: MenuCategory) : MenuListItem(){
        override val id: Long
            get() = StringBuilder(category.categoryId.toUpperCase()).removeRange(0, 10).toString().toLong(16)
    }
    data class MenuItemListItem(val menuItem: MenuItem) : MenuListItem(){
        override val id: Long
            get() = StringBuilder(menuItem.menuItemId.toUpperCase()).removeRange(0, 10).toString().toLong(16)
    }

    abstract val id : Long
}