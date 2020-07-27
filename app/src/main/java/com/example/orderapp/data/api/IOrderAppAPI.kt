package com.example.orderapp.data.api

import com.example.orderapp.model.Business

interface IOrderAppAPI {
    fun getBusiness(id : String) : Business
    fun getBusinessWithCode(code : String) : Business
}