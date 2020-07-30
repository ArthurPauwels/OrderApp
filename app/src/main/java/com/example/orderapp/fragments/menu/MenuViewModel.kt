package com.example.orderapp.fragments.menu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.orderapp.data.database.getDatabase
import com.example.orderapp.data.repositories.BusinessRepository
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

    fun handeArgs(businessId: String, businessName: String){
        viewModelScope.launch {
            repository.getMenuCategoryAndItemsForBusiness(businessId)
        }
    }

    fun handleRemoveMenuItem(menuItemId : String){
        repository.removeOneFrom(menuItemId)
    }

    fun handleAddMenuItem(menuItemId: String){
        repository.addOneTo(menuItemId)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}