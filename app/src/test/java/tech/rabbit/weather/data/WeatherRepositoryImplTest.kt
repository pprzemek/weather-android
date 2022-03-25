package tech.rabbit.weather.data

import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import tech.rabbit.weather.data.model.*
import com.google.common.truth.Truth.assertThat

class WeatherRepositoryImplTest {
    private lateinit var remoteRemoteDataSource: FakeRemoteDataSource
    private lateinit var preferenceDataSource: FakePreferenceDataSource
    private lateinit var repository: WeatherRepositoryImpl

    private val fakeOneCallWeather1 = OneCallWeather(
        current=CurrentWeather(
            feels_like=3.13,
            humidity=84,
            temp=6.3,
            visibility=10000,
            weather=listOf(Weather(main="Rain", icon="10n")),
            wind_speed=4.63),
        daily=listOf(Daily(
                dt=1645354800,
                humidity=78,
                temp=Temp(
                    day=5.84,
                    max=7.74,
                    min=2.84,),
                weather=listOf(Weather(main="Snow", icon="13d")),
                wind_speed=9.48),
            Daily(
                dt=1645441200,
                humidity=56,
                temp=Temp(day=5.87, max=9.3, min=4.17),
                weather=listOf(Weather(main="Rain", icon="10d)")),
                wind_speed=11.39)),
        hourly=listOf(
            Hourly(
                dt=1645372800,
                feels_like=2.11,
                humidity=83,
                temp=6.39,
                visibility=10000,
                weather=listOf(Weather(main="Rain", icon="10d")),
                wind_speed=7.57),
            Hourly(
                dt=1645376400,
                feels_like=1.84,
                humidity=84,
                temp=6.3,
                visibility=10000,
                weather=listOf(Weather(main="Rain", icon="10n")),
                wind_speed=8.07)),
        )

    @Before
    fun setUp() {
        remoteRemoteDataSource = FakeRemoteDataSource(fakeOneCallWeather1)
        preferenceDataSource = FakePreferenceDataSource()
        repository = WeatherRepositoryImpl(remoteRemoteDataSource, preferenceDataSource)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testEmptyWeather() = runTest {
        assertThat(remoteRemoteDataSource.getOneCallWeather(0.0, 0.0, "metrics", "").isSuccess).isTrue()
        assertThat(repository.getOneCallWeather(0.0, 0.0, "").isSuccess).isTrue()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testGetOneCallWeather() = runTest {
        val weather = repository.getOneCallWeather(0.0, 0.0, "metrics").getOrNull()
        assertThat(weather).isEqualTo(fakeOneCallWeather1)
    }
}