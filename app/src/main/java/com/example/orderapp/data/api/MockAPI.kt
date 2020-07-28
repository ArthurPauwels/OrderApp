package com.example.orderapp.data.api

import com.example.orderapp.model.Business
import com.example.orderapp.model.BusinessType
import com.example.orderapp.model.OpeningHours

class MockAPI : IOrderAppAPI{
    override fun getBusiness(id: String): Business {
        val openingHours = OpeningHours(
            OpeningHours.parseOpeningHours("12:00 - 15:30, 17:00 - 22:00"),
            OpeningHours.parseOpeningHours("12:00 - 15:30, 17:00 - 22:00"),
            OpeningHours.parseOpeningHours("12:00 - 15:30, 17:00 - 22:00"),
            OpeningHours.parseOpeningHours("12:00 - 15:30, 17:00 - 22:00"),
            OpeningHours.parseOpeningHours("12:00 - 15:30, 17:00 - 22:00"),
            OpeningHours.parseOpeningHours("closed"),
            OpeningHours.parseOpeningHours(" 17:00 - 22:00")
        )

        return  Business(id,"TempPlace", "SOMECODE", BusinessType.RESTAURANT, "Sample description for some business", setOf("Toilet", "Wheelchair access", "Airco"),openingHours)
    }

    override fun getBusinessWithCode(code: String): Business {

        val openingHours = OpeningHours(
            OpeningHours.parseOpeningHours("12:00 - 15:30, 17:00 - 22:00"),
            OpeningHours.parseOpeningHours("12:00 - 15:30, 17:00 - 22:00"),
            OpeningHours.parseOpeningHours("12:00 - 15:30, 17:00 - 22:00"),
            OpeningHours.parseOpeningHours("12:00 - 15:30, 17:00 - 22:00"),
            OpeningHours.parseOpeningHours("12:00 - 15:30, 17:00 - 22:00"),
            OpeningHours.parseOpeningHours("closed"),
            OpeningHours.parseOpeningHours(" 17:00 - 22:00")
        )

        return  Business("Android Test", code,"TempPlace", BusinessType.RESTAURANT, "Sample description for some business", setOf("Toilet", "Wheelchair access", "Airco"),openingHours)
    }
}