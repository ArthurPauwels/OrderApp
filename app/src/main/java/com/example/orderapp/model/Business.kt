package com.example.orderapp.model

import kotlin.collections.HashSet

class Business (
    var businessID : String,
    var name : String,
    var type : BusinessType,
    var description : String,

    var rating : Int?,
    var amenities : Set<String> = HashSet<String>(),
    var openingHours : OpeningHours
) {
    fun toDTO() : BusinessDTO{
        return BusinessDTO(name, type.toString())
    }


}

enum class BusinessType(val type : String) {
    RESTAURANT("restaurant")

}

class OpeningHours(
    val monday : List<Pair<Int, Int>>,
    val tuesday : List<Pair<Int, Int>>,
    val wednesday : List<Pair<Int, Int>>,
    val thursday : List<Pair<Int, Int>>,
    val friday : List<Pair<Int, Int>>,
    val saturday : List<Pair<Int, Int>>,
    val sunday : List<Pair<Int, Int>>
){
    companion object{
        fun parseOpeningHours(openingHoursString: String): List<Pair<Int, Int>>{
            val list : MutableList<Pair<Int, Int>> = mutableListOf()
            openingHoursString.split(",").forEach { hourPair ->
                if (!hourPair.matches(Regex("\\s*\\d\\d:\\d\\d\\s*-\\s*\\d\\d:\\d\\d\\s*"))) return@forEach
                val hours = hourPair.split("-")
                //todo ... this is just messy tbh but it'll have to do for now
                list.add(Pair<Int, Int>(hours[0].trim().replace(":", "").toInt(), hours[1].trim().replace(":", "").toInt()))
            }
            return list
        }
    }
}