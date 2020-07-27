package com.example.orderapp.fragments.businessOverview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.orderapp.data.repositories.BusinessRepository
import com.example.orderapp.model.Business

class BusinessOverviewViewModel : ViewModel(){

    private val _business = MutableLiveData<Business>()
    val business : LiveData<Business> get() = _business

    private val _readyToOrder = MutableLiveData<Boolean>()
    val readyToOrder : LiveData<Boolean> get() = _readyToOrder

    val hasBusinessSelected : Boolean get() {
        return _business.value != null
    }

    fun handleNewCode(code : String) {
        switchBusiness(code)
    }

    fun switchBusiness(id : String){
        _business.value = BusinessRepository.getBusinessByID(id)
        _readyToOrder.value = true
    }
}