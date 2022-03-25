package tech.rabbit.weather

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tech.rabbit.weather.data.model.Daily
import tech.rabbit.weather.data.model.Hourly
import tech.rabbit.weather.data.model.Temp
import tech.rabbit.weather.data.model.Weather
import tech.rabbit.weather.ui.*

@RunWith(AndroidJUnit4::class)
class MainWindowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun hourlyItem() {
        composeTestRule.setContent {
            HourlyItem(Hourly(System.currentTimeMillis(), 10.0, 20, 11.0, 10000,
                listOf(Weather("Clear", "10d")), 20.0))
        }

        composeTestRule.onNodeWithContentDescription("hourly weather").assertIsDisplayed()
    }

    @Test
    fun dailyItem() {
        composeTestRule.setContent {
            DailyItem(Daily(System.currentTimeMillis(), 50,
                Temp(10.0, 11.0, 12.0),
                listOf(Weather("Clear", "10d")), 20.0))
        }

        composeTestRule.onNodeWithContentDescription("daily weather").assertIsDisplayed()
    }

    @Test
    fun currentMaxMinTempTest() {
        composeTestRule.setContent {
            CurrentMaxMinTemp(Pair(23.3, 15.0))
        }

        composeTestRule.onNodeWithText(String.format("H: %.1f° L: %.1f°", 23.3, 15.0)).assertIsDisplayed()
    }

    @Test
    fun weatherConditionTest() {
        composeTestRule.setContent {
            WeatherCondition("raining")
        }

        composeTestRule.onNodeWithText("raining").assertIsDisplayed()
    }

    @Test
    fun currentCityTest() {
        composeTestRule.setContent {
            CurrentCity("Berlin")
        }

        composeTestRule.onNodeWithText("Berlin").assertIsDisplayed()
    }
}