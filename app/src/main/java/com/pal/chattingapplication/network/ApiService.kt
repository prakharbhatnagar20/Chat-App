package com.pal.chattingapplication.network

import com.pal.chattingapplication.LocationData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService{
    @POST("location")
    fun createLocationEntry(@Body request: LocationData): Call<Void>
}

object RetrofitInstance{
    private const val BASE_URL = "https://needed-werewolf-bold.ngrok-free.app/"

    val api: ApiService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}