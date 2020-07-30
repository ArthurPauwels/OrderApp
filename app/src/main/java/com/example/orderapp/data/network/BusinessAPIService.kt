package com.example.orderapp.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val baseURL = "http://192.168.0.200:666/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(baseURL)
    .build()



interface BusinessAPIService {
    @GET("restaurants/{id}")
    fun getBusiness(@Path("id") id : String) : Deferred<BusinessDTO>

    @GET("restaurants")
    fun getBusinesses() : Deferred<List<BusinessDTO>>

    @GET("restaurants/human/{code}")
    fun findBusinessWithCode(@Path("code") readableCode : String) : Deferred<BusinessDTO>

    @GET("categories/restaurant/{id}")
    fun getCategoriesById(@Path("id")businessId : String) : Deferred<List<CategoryDTO>>

    @GET("menuitems/category/{id}")
    fun getMenuItmesById(@Path("id")categoryId : String) : Deferred<List<MenuItemDTO>>
}

object BusinessAPI{
    val retrofitService = retrofit.create(BusinessAPIService::class.java)
}