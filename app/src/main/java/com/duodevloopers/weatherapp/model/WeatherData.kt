package com.duodevloopers.weatherapp.model

data class WeatherData(
    val clouds: Clouds,
    val coord: CoOrdinates,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val rain: Any,
    val snow: Any,
    val sys: Sys,
    val weather: List<Weather>,
    val wind: Wind
)