package com.telekom.odsystem.foundations

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class ODSCorners {
    var topLeft: Dp = 0.dp
    var topRight: Dp = 0.dp
    var bottomLeft: Dp = 0.dp
    var bottomRight: Dp = 0.dp

    // MARK: - Initializers
    constructor()

    constructor(all: Dp?) {
        this.topLeft = all ?: 0.dp
        this.topRight = all ?: 0.dp
        this.bottomLeft = all ?: 0.dp
        this.bottomRight = all ?: 0.dp
    }

    constructor(
        topLeft: Dp? = null,
        topRight: Dp? = null,
        bottomLeft: Dp? = null,
        bottomRight: Dp? = null
    ) {
        this.topLeft = topLeft ?: 0.dp
        this.topRight = topRight ?: 0.dp
        this.bottomLeft = bottomLeft ?: 0.dp
        this.bottomRight = bottomRight ?: 0.dp
    }

    @Composable
    fun getRoundedCornerShape(): RoundedCornerShape {
        return if (topLeft == 0.dp && topRight == 0.dp && bottomLeft == 0.dp && bottomRight == 0.dp) {
            RoundedCornerShape(0.dp)
        } else {
            RoundedCornerShape(
                topStart = topLeft,
                topEnd = topRight,
                bottomStart = bottomLeft,
                bottomEnd = bottomRight
            )
        }
    }

    fun getListOfRawValues(): ArrayList<Int?> {
        return arrayListOf(
            topLeft.value.toInt(),
            topRight.value.toInt(),
            bottomLeft.value.toInt(),
            bottomRight.value.toInt()
        )
    }

    fun copy(
        topLeft: Dp? = this.topLeft,
        topRight: Dp? = this.topRight,
        bottomLeft: Dp? = this.bottomLeft,
        bottomRight: Dp? = this.bottomRight
    ): ODSCorners {
        return ODSCorners(topLeft, topRight, bottomLeft, bottomRight)
    }
}
