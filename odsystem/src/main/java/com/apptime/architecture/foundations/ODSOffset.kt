package com.telekom.odsystem.foundations

import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class ODSOffset(x: Dp? = null, y: Dp? = null) {
    private val offsetX: Dp? = x
    private val offsetY: Dp? = y

    val x: Dp
        get() = offsetX ?: 0.dp

    val y: Dp
        get() = offsetY ?: 0.dp
}

fun Modifier.offset(offset: ODSOffset?): Modifier {
    return offset?.let { this.offset(x = it.x, y = it.y) } ?: this
}
