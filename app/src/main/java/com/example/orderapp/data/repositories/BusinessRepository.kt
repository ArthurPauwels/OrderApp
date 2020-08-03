package com.example.orderapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.orderapp.data.database.BusinessDatabase
import com.example.orderapp.data.database.asDomainModel
import com.example.orderapp.data.network.*
import com.example.orderapp.domain.Business
import com.example.orderapp.domain.MenuCategory
import com.example.orderapp.domain.MenuItem
import com.example.orderapp.domain.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BusinessRepository(
    private val database : BusinessDatabase
) {

    private val _currentBusiness = MutableLiveData<Business>()
    val currentBusiness : LiveData<Business> get() = _currentBusiness

    private val _currentCategories = MutableLiveData<List<MenuCategory>>()
    val currentCategories : LiveData<List<MenuCategory>> get() = _currentCategories

    private val _currentOrder = MutableLiveData<Order>()
    val currentOrder : LiveData<Order> get() = _currentOrder

    private val businesses = Transformations.map(database.businessDAO.getAll(), {
        it.asDomainModel()
    })

    suspend fun refreshCurrentBusinessByID(id : String){
        withContext(Dispatchers.IO){
            var found = businesses.value?.filter { it.businessID == id }?.first()
            if (found == null) {
                val fromApi = BusinessAPI.retrofitService.getBusiness(id).await()
                found = fromApi.asDomainModel()
                database.businessDAO.insertAll(fromApi.asDataModel())
                _currentBusiness.postValue(found)
            }else {
                _currentBusiness.postValue(found)
            }
        }
    }

    suspend fun refreshCurrentBusinessByCode(code : String){
        withContext(Dispatchers.IO){
            var found = businesses.value?.filter { it.code == code }?.first()
            if (found == null) {
                val fromApi = BusinessAPI.retrofitService.findBusinessWithCode(code).await()
                found = fromApi.asDomainModel()
                database.businessDAO.insertAll(fromApi.asDataModel())
                _currentBusiness.postValue(found)
            }else {
                _currentBusiness.postValue(found)
            }
        }
    }

    suspend fun getMenuCategoryAndItemsForBusiness(id : String){
        withContext(Dispatchers.IO){
            //I know this is terrible API architecture, but this is me dealing with it... this should have been a singular call
            val fromApi = BusinessAPI.retrofitService.getCategoriesById(id).await()
            val categories = fromApi.categoryAsDomainModel()
            categories.forEach {category ->
                category.menuItems = BusinessAPI.retrofitService.getMenuItmesById(category.categoryId).await().menuItemAsDomainModel()
            }
            _currentCategories.postValue(categories)
        }
    }

    fun addOneToOrder(menuItemId : String){
        removeOrAddOneToOrder(menuItemId, true)
        _currentCategories.value?.forEach { cat ->
            cat.menuItems.forEach { it ->
                if (it.menuItemId == menuItemId) it.amount ++
            }
        }
    }

    fun removeOneFromOrder(menuItemId: String){
        removeOrAddOneToOrder(menuItemId, false)
        _currentCategories.value?.forEach { cat ->
            cat.menuItems.forEach { it ->
                if (it.menuItemId == menuItemId && it.amount > 0) it.amount --
            }
        }
    }

    private fun findMenuItemInCategories(menuItemId: String) : MenuItem? {
        currentCategories.value?.forEach { category ->
            category.menuItems.forEach{ menuItem ->
                if (menuItem.menuItemId == menuItemId)
                    return menuItem
            }
        }
        return null
    }

    private fun removeOrAddOneToOrder(menuItemId: String, add: Boolean){
        val updatedOrderItems : MutableMap<MenuItem, Int> = mutableMapOf()
        currentOrder.value?.items?.let { updatedOrderItems.putAll(it) }
        val item = findMenuItemInCategories(menuItemId)
        item?.let {
            val oldAmount = updatedOrderItems.remove(it) ?: 0
            if(add) updatedOrderItems.put(it, oldAmount + 1)
            else { if (oldAmount > 1) updatedOrderItems.put(it, oldAmount - 1) else {} }
        }
        _currentOrder.postValue(Order(updatedOrderItems))
    }
}