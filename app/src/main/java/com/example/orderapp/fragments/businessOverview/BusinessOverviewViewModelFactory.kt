package com.example.orderapp.fragments.businessOverview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.orderapp.data.database.BusinessDatabaseDAO
import java.lang.IllegalArgumentException

class BusinessOverviewViewModelFactory (
    private val dataSource: BusinessDatabaseDAO,
    private val application: Application
) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusinessOverviewViewModel::class.java)){
            return BusinessOverviewViewModel(dataSource, application) as T}
        throw IllegalArgumentException("unknown view model class")
    }
}