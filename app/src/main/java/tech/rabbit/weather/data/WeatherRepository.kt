package tech.rabbit.weather.data

import android.location.Address
import android.location.Geocoder
import android.location.Location
import tech.rabbit.weather.data.model.OneCallWeather
import tech.rabbit.weather.utils.UnitSystem

abstract class WeatherRepository {
    abstract suspend fun getOneCallWeather(latitude: Double, longitude: Double, exclude: String): Result<OneCallWeather>
    abstract suspend fun setUnitSystem(system: UnitSystem)
    abstract suspend fun getUnitSystem(): UnitSystem
    abstract suspend fun setWeatherLocation(location: Location)
    abstract suspend fun getWeatherLocation(): Location
    abstract suspend fun getAddressAsync(location: Location, geocoder: Geocoder): List<Address>?
}