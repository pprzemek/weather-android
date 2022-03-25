package tech.rabbit.weather.data.local

import android.content.Context
import android.location.Location
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.rabbit.weather.data.local.PreferenceDataSourceImpl.Keys.UNIT_SYSTEM
import tech.rabbit.weather.data.local.PreferenceDataSourceImpl.Keys.WEATHER_LOCATION_LAT
import tech.rabbit.weather.data.local.PreferenceDataSourceImpl.Keys.WEATHER_LOCATION_LON
import tech.rabbit.weather.utils.UnitSystem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceDataSourceImpl @Inject constructor(private val context: Context): PreferenceDataSource() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "weatherSettings")

    override suspend fun setUnitSystem(system: UnitSystem) {
        context.dataStore.edit { pref ->
            when (system) {
                UnitSystem.METRIC -> pref[UNIT_SYSTEM] = "metric"
                UnitSystem.IMPERIAL -> pref[UNIT_SYSTEM] = "imperial"
                UnitSystem.STANDARD -> pref[UNIT_SYSTEM] = "standard"
            }
        }
    }

    override suspend fun setWeatherLocation(location: Location) {
        context.dataStore.edit {
            it[WEATHER_LOCATION_LAT] = location.latitude
            it[WEATHER_LOCATION_LON] = location.longitude
        }
    }

    override fun getWeatherLocation(): Flow<Location> {
        return context.dataStore.data.map {
            val location = Location("")
            location.latitude = it[WEATHER_LOCATION_LAT] ?: 0.0
            location.longitude = it[WEATHER_LOCATION_LON] ?: 0.0
            location
        }
    }

    override fun getUnitSystem(): Flow<UnitSystem> {
        return context.dataStore.data.map { pref ->
            when (pref[UNIT_SYSTEM]) {
                "metric" -> UnitSystem.METRIC
                "imperial" -> UnitSystem.IMPERIAL
                "standard" -> UnitSystem.STANDARD
                else -> UnitSystem.METRIC
            }
        }
    }

    object Keys {
        val WEATHER_LOCATION_LAT = doublePreferencesKey("WEATHER_LOCATION_LAT")
        val WEATHER_LOCATION_LON = doublePreferencesKey("WEATHER_LOCATION_LON")
        val UNIT_SYSTEM = stringPreferencesKey("UNIT_SYSTEM")
    }
}