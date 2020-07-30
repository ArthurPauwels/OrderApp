package com.example.orderapp.domain

class MenuCategory (
    val categoryId : String,
    val name : String,
    var menuItems : List<MenuItem>
) {
}