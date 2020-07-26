package com.example.orderapp.model

class Business (
    var businessID : String,
    var name : String,
    var type : BusinessType,
    var description : String,

    var rating : Int?,
    var amenities : Set<String> = HashSet<String>()
) {
    fun toDTO() : BusinessDTO{
        return BusinessDTO(name, type.toString())
    }


}

enum class BusinessType(val type : String) {
    RESTAURANT("restaurant")

}