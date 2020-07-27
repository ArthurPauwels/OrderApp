package com.example.orderapp.fragments.businessOverview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.orderapp.data.repositories.BusinessRepository
import com.example.orderapp.model.Business

class BusinessOverviewViewModel : ViewModel(){

    var business = MutableLiveData<Business?>()

    val hasBusinessSelected : Boolean get() {
        return business.value != null
    }
    init {

    }

    fun handleNewCode(code : String) {
        switchBusiness(code)
    }

    fun switchBusiness(id : String){
        business.value = BusinessRepository.getBusinessByID(id)
    }
}