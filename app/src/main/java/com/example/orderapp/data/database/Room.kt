package com.example.orderapp.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BusinessDatabaseDAO {
    @Query("SELECT * FROM business_table")
    fun getAll(): LiveData<List<BusinessDBE>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg business: BusinessDBE)

    @Query("DELETE FROM business_table")
    fun clear()

    @Query("SELECT * FROM order_history_table")
    fun getAllOrderHistory(): LiveData<List<OrderHistoryDBE>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderHistory(vararg  orderHistory: OrderHistoryDBE)
}

@Database(entities = [
    BusinessDBE::class,
    OrderHistoryDBE::class
], version = 3, exportSchema = false)
abstract class BusinessDatabase : RoomDatabase() {
    abstract val businessDAO: BusinessDatabaseDAO
}

private lateinit var INSTANCE: BusinessDatabase

fun getDatabase(context: Context): BusinessDatabase {
    synchronized(BusinessDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                BusinessDatabase::class.java,
                "businesses"
            ).fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}