package com.example.orderapp.domain

class MenuItem  (
    val menuItemId : String,
    val name : String,
    val description : String,
    val price : Double,
    var amount: Int = 0
) : ITotalPriceProvider {
    override fun getTotalPrice(): Double = amount * price
}