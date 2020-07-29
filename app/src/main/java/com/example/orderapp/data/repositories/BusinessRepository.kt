package com.example.orderapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.orderapp.data.database.BusinessDatabase
import com.example.orderapp.data.database.asDomainModel
import com.example.orderapp.data.network.BusinessAPI
import com.example.orderapp.data.network.asDataModel
import com.example.orderapp.data.network.asDomainModel
import com.example.orderapp.model.Business
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BusinessRepository(
    private val database : BusinessDatabase
) {

    val _currentBusiness = MutableLiveData<Business>()
    val currentBusiness : LiveData<Business> get() = _currentBusiness

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

}