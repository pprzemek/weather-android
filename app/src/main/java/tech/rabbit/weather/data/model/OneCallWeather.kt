package tech.rabbit.weather.data.model

data class OneCallWeather(
    val current: CurrentWeather,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
)
