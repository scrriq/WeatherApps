package com.example.weatherapp.Models

data class WeatherModel(
    var currentDay: Int,
    val location: LocationModel,
    val current: CurrentModel,
    val forecast: ForecastDayModel,
)

data class LocationModel(
    val name: String,
)

data class CurrentModel(
    val region: String,
    val last_updated: String,
    val condition: ConditionModel,
    val temp_c: Float,
)

data class ConditionModel(
    val text: String,
    val icon: String,
)

data class ForecastDayModel(
    val forecastday: List<DataModel>,
)

data class DataModel(
    val date: String,
    val day: DayModel,
    val hour: List<HourModel>
)

data class DayModel(
    val condition: ConditionModel,
    val maxtemp_c: Float,
    val mintemp_c: Float,
)

data class HourModel(
    val time: String,
    val temp_c: Float,
    val condition: ConditionModel
)

data class DefaultWeatherModel(
    val id: Int,
    val time: String,
    val condition: String,
    val currentTemp: String,
    val maxTemp: String,
    val minTemp: String,
    val imageUrl: String,
)
