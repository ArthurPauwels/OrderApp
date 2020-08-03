package com.example.orderapp.domain

import java.time.LocalDateTime
import java.util.*

class Order (
    val items : Map<MenuItem, Int>,
    val date : Date = Date()
) : ITotalPriceProvider {
    override fun getTotalPrice(): Double {
        var total = 0.0
        items.forEach{(item, amount) ->
            total += (item.price * amount)
        }
        return total
    }
}