package com.example.orderapp.data.repositories

import com.example.orderapp.model.Business
import com.example.orderapp.model.BusinessType

object BusinessRepository : IBusinessRepository{

    private val businesses : MutableList<Business>

    init {
        businesses = mutableListOf()

        var scanbusiness = Business("Android Test","TempPlace", BusinessType.RESTAURANT, "Sample description for some business", null)
        var textbusiness = Business("anID", "Testy mc testface", BusinessType.RESTAURANT, "Sample description of some business", null)

        businesses.add(scanbusiness)
        businesses.add(textbusiness)
    }

    override fun getBusinessByID(businessId: String): Business {
        return businesses.filter { it.businessID == businessId }.first()
    }

    override fun updateBusiness(business: Business) {

    }
}