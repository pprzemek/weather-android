package tech.rabbit.weather.utils

import com.google.common.truth.Truth.assertThat

import org.junit.Test
import tech.rabbit.weather.utils.WeatherUtils.excludeData
import tech.rabbit.weather.utils.WeatherUtils.getUnitString
import tech.rabbit.weather.utils.WeatherUtils.getWindString

class WeatherUtilsTest {
    @Test
    fun excludeDataTest() {
        assertThat(excludeData()).isEqualTo("minutely,alerts")
    }

    @Test
    fun getWindStringTest() {
        assertThat(getWindString(12.0, UnitSystem.METRIC)).isEqualTo("12.0 m/s")
        assertThat(getWindString(12.0, UnitSystem.IMPERIAL)).isEqualTo("12.0 mi/h")
        assertThat(getWindString(12.0, UnitSystem.STANDARD)).isEqualTo("12.0 m/s")
    }

    @Test
    fun getUnitStringTest() {
        assertThat(getUnitString(UnitSystem.STANDARD)).isEqualTo("standard")
        assertThat(getUnitString(UnitSystem.IMPERIAL)).isEqualTo("imperial")
        assertThat(getUnitString(UnitSystem.METRIC)).isEqualTo("metric")
    }
}