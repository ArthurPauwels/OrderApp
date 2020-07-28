package com.example.orderapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.orderapp.model.Business

@Dao
interface BusinessDatabaseDAO {
    @Insert
    fun insert(business: Business)

    @Update
    fun update(business: Business)

    @Query("SELECT * FROM business_table WHERE business_id = :id")
    fun getBusinessByID(id : String) : LiveData<Business>

    @Query("SELECT * FROM business_table WHERE readable_code = :humanReadableCode LIMIT 1")
    fun getBusinessByCode(humanReadableCode : String) : LiveData<Business>

    @Delete
    fun deleteBusiness(business: Business)

    @Query("DELETE FROM business_table")
    fun clearTable()

    @Query("SELECT * FROM business_table ORDER BY name ASC")
    fun getAll():LiveData<List<Business>>
}