package com.telekom.odsystem.extensions

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.speech.SpeechRecognizer
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import java.math.RoundingMode
import kotlin.math.roundToInt

@ColorInt
fun Int.adjustAlpha(factor: Float): Int {
    val alpha = (Color.alpha(this) * factor).roundToInt()
    val red: Int = Color.red(this)
    val green: Int = Color.green(this)
    val blue: Int = Color.blue(this)
    return Color.argb(alpha, red, green, blue)
}

fun Context.hideKeyboard(view: View) {
    val inputManager: InputMethodManager? =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputManager?.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Context.isSpeechRecognitionAvailable() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    SpeechRecognizer.isRecognitionAvailable(this) || SpeechRecognizer.isOnDeviceRecognitionAvailable(
        this
    )
} else {
    SpeechRecognizer.isRecognitionAvailable(this)
}

fun Float.upToDecimals(fractionDecimal: Int): Float {
    return this.toBigDecimal()
        .setScale(fractionDecimal, RoundingMode.HALF_UP).toFloat()
}

fun String.isDecimalNumber(): Boolean {
    val regex = """-?\d*\.?\d*""".toRegex() // Matches negatives, decimals, or integers
    return this.isEmpty() || this.matches(regex)
}

/**
 * Converts a nullable callback that takes a parameter into a no-argument callback,
 * supplying the parameter by evaluating the given transformation lambda at invocation time.
 *
 * This is useful for UI event handlers where the callback expects a parameter,
 * but the event source provides no arguments. The transformation lambda allows you
 * to compute or capture the parameter value dynamically when the callback is triggered.
 *
 * Example usage:
 * ```
 * val onClickCallback = onClick.invokeWith { !props.expanded }
 * ```
 * - If `onClick` is not null, `onClickCallback` will invoke it with the transformed value.
 * - If `onClick` is null, `onClickCallback` will also be null.
 *
 * @param transform Lambda that produces the parameter value to pass to the callback.
 * @return A no-argument lambda that invokes the original callback with the transformed value, or null if the original callback is null.
 */
fun <T> ((T) -> Unit)?.invokeWith(transform: () -> T): (() -> Unit)? =
    this?.let { callback ->
        { callback.invoke(transform()) }
    }

/**
 * Calculates the animated scale value based on component state and dimensions
 */
@Composable
fun animatedScale(
    dimension: Int,
    isHovered: Boolean,
    isPressed: Boolean,
    scaleFactor: Float?,
): Float {
    return animateFloatAsState(
        targetValue = if (isHovered && !isPressed && dimension > 0) {
            (dimension + (scaleFactor ?: 0f)) / dimension
        } else {
            1f
        },
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = "scale"
    ).value
}
