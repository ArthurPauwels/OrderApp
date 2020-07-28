package com.example.orderapp.data.repositories

import com.example.orderapp.model.Business

interface IBusinessRepository {
    fun getBusinessByID(businessId: String): Business
    fun getBusinessByCode(code: String): Business
    fun updateBusiness(business: Business)
}