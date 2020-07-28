package com.example.orderapp.data.repositories

import androidx.lifecycle.LiveData
import com.example.orderapp.ServiceLocator
import com.example.orderapp.data.api.IOrderAppAPI
import com.example.orderapp.data.api.IWebservice
import com.example.orderapp.data.api.MockAPI
import com.example.orderapp.model.Business
import timber.log.Timber
import java.util.concurrent.Executor

class BusinessRepository {

    //private val webservice : IWebservice = TODO()

    private val businesses : LiveData<List<Business>>
    private val api : IOrderAppAPI = MockAPI()
    private val dbDAO = ServiceLocator.database().businessDAO

    init {
        businesses = dbDAO.getAll()
    }

     fun getBusinessByID(businessId: String): LiveData<Business> {
        val business = dbDAO.getBusinessByID(businessId)
         return if ( business.value != null) {
             Timber.i("got business from db")
             business
         } else{
             val fetchedbusiness = api.getBusiness(businessId)
             Timber.i("got business from API")
             dbDAO.insert(fetchedbusiness)
             getBusinessByID(businessId)
         }
    }

     fun getBusinessByCode(code: String): LiveData<Business> {
         val business = dbDAO.getBusinessByCode(code)
         return if ( business.value != null) {
             Timber.i("got business from db")
             business
         } else{
             val fetchedbusiness = api.getBusinessWithCode(code)
             Timber.i("got business from API")
             dbDAO.insert(fetchedbusiness)
             getBusinessByID(fetchedbusiness.businessID)
         }
    }
}