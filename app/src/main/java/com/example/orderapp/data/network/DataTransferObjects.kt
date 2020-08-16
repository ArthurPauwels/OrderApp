package com.example.orderapp.data.network

import com.example.orderapp.data.database.BusinessDBE
import com.example.orderapp.domain.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class BusinessDTO constructor(
    @Json(name = "_id")
    val businessId : String,
    @Json(name = "_humanId")
    val readableCode: String,
    val name: String,
    val description: String,
    //val paymentMethods: Map<String, String>,
    //val amenities: Array<String>,
    val openingHours: OpeningHoursDTO
)

@JsonClass(generateAdapter = true)
data class OpeningHoursDTO constructor(
    val monday : String,
    val thursday : String,
    val wednesday : String,
    val tuesday : String,
    val friday : String,
    val saturday : String,
    val sunday : String
)

@JsonClass(generateAdapter = true)
data class CategoryDTO constructor(
    @Json(name = "_id")
    val categoryId : String,
    val name : String,
    @Json(name = "_restoId")
    val BusinessId : String
)

@JsonClass(generateAdapter = true)
data class MenuItemDTO constructor(
    @Json(name = "_id")
    val menuItemId : String,
    val name: String,
    val description: String,
    val price : Double,
    @Json(name = "_categoryId")
    val categoryId: String
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
        openingHours = OpeningHours.getSampleHours()
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
        sundayHours = "12:00 - 20:00"
    )
}

fun List<MenuItemDTO>.menuItemAsDomainModel(): List<MenuItem> {
    return map {
        MenuItem(
            menuItemId = it.menuItemId,
            name = it.name,
            description = it.description,
            price = it.price
        )
    }
}

fun List<CategoryDTO>.categoryAsDomainModel(): List<MenuCategory>{
    return map {
        MenuCategory(
            categoryId = it.categoryId,
            name = it.name,
            menuItems = listOf()
        )
    }
}

