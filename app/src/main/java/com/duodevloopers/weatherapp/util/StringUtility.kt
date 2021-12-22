package com.duodevloopers.weatherapp.util

class StringUtility {

    companion object {
        fun toCelsius(t : Double) : String  {
            return String.format("%.2f°C",t - 273.15)
        }

    }

}