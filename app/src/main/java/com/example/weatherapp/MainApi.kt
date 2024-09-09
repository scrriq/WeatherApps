package com.example.weatherapp

import com.example.weatherapp.Models.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface MainApi {
    @GET("forecast.json")
    suspend fun getWeatherData(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: String,
        @Query("aqi") aqi: String,
        @Query("alerts") alerts: String,
    ) : WeatherModel
}