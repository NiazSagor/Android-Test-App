package com.duodevloopers.weatherapp.api

import com.duodevloopers.weatherapp.model.WeatherInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    @GET("find")
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("cnt") cnt: String,
        @Query("appid") appid: String
    ) : Response<WeatherInfo>
}