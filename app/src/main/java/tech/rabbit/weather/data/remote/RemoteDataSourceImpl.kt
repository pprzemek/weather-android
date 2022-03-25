package tech.rabbit.weather.data.remote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import tech.rabbit.weather.data.model.OneCallWeather
import timber.log.Timber
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    retrofit: Retrofit,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): RemoteDataSource() {
    private val apiService: ClientService = retrofit.create(ClientService::class.java)

    override suspend fun getOneCallWeather(
        lat: Double,
        lon: Double,
        units: String,
        exclude: String
    ): Result<OneCallWeather> {
        return withContext(ioDispatcher) {
            try {
                Timber.d("Getting one time call weather")
                return@withContext Result.success(apiService.getOneCallWeather(lat, lon, units, exclude))
            } catch (e: Exception) {
                Timber.d("Exception while getting one call weather.", e)
                return@withContext Result.failure(e)
            }
        }
    }
}