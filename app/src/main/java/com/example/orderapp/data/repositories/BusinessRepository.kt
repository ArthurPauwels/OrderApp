package com.example.orderapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.orderapp.data.database.BusinessDatabase
import com.example.orderapp.data.database.asDomainModel
import com.example.orderapp.data.network.*
import com.example.orderapp.domain.Business
import com.example.orderapp.domain.MenuCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BusinessRepository(
    private val database : BusinessDatabase
) {

    val _currentBusiness = MutableLiveData<Business>()
    val currentBusiness : LiveData<Business> get() = _currentBusiness

    val _currentCategories = MutableLiveData<List<MenuCategory>>()
    val currentCategories : LiveData<List<MenuCategory>> get() = _currentCategories

    private val businesses = Transformations.map(database.businessDAO.getAll(), {
        it.asDomainModel()
    })

    suspend fun refreshCurrentBusinessByID(id : String){
        withContext(Dispatchers.IO){
            var found = businesses.value?.filter { it.businessID == id }?.first()
            if (found == null) {
                val fromApi = BusinessAPI.retrofitService.getBusiness(id).await()
                found = fromApi.asDomainModel()
                if (found != null) {
                    database.businessDAO.insertAll(fromApi.asDataModel())
                    _currentBusiness.postValue(found)
                }
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
                if (found != null) {
                    database.businessDAO.insertAll(fromApi.asDataModel())
                    _currentBusiness.postValue(found)
                }
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

    fun addOneTo(menuItemId : String){
        var value = currentCategories.value
        value?.forEach { category ->
            category.menuItems.forEach{ menuItem ->
                if (menuItem.menuItemId == menuItemId)
                    menuItem.amount = menuItem.amount + 1
            }
        }
        _currentCategories.postValue(value)
    }

    fun removeOneFrom(menuItemId: String){
        var value = currentCategories.value
        value?.forEach { category ->
            category.menuItems.forEach{ menuItem ->
                if (menuItem.menuItemId == menuItemId && menuItem.amount > 0) menuItem.amount = menuItem.amount -1
            }
        }
        _currentCategories.postValue(value)
    }
}