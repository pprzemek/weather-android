package tech.rabbit.weather.data.model

data class Daily(
    val dt: Long,
    val humidity: Int,
    val temp: Temp,
    val weather: List<Weather>,
    val wind_speed: Double
)