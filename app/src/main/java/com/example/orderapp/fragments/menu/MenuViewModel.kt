package com.example.orderapp.fragments.menu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.orderapp.data.database.getDatabase
import com.example.orderapp.data.repositories.BusinessRepository
import com.example.orderapp.domain.getTotalPrice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MenuViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val repository = BusinessRepository(database)

    val categories = repository.currentCategories
    val order = repository.currentOrder
    val orderButtonString : LiveData<String> = Transformations.map(order) {if (it.getTotalPrice() > 0){"Order: €" + it.getTotalPrice().toString()} else "Select items to order"}

    private var businessName : String = ""
    private var businessId : String = ""

    fun handeArgs(businessId: String, businessName: String){
        viewModelScope.launch {
            repository.getMenuCategoryAndItemsForBusiness(businessId)
        }
        this.businessId = businessId
        this.businessName = businessName
    }

    fun handleRemoveMenuItem(menuItemId : String){
        repository.removeOneFromOrder(menuItemId)
    }

    fun handleAddMenuItem(menuItemId: String){
        repository.addOneToOrder(menuItemId)
    }

    fun onOrderClicked() {
        val currentOrder = order.value
        if (currentOrder != null) {
            if (currentOrder.getTotalPrice() > 0) {
                viewModelScope.launch {
                    repository.placeOrder(currentOrder, businessName, businessId)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}