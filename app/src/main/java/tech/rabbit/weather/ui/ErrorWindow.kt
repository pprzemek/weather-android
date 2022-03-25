package tech.rabbit.weather.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import tech.rabbit.weather.R

@Composable
fun ErrorWindow() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error_anim))
    LottieAnimation(
        composition,
        modifier = Modifier.fillMaxWidth().fillMaxHeight().background(color = MaterialTheme.colors.background)
            .semantics { contentDescription = "errorAnimation" },
        iterations = LottieConstants.IterateForever
    )
}