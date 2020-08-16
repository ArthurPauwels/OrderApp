package com.example.orderapp.fragments.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.orderapp.data.database.getDatabase
import com.example.orderapp.data.repositories.BusinessRepository

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repository = BusinessRepository(database)

    val orderHistory = repository.orderHistory
}