package com.app.screentime.permission.component.bottombar

import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

/**
 * Design tokens for BottomBar component.
 */
data class BottomBarTokens(
    val padding: Dp,
    val verticalSpacing: Dp,
    val linkSpacing: Dp,
    val separatorText: String
)

/**
 * Default tokens for BottomBar.
 */
val defaultBottomBarTokens = BottomBarTokens(
    padding = DSVariables.spacingComponent3, // 8.dp
    verticalSpacing = DSVariables.spacingComponent5, // 16.dp
    linkSpacing = DSVariables.spacingComponent5, // 16.dp
    separatorText = "•"
)


