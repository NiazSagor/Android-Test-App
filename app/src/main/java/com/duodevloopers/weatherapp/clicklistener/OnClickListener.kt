package com.duodevloopers.weatherapp.clicklistener

import com.duodevloopers.weatherapp.model.WeatherData

interface OnClickListener {
    fun onClick(weatherData: WeatherData)
}