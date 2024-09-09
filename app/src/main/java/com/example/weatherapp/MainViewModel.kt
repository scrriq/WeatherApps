package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Models.WeatherModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class MainViewModel @Inject constructor(
    private val mainApi: MainApi
) : ViewModel() {
    val liveDataWeather = MutableLiveData<WeatherModel>()
    fun getWeatherForecast(
        key: String,
        q: String,
        days: String,
        aqi: String,
        alerts: String
    ) = viewModelScope.launch {
        liveDataWeather.value = mainApi.getWeatherData(key, q, days, aqi, alerts)
    }
}