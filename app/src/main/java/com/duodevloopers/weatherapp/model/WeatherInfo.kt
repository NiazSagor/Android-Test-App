package com.duodevloopers.weatherapp.model

data class WeatherInfo(
    val cod: String,
    val count: Int,
    val list: List<WeatherData>,
    val message: String
)