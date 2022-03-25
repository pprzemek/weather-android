package tech.rabbit.weather.data

import android.location.Address
import android.location.Geocoder
import android.location.Location
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import tech.rabbit.weather.data.local.PreferenceDataSource
import tech.rabbit.weather.data.model.OneCallWeather
import tech.rabbit.weather.data.remote.RemoteDataSource
import tech.rabbit.weather.utils.WeatherUtils.getUnitString
import tech.rabbit.weather.utils.UnitSystem
import timber.log.Timber
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val preferenceDataSourceImpl: PreferenceDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): WeatherRepository() {

    init {
        Timber.d("Inject repository")
    }

    override suspend fun getOneCallWeather(
        latitude: Double,
        longitude: Double,
        exclude: String
    ): Result<OneCallWeather> {
        return remoteDataSource.getOneCallWeather(latitude, longitude,
            getUnitString(preferenceDataSourceImpl.getUnitSystem().first()), exclude)
    }

    override suspend fun setUnitSystem(system: UnitSystem) {
        preferenceDataSourceImpl.setUnitSystem(system)
    }

    override suspend fun getUnitSystem(): UnitSystem {
        return withContext(ioDispatcher) {
            preferenceDataSourceImpl.getUnitSystem().first()
        }
    }

    override suspend fun setWeatherLocation(location: Location) {
        preferenceDataSourceImpl.setWeatherLocation(location)
    }

    override suspend fun getWeatherLocation(): Location {
        return withContext(ioDispatcher) {
            preferenceDataSourceImpl.getWeatherLocation().first()
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getAddressAsync(location: Location, geocoder: Geocoder): List<Address>? =
        withContext(ioDispatcher) {
            geocoder.getFromLocation(location.latitude, location.longitude, 1)
        }
}