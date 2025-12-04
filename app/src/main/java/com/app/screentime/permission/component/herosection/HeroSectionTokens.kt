package com.app.screentime.permission.component.herosection

import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables

/**
 * Design tokens for HeroSection component.
 */
data class HeroSectionTokens(
    val iconSize: Dp,
    val iconContainerSize: Dp,
    val diamondSize: Dp,
    val titleSpacing: Dp,
    val animationDuration: Int,
    val scaleMin: Float,
    val scaleMax: Float,
    val floatMin: Float,
    val floatMax: Float
)

/**
 * Default tokens for HeroSection.
 */
val defaultHeroSectionTokens = HeroSectionTokens(
    iconSize = DSVariables.sizingComponent15, // 64.dp (closest to 60.dp)
    iconContainerSize = DSVariables.sizingComponent17, // 80.dp
    diamondSize = DSVariables.sizingComponent19, // 144.dp (closest to 120.dp)
    titleSpacing = DSVariables.spacingComponent7, // 24.dp
    animationDuration = 4000,
    scaleMin = 1f,
    scaleMax = 1.1f,
    floatMin = 0f,
    floatMax = -10f
)


