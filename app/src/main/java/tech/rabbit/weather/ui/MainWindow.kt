package tech.rabbit.weather.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.glide.rememberGlidePainter
import tech.rabbit.weather.R
import tech.rabbit.weather.data.model.Hourly
import tech.rabbit.weather.data.model.OneCallWeather
import tech.rabbit.weather.main.MainViewModel
import tech.rabbit.weather.utils.WeatherUtils
import tech.rabbit.weather.utils.UnitSystem

@Composable
fun WeatherWindow(mainViewModel: MainViewModel) {
    val oneCallWeather: OneCallWeather? by mainViewModel.oneCallWeather.observeAsState()
    val minMaxTemp: Pair<Double?, Double?>? by mainViewModel.minMaxTemp.observeAsState()
    val weatherImgUrl: String? by mainViewModel.weatherImg.observeAsState()
    val city: String? by mainViewModel.city.observeAsState()
    val unitSystem: UnitSystem? by mainViewModel.unitSystem.observeAsState()
    Column (
        modifier = Modifier.background(color = MaterialTheme.colors.background)
    ){
        Row {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Spacer(Modifier.size(48.dp))
                CurrentCity(city)
                CurrentTemperature(oneCallWeather?.current?.temp)
                Spacer(Modifier.size(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    WeatherImg(weatherImgUrl)
                    WeatherCondition(oneCallWeather?.current?.weather?.get(0)?.main)
                }
                Spacer(Modifier.size(4.dp))
                CurrentMaxMinTemp(minMaxTemp)
                Spacer(Modifier.size(28.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                SelectedMetricSystem(unitSystem, onUnitSystemClicked = {
                    mainViewModel.setUnitSystem(it)
                })
                Spacer(Modifier.size(32.dp))
                WeatherAnim()
            }
        }
        Column {
            Divider(color = Color.Gray, thickness = 1.dp)
            LazyColumn(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp)
            ) {
                item { WeatherDetails(oneCallWeather, unitSystem) }
                item { WeatherHourly(oneCallWeather?.hourly) }
                items(count = oneCallWeather?.daily?.size ?: 0) {
                    DailyItem(oneCallWeather?.daily?.get(it))
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
fun SelectedMetricSystem(unitSystem: UnitSystem?, onUnitSystemClicked: (UnitSystem) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val standardSelected = unitSystem == UnitSystem.STANDARD
        val imperialSelected = unitSystem == UnitSystem.IMPERIAL
        val metricSelected = unitSystem == UnitSystem.METRIC
        Text(
            text = stringResource(id = R.string.kelvin_degree),
            style = TextStyle(color = if (standardSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSecondary,
                fontSize = 22.sp, fontStyle = FontStyle.Italic,
            fontWeight = if (standardSelected) FontWeight.Black else FontWeight.Normal),
            modifier = Modifier.clickable(onClick = {
                onUnitSystemClicked(UnitSystem.STANDARD)
            })
        )
        MetricSystemDivider(modifier = Modifier.align(alignment = Alignment.CenterVertically))
        Text(
            text = stringResource(id = R.string.fahrenheit_degree),
            style = TextStyle(color = if (imperialSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSecondary,
                fontSize = 22.sp, fontStyle = FontStyle.Italic,
            fontWeight = if (imperialSelected) FontWeight.Bold else FontWeight.Normal),
            modifier = Modifier.clickable(onClick = {
                onUnitSystemClicked(UnitSystem.IMPERIAL)
            })
        )
        MetricSystemDivider(modifier = Modifier.align(alignment = Alignment.CenterVertically))
        Text(
            text = stringResource(id = R.string.celsius_degree),
            style = TextStyle(color = if (metricSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSecondary,
                fontSize = 22.sp, fontStyle = FontStyle.Italic,
            fontWeight = if (metricSelected) FontWeight.Bold else FontWeight.Normal),
            modifier = Modifier.clickable(onClick = {
                onUnitSystemClicked(UnitSystem.METRIC)
            })
        )
    }
}

@Composable
fun MetricSystemDivider(modifier: Modifier) {
    Divider(modifier = modifier
        .height(20.dp)
        .width(2.dp)
    )
}

@Composable
fun WeatherAnim() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.weather_anim))
    LottieAnimation(
        composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.height(dimensionResource(id = R.dimen.weather_anim_height))
    )
}

@Preview
@Composable
fun CurrentCity(@PreviewParameter(CityProvider::class) city: String?) {
    Row(
        modifier = Modifier.semantics { contentDescription = "city" }
    ) {
        Icon(
            Icons.Outlined.NearMe, contentDescription = "NearMe",
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.icon_size_small))
                .align(Alignment.CenterVertically))
        Text(
            text = city ?: "",
            style = TextStyle(color = MaterialTheme.colors.onBackground, fontSize = 22.sp, fontStyle = FontStyle.Italic)
        )
    }
}

@Preview
@Composable
fun CurrentTemperature(@PreviewParameter(TempProvider::class) currentTemp: Double?) {
    Text(
        text = stringResource(R.string.temperature, currentTemp ?: 0f),
        maxLines = 1,
        style = TextStyle(color = MaterialTheme.colors.onSecondary, fontSize = 56.sp, fontWeight = FontWeight.Bold)
    )
}

@Preview
@Composable
fun CurrentMaxMinTemp(@PreviewParameter(MaxMinProvider::class) maxMinTemp: Pair<Double?, Double?>?) {
    Text(
        text = stringResource(R.string.temperature_max_min,
            maxMinTemp?.first ?: 0f, maxMinTemp?.second ?: 0f),
        style = TextStyle(color = MaterialTheme.colors.onBackground)
    )
}

@Composable
fun WeatherImg(weatherImgUrl: String?) {
    Image(
        painter = rememberGlidePainter(weatherImgUrl),
        contentDescription = "Weather",
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.weather_img_size))
            .offset(x = (-10).dp)
            .semantics { contentDescription = "weather" },
    )
}

@Preview
@Composable
fun WeatherCondition(@PreviewParameter(WeatherConditionProvider::class) weatherCondition: String?) {
    Text(
        text = weatherCondition ?: "",
        style = TextStyle(color = MaterialTheme.colors.onBackground)
    )
}

@Composable
fun WeatherDetails(oneCallWeather: OneCallWeather?, unitSystem: UnitSystem?) {
    Column(
        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
    ) {
        Row {
            DetailsIcon(Modifier.align(Alignment.CenterVertically), Icons.Outlined.Air, "Air")
            DetailsText(
                modifier = Modifier.weight(1f),
                R.string.weather_wind_speed, WeatherUtils.getWindString(oneCallWeather?.current?.wind_speed, unitSystem))
            DetailsIcon(Modifier.align(Alignment.CenterVertically), Icons.Outlined.Thermostat, "Thermostat")
            DetailsText(
                modifier = Modifier.weight(1f),
                R.string.weather_feels_like, stringResource(R.string.temperature, oneCallWeather?.current?.feels_like ?: 0f))
        }
        Divider(color = Color.Gray, thickness = 1.dp)
        Row {
            DetailsIcon(Modifier.align(Alignment.CenterVertically), Icons.Outlined.Visibility, "Visibility")
            DetailsText(
                modifier = Modifier.weight(1f),
                R.string.weather_visibility, (oneCallWeather?.current?.visibility?.div(1000)).toString() + " km")
            DetailsIcon(Modifier.align(Alignment.CenterVertically), Icons.Outlined.WaterDrop, "WaterDrop")
            DetailsText(
                modifier = Modifier.weight(1f),
                R.string.weather_humidity, oneCallWeather?.current?.humidity.toString() + " %")
        }
    }
}

@Composable
fun DetailsIcon(modifier: Modifier, icon: ImageVector, contentDescription: String) {
    Icon(
        icon, contentDescription = contentDescription,
        tint = MaterialTheme.colors.onBackground,
        modifier = modifier
            .size(dimensionResource(id = R.dimen.icon_size_normal))
    )
}

@Composable
fun DetailsText(modifier: Modifier = Modifier, @StringRes labelId: Int, value: String?) {
    Column(
        modifier = modifier.padding(6.dp)
    ) {
        Text(stringResource(id = labelId), style = TextStyle(color = MaterialTheme.colors.onPrimary))
        Text(value ?: "", style = TextStyle(color = MaterialTheme.colors.onPrimary))
    }
}

@Composable
fun WeatherHourly(hourlyWeather: List<Hourly>?) {
    LazyRow (
        modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
    ) {
        items(count = hourlyWeather?.size ?: 0) {
            HourlyItem(hourlyWeather?.get(it))
            Spacer(Modifier.width(4.dp))
        }
    }
}

class WeatherConditionProvider: PreviewParameterProvider<String> {
    override val values: Sequence<String> = sequenceOf("clear sky", "raining")
}

class MaxMinProvider: PreviewParameterProvider<Pair<Double?, Double?>?> {
    override val values: Sequence<Pair<Double?, Double?>?> = sequenceOf(Pair(10.0, -2.3))
}

class TempProvider: PreviewParameterProvider<Double> {
    override val values: Sequence<Double> = sequenceOf(12.0)
}

class CityProvider: PreviewParameterProvider<String> {
    override val values: Sequence<String> = sequenceOf("Berlin")
}