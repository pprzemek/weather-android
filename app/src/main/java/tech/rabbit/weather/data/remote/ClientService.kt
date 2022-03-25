package tech.rabbit.weather.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import tech.rabbit.weather.data.model.OneCallWeather

interface ClientService {
    @GET("onecall")
    suspend fun getOneCallWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String): OneCallWeather
}