package com.duodevloopers.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duodevloopers.weatherapp.model.Weather
import com.duodevloopers.weatherapp.model.WeatherData
import com.duodevloopers.weatherapp.model.WeatherInfo
import com.duodevloopers.weatherapp.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val repository: Repository = Repository()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getInitialWeather()
        }
    }

    fun getInitialCityWeather(): LiveData<WeatherInfo> = repository.getInitialWeatherList()

    private lateinit var selectedCity : WeatherData

    fun setSelectedCity(city : WeatherData) {
        this.selectedCity = city
    }

    fun getSelectedCity() : WeatherData = selectedCity

}