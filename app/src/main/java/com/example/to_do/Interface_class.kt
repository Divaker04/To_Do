package com.example.to_do

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

const val BASE_URL="https://edamam-food-and-grocery-database.p.rapidapi.com/"
const val X_RapidAPI_Key="84576b0a1emsh71c592c146c6c99p1b5d1cjsn1a699628d3ee"
// Second:-af7557af11msh998c3480b34d0fep18941ejsnf00c9cac3d17

interface Interface_class {
    @Headers("X-RapidAPI-Key:af7557af11msh998c3480b34d0fep18941ejsnf00c9cac3d17")

    @GET("auto-complete?")
    fun searchrecomendation(@Query("q")q:String):Call<ResPonse_class>
}
object MyService{
    val newsInstance:Interface_class
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)

            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsInstance=retrofit.create(Interface_class::class.java)
    }
}