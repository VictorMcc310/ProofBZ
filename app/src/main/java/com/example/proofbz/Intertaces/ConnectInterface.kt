package com.example.proofbz.Intertaces

import retrofit2.Call
import retrofit2.http.*


interface ConnectInterface {
//



    @GET("forecast")
    fun forecastd(@FieldMap user: HashMap<String, Any>): Call<String?>?

    @GET("/example/")
    fun forecast1(@Query(value = "latlng", encoded = true) latlng: String?)

    @GET("forecast")
     fun forecast(
        @Query("latitude") latitude:String,
        @Query("longitude") longitude:String,
        @Query("current") current:String,
        @Query("hourly") hourly:String,
    ): Call<String?>?

}