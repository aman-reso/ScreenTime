package com.app.screentime.ui.atom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Type of loader indicator
 */
enum class AppLoaderType {
    CIRCULAR,
    LINEAR
}

/**
 * App Loader Component
 * Reusable loading indicator using Material 3 components
 * Supports both circular and linear progress indicators
 *
 * @param type Type of loader - CIRCULAR or LINEAR
 * @param modifier Modifier for the loader
 * @param color Color of the progress indicator (optional, uses theme primary by default)
 * @param trackColor Color of the track for linear progress indicator (optional)
 * @param size Size of the circular progress indicator (for CIRCULAR type only)
 * @param height Height of the linear progress indicator (for LINEAR type only)
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppLoader(
    modifier: Modifier = Modifier,
    type: AppLoaderType = AppLoaderType.CIRCULAR,
    color: Color? = null,
    trackColor: Color? = null,
    size: Dp = 48.dp,
    height: Dp = 4.dp
) {
    val progressColor = color ?: MaterialTheme.colorScheme.primary
    val linearTrackColor = trackColor ?: MaterialTheme.colorScheme.surfaceContainerHighest

    when (type) {
        AppLoaderType.CIRCULAR -> {
            CircularWavyProgressIndicator(
                modifier = modifier.size(size),
                color = progressColor
            )
        }

        AppLoaderType.LINEAR -> {
            LinearWavyProgressIndicator(
                modifier = modifier
                    .fillMaxWidth()
                    .height(height),
                color = progressColor,
                trackColor = linearTrackColor
            )
        }
    }
}

/**
 * App Loader centered in a Box
 * Useful for full-screen loading states
 *
 * @param type Type of loader - CIRCULAR or LINEAR
 * @param modifier Modifier for the container Box
 * @param color Color of the progress indicator (optional, uses theme primary by default)
 */
@Composable
fun AppLoaderCentered(
    type: AppLoaderType = AppLoaderType.CIRCULAR,
    modifier: Modifier = Modifier,
    color: Color? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (type) {
            AppLoaderType.CIRCULAR -> {
                AppLoader(type = AppLoaderType.CIRCULAR, color = color)
            }

            AppLoaderType.LINEAR -> {
                AppLoader(type = AppLoaderType.LINEAR, color = color)
            }
        }
    }
}

