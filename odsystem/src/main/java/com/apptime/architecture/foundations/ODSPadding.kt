package com.telekom.odsystem.foundations

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class ODSPadding {
    var top: Dp = 0.dp
    var bottom: Dp = 0.dp
    var left: Dp = 0.dp
    var right: Dp = 0.dp

    // MARK: - Initializers
    constructor()

    constructor(top: Dp? = null, bottom: Dp? = null, left: Dp? = null, right: Dp? = null) {
        this.top = top ?: 0.dp
        this.bottom = bottom ?: 0.dp
        this.left = left ?: 0.dp
        this.right = right ?: 0.dp
    }

    constructor(
        horizontal: Dp = 0.dp,
        vertical: Dp = 0.dp,
        top: Dp? = null,
        bottom: Dp? = null,
        left: Dp? = null,
        right: Dp? = null
    ) : this(top = top ?: vertical, bottom = bottom ?: vertical, left = left ?: horizontal, right = right ?: horizontal)

    constructor(all: Dp?) : this(top = all, bottom = all, left = all, right = all)

    fun getPaddingValues(): PaddingValues {
        return PaddingValues(start = left, end = right, top = top, bottom = bottom)
    }
}
