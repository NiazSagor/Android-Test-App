package com.duodevloopers.weatherapp.repo

import androidx.lifecycle.MutableLiveData
import com.duodevloopers.weatherapp.api.RetrofitInstance
import com.duodevloopers.weatherapp.model.WeatherInfo
import com.duodevloopers.weatherapp.myapp.MyApp

class Repository {

    private val weatherLiveData = MutableLiveData<WeatherInfo>()

    suspend fun getInitialWeather() {
        val weather = RetrofitInstance.api.getWeather("23.68", "90.35", "50", MyApp.appId)
        if (weather.isSuccessful && weather.body() != null) weatherLiveData.postValue(weather.body())
    }

    fun getInitialWeatherList() = weatherLiveData


}