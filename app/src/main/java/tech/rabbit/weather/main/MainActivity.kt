package tech.rabbit.weather.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import tech.rabbit.weather.R
import tech.rabbit.weather.ui.ErrorWindow
import tech.rabbit.weather.ui.LoadingWindow
import tech.rabbit.weather.ui.theme.WeatherTheme
import tech.rabbit.weather.ui.WeatherWindow
import tech.rabbit.weather.utils.WeatherUtils
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                WindowSelection()
            }
        }

        mainViewModel.init()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onResume() {
        super.onResume()
        getLastKnownLocation()
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {
            mainViewModel.updateWeatherForLocation(it ?: WeatherUtils.defaultLocation())
        }
    }

    @Composable
    fun WindowSelection() {
        val selectedWindow = mainViewModel.windowType.collectAsState()
        Crossfade(selectedWindow.value) { type ->
            when (type) {
                MainViewModel.WindowType.LOADING -> LoadingWindow()
                MainViewModel.WindowType.MAIN -> WeatherWindow(mainViewModel)
                MainViewModel.WindowType.ERROR -> ErrorWindow()
                else -> { Timber.e("Window type not implemented") }
            }
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()){ isGranted ->
            if (!isGranted) {
                Toast.makeText(this, getString(R.string.permission_location_denied), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
}