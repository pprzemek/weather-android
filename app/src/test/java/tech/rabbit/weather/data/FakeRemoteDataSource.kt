package tech.rabbit.weather.data

import tech.rabbit.weather.data.model.OneCallWeather
import tech.rabbit.weather.data.remote.RemoteDataSource

class FakeRemoteDataSource(var fakeOneCallWeather: OneCallWeather?): RemoteDataSource() {
    override suspend fun getOneCallWeather(
        lat: Double,
        lon: Double,
        units: String,
        exclude: String
    ): Result<OneCallWeather> {
        fakeOneCallWeather?.let { return Result.success(it) }
        return Result.failure(Exception("No one call weather :("))
    }
}