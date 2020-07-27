package com.example.orderapp.model

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.util.*
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

    @SuppressLint("DefaultLocale")
    fun isOpen() : Boolean{
        val weekday = Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)
        if (weekday == null) return false
        return when (weekday.toLowerCase()) {
            "monday" -> checkDay(openingHours.monday)
            "tuesday" -> checkDay(openingHours.tuesday)
            "wednesday" -> checkDay(openingHours.wednesday)
            "thursday" -> checkDay(openingHours.thursday)
            "friday" -> checkDay(openingHours.friday)
            "saturday" -> checkDay(openingHours.saturday)
            "sunday" -> checkDay(openingHours.sunday)
            else -> false
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun checkDay(openingHoursForDay : List<Pair<Int, Int>>) : Boolean{
        val sdf = SimpleDateFormat("HHmm")
        val currentTime = sdf.format(Date()).toInt()
        var open = false
        openingHoursForDay.forEach { pair ->
            if (currentTime >= pair.first && currentTime <= pair.second) open = true
        }
        return open
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