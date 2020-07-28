package com.example.orderapp.model

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.room.*
import java.util.*
import kotlin.collections.HashSet

@Entity(tableName = "business_table")
data class Business (
    @PrimaryKey
    @ColumnInfo(name = "business_id")
    var businessID : String,

    @ColumnInfo(name = "readable_code")
    var code : String,

    @ColumnInfo(name = "name")
    var name : String,

    //@ColumnInfo(name = "business_type")
    @Ignore
    var type : BusinessType,

    @ColumnInfo(name = "description")
    var description : String,

    //@ColumnInfo(name = "amenities")
    @Ignore
    var amenities : Set<String> = HashSet<String>(),

    @Ignore
    var openingHours : OpeningHours,

    @ColumnInfo(name = "ratingDB")
    var rating : Int? = null
) {
    constructor(businessID: String, code: String, name: String, description: String, rating: Int?):this(businessID, code, name, BusinessType.RESTAURANT, description, HashSet<String>(), OpeningHours.getSampleHours(), rating)

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

//@Entity(tableName = "opening_hours_table")
class OpeningHours(
    //@PrimaryKey(autoGenerate = true)
    //var hoursId : Long = 0L,

    //@ColumnInfo(name = "monday")
    val monday : List<Pair<Int, Int>>,

    //@ColumnInfo(name = "tuesday")
    val tuesday : List<Pair<Int, Int>>,

    //@ColumnInfo(name = "wednesday")
    val wednesday : List<Pair<Int, Int>>,

    //@ColumnInfo(name = "thursday")
    val thursday : List<Pair<Int, Int>>,

    //@ColumnInfo(name = "friday")
    val friday : List<Pair<Int, Int>>,

    //@ColumnInfo(name = "saturday")
    val saturday : List<Pair<Int, Int>>,

    //@ColumnInfo(name = "sunday")
    val sunday : List<Pair<Int, Int>>
){
    companion object{
        //todo remove this shit
        fun getSampleHours() : OpeningHours {
            val daily = parseOpeningHours("12:00 - 13:00")
            return OpeningHours(daily, daily, daily, daily, daily, daily, daily)
        }
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