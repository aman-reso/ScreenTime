package com.app.screentime.permission.component.infocard

import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

/**
 * Design tokens for InfoCard component.
 */
data class InfoCardTokens(
    val cornerRadius: Dp,
    val padding: Dp,
    val titleSpacing: Dp,
    val borderWidth: Dp
)

/**
 * Default tokens for InfoCard.
 */
val defaultInfoCardTokens = InfoCardTokens(
    cornerRadius = DSVariables.radiusLarge, // 24.dp
    padding = DSVariables.spacingComponent7, // 24.dp
    titleSpacing = DSVariables.spacingComponent3, // 8.dp
    borderWidth = DSVariables.strokes1 // 1.dp
)

