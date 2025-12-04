package com.telekom.odsystem.extensions

import android.content.res.Configuration

/**
 * Created by dmarinopoulos on 18/2/25
 */

fun Configuration.isTablet(): Boolean {
    return this.smallestScreenWidthDp >= TABLET_SMALLEST_WIDTH
}

fun Configuration.isLandscape(): Boolean {
    return this.orientation == Configuration.ORIENTATION_LANDSCAPE
}

private const val TABLET_SMALLEST_WIDTH = 600
