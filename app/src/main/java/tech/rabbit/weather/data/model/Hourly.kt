package tech.rabbit.weather.data.model

data class Hourly(
    val dt: Long,
    val feels_like: Double,
    val humidity: Int,
    val temp: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_speed: Double
)