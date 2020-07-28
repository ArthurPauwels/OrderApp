package com.example.orderapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.orderapp.model.Business

@Database(entities = [Business::class], version = 1, exportSchema = false)
abstract class BusinessDatabase : RoomDatabase() {
    abstract val businessDAO : BusinessDatabaseDAO
    companion object{
        @Volatile
        private var INSTANCE : BusinessDatabase? = null

        fun getInstance(context: Context) : BusinessDatabase {
            synchronized(this){
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, BusinessDatabase::class.java, "business_database")
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}