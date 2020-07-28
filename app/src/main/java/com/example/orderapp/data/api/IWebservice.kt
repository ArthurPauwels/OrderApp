package com.example.orderapp.data.api

import com.example.orderapp.model.Business
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IWebservice {
    @GET("/restaurants/{id}")
    fun getBusinessByID(@Path("id") userId: String): Call<Business>
}