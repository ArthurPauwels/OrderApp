package com.example.orderapp.data.database

import androidx.room.*
import com.example.orderapp.domain.Business
import com.example.orderapp.domain.BusinessType
import com.example.orderapp.domain.OpeningHours

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
    val sundayHours : String,
    //val paymentMethods: String,
    //val amenities: String,
    val rating : Int?
)

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
            ),
            rating = it.rating
        )
    }
}