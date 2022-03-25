package tech.rabbit.weather.data.remote

import tech.rabbit.weather.data.model.OneCallWeather

abstract class RemoteDataSource {
    abstract suspend fun getOneCallWeather(lat: Double, lon: Double, units: String, exclude: String): Result<OneCallWeather>
}