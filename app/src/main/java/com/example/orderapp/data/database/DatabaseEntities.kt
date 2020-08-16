package com.example.orderapp.data.database

import androidx.room.*
import com.example.orderapp.domain.Business
import com.example.orderapp.domain.BusinessType
import com.example.orderapp.domain.OpeningHours
import com.example.orderapp.domain.Order
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Entity(tableName = "business_table")
data class BusinessDBE constructor(
    @PrimaryKey
    val businessId : String,
    val readableCode : String,
    val name : String,
    val description : String,
    val mondayHours : String,
    val tuesdayHours : String,
    val wednesdayHours : String,
    val thursdayHours : String,
    val fridayHours : String,
    val saturdayHours : String,
    val sundayHours : String
    //val paymentMethods: String,
    //val amenities: String,
)

@Entity(tableName = "order_history_table")
data class OrderHistoryDBE constructor(
    val time : String,
    val totalPrice : Double,
    val businessName: String
) {
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}

fun List<BusinessDBE>.asDomainModel(): List<Business> {
    return map {
        Business(
            businessID = it.businessId,
            code = it.readableCode,
            name = it.name,
            type = BusinessType.RESTAURANT,
            description = it.description,
            //amenities = ,
            //paymentMethods = ,
            openingHours = OpeningHours(
                OpeningHours.parseOpeningHours(it.mondayHours),
                OpeningHours.parseOpeningHours(it.tuesdayHours),
                OpeningHours.parseOpeningHours(it.wednesdayHours),
                OpeningHours.parseOpeningHours(it.thursdayHours),
                OpeningHours.parseOpeningHours(it.fridayHours),
                OpeningHours.parseOpeningHours(it.saturdayHours),
                OpeningHours.parseOpeningHours(it.sundayHours)
            )
        )
    }
}