package tech.rabbit.weather.utils

import android.location.Location
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

object WeatherUtils {
    fun defaultLocation(): Location = Location("MyLocation")
        .apply { latitude = 51.113726; longitude = 17.022000 }

    fun excludeData(): String = "minutely,alerts"

    fun timestampToHour(timestamp: Long?): String {
        // UNIX timestamp in seconds to milliseconds
        val correctTimestamp = timestamp?.times(1000) ?: System.currentTimeMillis()
        val sdf = SimpleDateFormat(
            if (DateUtils.isToday(correctTimestamp)) "HH:mm" else "EE HH:mm", Locale.getDefault())
        val date = Date(correctTimestamp)
        return sdf.format(date)
    }

    fun timestampToDayOfTheWeek(timestamp: Long?): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        val date = Date(timestamp?.times(1000) ?: System.currentTimeMillis())
        return sdf.format(date)
    }

    fun getWindString(windSpeed: Double?, unitSystem: UnitSystem?): String =
        windSpeed.toString() + if (unitSystem == UnitSystem.IMPERIAL) " mi/h" else " m/s"

    fun getUnitString(system: UnitSystem): String {
        return when (system) {
            UnitSystem.METRIC -> "metric"
            UnitSystem.IMPERIAL -> "imperial"
            UnitSystem.STANDARD -> "standard"
        }
    }
}