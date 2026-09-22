@file:Suppress("MagicNumber")

package com.telekom.odsystem.foundations

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class HexColor(private val hexColor: String, val alpha: Float = 1f) {

    companion object {
        /**
         * Represents a fully transparent color.
         */
        val transparent = HexColor("#00000000")

        /**
         * Represents a state where no color or tint should be applied.
         * This is useful for cases like an icon's tint, where setting it to `None`
         * will ensure the icon preserves its original colors.
         */
        val None = HexColor("#", -1f)
    }

    constructor(color: Long) : this(
        hexColor = color.getHexStringFromLong(),
        alpha = color.getFloatAlphaFromLong()
    )

    fun getColor(): Color {
        return Color(getIntColor())
    }

    fun getIntColor(): Int {
        if (alpha == 0f) {
            return 0
        }
        val stringValue = if (hexColor.startsWith("#")) {
            hexColor
        } else {
            "#$hexColor"
        }
        return try {
            val color = stringValue.toColorInt()
            android.graphics.Color.argb(
                (alpha * 255.0).toInt(),
                android.graphics.Color.red(color),
                android.graphics.Color.green(color),
                android.graphics.Color.blue(color)
            )
        } catch (e: Exception) {
            if (hexColor != "#") {
                Log.e("DSQColors", e.localizedMessage ?: "Error parsing color")
            }
            return Color.Unspecified.toArgb()
        }
    }

    fun getHexColor(): String {
        return hexColor
    }
}

@Suppress("ImplicitDefaultLocale")
internal fun Long.getHexStringFromLong(): String {
    return String.format("#%06X", 0xFFFFFF and this.toInt())
}

internal fun Long.getFloatAlphaFromLong(): Float {
    return ((this shr 24) and 0xFF).toFloat() / 255f
}

internal fun Color.toHexColor(): HexColor {
    val alpha = (this.alpha * 255).toInt()
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()
    val hexString = String.format(Locale.ROOT, "#%02X%02X%02X%02X", alpha, red, green, blue)
    return HexColor(hexString, this.alpha)
}
