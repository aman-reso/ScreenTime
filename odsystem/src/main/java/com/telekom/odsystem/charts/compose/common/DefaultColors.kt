package com.telekom.odsystem.charts.compose.common

import androidx.annotation.RestrictTo
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.telekom.odsystem.charts.core.common.DefaultColors

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@Composable
fun getDefaultColors(): DefaultColors =
    if (isSystemInDarkTheme()) DefaultColors.Dark else DefaultColors.Light
