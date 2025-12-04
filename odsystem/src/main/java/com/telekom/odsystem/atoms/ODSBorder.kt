package com.telekom.odsystem.atoms

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel

data class ODSBorder(
    val width: Dp? = null,
    val colorList: List<ODSColorModel>? = null
)

fun Modifier.border(
    width: Dp,
    colorList: List<ODSColorModel>?,
    shape: RoundedCornerShape
) = colorList?.fold(this) { modifier, color ->
    if (color.hexColor != null) {
        modifier.border(
            color = color.hexColor!!.getColor(),
            width = width,
            shape = shape
        )
    } else if (color.brush != null) {
        modifier.border(
            width = width,
            brush = color.brush!!,
            shape = shape
        )
    } else {
        modifier.border(width = width, Color.Transparent, shape = shape)
    }
} ?: this