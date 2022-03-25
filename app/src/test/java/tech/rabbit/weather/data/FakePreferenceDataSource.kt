package tech.rabbit.weather.data

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tech.rabbit.weather.data.local.PreferenceDataSource
import tech.rabbit.weather.utils.UnitSystem
import tech.rabbit.weather.utils.WeatherUtils.defaultLocation

class FakePreferenceDataSource: PreferenceDataSource() {
    override fun getUnitSystem(): Flow<UnitSystem> {
        return flow {
            emit(UnitSystem.IMPERIAL)
        }
    }

    override suspend fun setUnitSystem(system: UnitSystem) { }

    override fun getWeatherLocation(): Flow<Location> {
        return flow {
            emit(defaultLocation())
        }
    }

    override suspend fun setWeatherLocation(location: Location) {}
}