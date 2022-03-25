package tech.rabbit.weather.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import tech.rabbit.weather.R
import tech.rabbit.weather.data.model.Daily
import tech.rabbit.weather.data.model.Hourly
import tech.rabbit.weather.utils.WeatherUtils

@Composable
fun HourlyItem(hourly: Hourly?) {
    Card(
        elevation = 1.dp,
        modifier = Modifier.semantics { contentDescription = "hourly weather" }
    ) {
        Column (
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val weatherIcon = hourly?.weather?.get(0)?.icon
            Text(
                WeatherUtils.timestampToHour(hourly?.dt),
                style = TextStyle(fontSize = 12.sp)
            )
            Text(stringResource(R.string.temperature, hourly?.temp ?: 0))
            Image(
                painter = rememberGlidePainter("https://openweathermap.org/img/wn/$weatherIcon@2x.png"),
                contentDescription = "Weather",
                modifier = Modifier
                    .size(36.dp)
            )
        }
    }
}

@Composable
fun DailyItem(dailyWeather: Daily?) {
    Card(modifier = Modifier.fillMaxWidth().semantics { contentDescription = "daily weather" }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val weatherIcon = dailyWeather?.weather?.get(0)?.icon
            Image(
                painter = rememberGlidePainter("https://openweathermap.org/img/wn/$weatherIcon@2x.png"),
                contentDescription = "Weather",
                modifier = Modifier
                    .size(56.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(WeatherUtils.timestampToDayOfTheWeek(dailyWeather?.dt))
                Text(stringResource(R.string.temperature, dailyWeather?.temp?.day ?: 0))
            }
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(stringResource(id = R.string.weather_humidity))
                Text(dailyWeather?.humidity.toString() + " %")
            }
        }
    }
}