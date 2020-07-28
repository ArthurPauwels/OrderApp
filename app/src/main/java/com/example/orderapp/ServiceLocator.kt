package com.example.orderapp

import android.content.Context
import com.example.orderapp.data.database.BusinessDatabase
import com.example.orderapp.data.database.BusinessDatabaseDAO
import com.example.orderapp.data.repositories.BusinessRepository
import java.lang.IllegalStateException

object ServiceLocator {
    private var _repository : BusinessRepository? = null
    private var _database : BusinessDatabase? = null
    private var _context : Context? = null
    private var _setUp = false

    fun setup(context: Context){
        this._context = context
        _setUp = true
    }

    fun repository(): BusinessRepository {
        if (_repository == null) _repository = BusinessRepository()
        return _repository as BusinessRepository
    }

    fun database(): BusinessDatabase {
        if (!_setUp) throw IllegalStateException()
        if (_database == null) _database = BusinessDatabase.getInstance(_context!!)
        return _database as BusinessDatabase
    }
}