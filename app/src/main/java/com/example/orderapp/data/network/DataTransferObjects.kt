package com.example.orderapp.data.network

import com.example.orderapp.data.database.BusinessDBE
import com.example.orderapp.model.Business
import com.example.orderapp.model.BusinessType
import com.example.orderapp.model.OpeningHours
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class BusinessDTO constructor(
    @Json(name = "_id")
    val businessId : String,
    @Json(name = "_humanId")
    val readableCode: String,
    val name: String,
    val description: String
    //val paymentMethods: Map<String, String>,
    //val amenities: Array<String>,
    //val openingHours: List<Array<String>>
)

fun BusinessDTO.asDomainModel(): Business {
    return Business(
        businessID = businessId,
        code = readableCode,
        name = name,
        type = BusinessType.RESTAURANT,
        description = description,
        //amenities = amenities,
        //paymentMethods = paymentMethods,
        openingHours = OpeningHours.getSampleHours(),
        rating = null
    )
}

fun BusinessDTO.asDataModel(): BusinessDBE {
    return BusinessDBE(
        businessId = businessId,
        readableCode = readableCode,
        name = name,
        description = description,
        //paymentMethods = paymentMethods,
        //amenities = amenities,
        mondayHours = "12:00 - 20:00",
        tuesdayHours = "12:00 - 20:00",
        wednesdayHours = "12:00 - 20:00",
        thursdayHours = "12:00 - 20:00",
        fridayHours = "12:00 - 20:00",
        saturdayHours = "12:00 - 20:00",
        sundayHours = "12:00 - 20:00",
        rating = null
    )
}
