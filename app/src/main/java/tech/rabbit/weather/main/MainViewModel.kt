package tech.rabbit.weather.main

import android.app.Application
import android.location.Address
import android.location.Location
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tech.rabbit.weather.data.WeatherRepositoryImpl
import tech.rabbit.weather.data.model.OneCallWeather
import javax.inject.Inject
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import tech.rabbit.weather.utils.UnitSystem
import tech.rabbit.weather.utils.WeatherUtils
import java.util.*


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepositoryImpl,
    application: Application
): AndroidViewModel(application) {
    private val _oneCallWeather = MutableStateFlow<OneCallWeather?>(null)
    val oneCallWeather = _oneCallWeather.asStateFlow()

    private val _minMaxTemp = MutableStateFlow<Pair<Double?, Double?>?>(null)
    val minMaxTemp = _minMaxTemp.asStateFlow()

    private val _weatherImg = MutableStateFlow("")
    val weatherImg = _weatherImg.asStateFlow()

    private val _city = MutableStateFlow("")
    val city = _city.asStateFlow()

    private val _unitSystem = MutableStateFlow<UnitSystem?>(null)
    val unitSystem = _unitSystem.asStateFlow()

    private val _windowType = MutableStateFlow(WindowType.LOADING)
    val windowType = _windowType.asStateFlow()

    fun init() {
        viewModelScope.launch {
            _unitSystem.value = repository.getUnitSystem()
        }
    }

    fun updateWeatherForLocation(location: Location) {
        viewModelScope.launch {
            repository.setWeatherLocation(location)
            updateCity()
            updateWeather()
        }
    }

    private fun updateWeather() {
        _windowType.value = WindowType.LOADING
        viewModelScope.launch {
            val location = withContext(Dispatchers.IO) {
                repository.getWeatherLocation()
            }
            val oneCallResult = repository.getOneCallWeather(location.latitude, location.longitude, WeatherUtils.excludeData())
            if (oneCallResult.isSuccess) {
                _oneCallWeather.value = oneCallResult.getOrNull()
                updateMaxMinTemp()
                updateWeatherImg()
                _windowType.value = WindowType.MAIN
            } else _windowType.value = WindowType.ERROR
        }
    }

    fun setUnitSystem(unitSystem: UnitSystem) {
        _unitSystem.value = unitSystem
        viewModelScope.launch {
            repository.setUnitSystem(unitSystem)
            updateWeather()
        }
    }

    private fun updateCity() {
        viewModelScope.launch {
            val location = repository.getWeatherLocation()
            val geocoder = Geocoder(getApplication(), Locale.getDefault())
            val addresses: List<Address>? = repository.getAddressAsync(location, geocoder)
            if (addresses != null && addresses.isNotEmpty()) {
                _city.value = addresses[0].locality
            }
        }
    }

    private fun updateMaxMinTemp() {
        val max = _oneCallWeather.value?.daily?.get(0)?.temp?.max
        val min = _oneCallWeather.value?.daily?.get(0)?.temp?.min
        _minMaxTemp.value = Pair(max, min)
    }

    private fun updateWeatherImg() {
        val weatherIcon = _oneCallWeather.value?.current?.weather?.get(0)?.icon
        _weatherImg.value = "https://openweathermap.org/img/wn/$weatherIcon@2x.png"
    }

    enum class WindowType {
        LOADING, MAIN, ERROR
    }
 }