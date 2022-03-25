package tech.rabbit.weather.data.local

import android.location.Location
import kotlinx.coroutines.flow.Flow
import tech.rabbit.weather.utils.UnitSystem

abstract class PreferenceDataSource {
    abstract fun getUnitSystem(): Flow<UnitSystem>
    abstract suspend fun setUnitSystem(system: UnitSystem)
    abstract fun getWeatherLocation(): Flow<Location>
    abstract suspend fun setWeatherLocation(location: Location)
}