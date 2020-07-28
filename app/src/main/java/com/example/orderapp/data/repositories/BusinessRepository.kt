package com.example.orderapp.data.repositories

import com.example.orderapp.data.api.IOrderAppAPI
import com.example.orderapp.data.api.MockAPI
import com.example.orderapp.model.Business
import com.example.orderapp.model.BusinessType

object BusinessRepository : IBusinessRepository{

    private val businesses : MutableList<Business>
    private val api : IOrderAppAPI = MockAPI()

    init {
        businesses = mutableListOf()
    }

    override fun getBusinessByID(businessId: String): Business {
        val business = businesses.filter { it.businessID == businessId }.firstOrNull()
        return if (business == null){
            val newEntry = api.getBusiness(businessId)
            businesses.add(newEntry)
            newEntry
        } else{
            business
        }

    }

    override fun getBusinessByCode(code: String): Business {
        val business = businesses.filter { it.code == code }.firstOrNull()
        return if (business == null){
            val newEntry = api.getBusinessWithCode(code)
            businesses.add(newEntry)
            newEntry
        } else{
            business
        }
    }

    override fun updateBusiness(business: Business) {

    }
}