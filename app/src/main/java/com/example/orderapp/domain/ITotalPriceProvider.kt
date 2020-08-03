package com.example.orderapp.domain

import java.util.stream.Collectors

interface ITotalPriceProvider {
    fun getTotalPrice() : Double
}

fun Collection<ITotalPriceProvider>.getTotalPrice() : Double {
    return stream().collect(Collectors.summingDouble({it.getTotalPrice()}))
}