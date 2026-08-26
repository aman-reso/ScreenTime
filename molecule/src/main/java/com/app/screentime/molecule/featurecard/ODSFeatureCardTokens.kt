package com.app.screentime.molecule.featurecard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Tokens for ODSFeatureCard component.
 * All values use ODS design system variables.
 */
data class ODSFeatureCardTokens(
    val padding: ODSPadding,
    val cornerRadius: ODSCorners,
    val minHeight: Dp,
    val contentGap: Dp,
    val contentHorizontalArrangement: Arrangement.Horizontal,
    val contentVerticalAlignment: Alignment.Vertical,
    val iconContainerSize: Dp,
    val iconCornerRadius: ODSCorners,
    val textContentGap: Dp,
    val textContentVerticalArrangement: Arrangement.Vertical,
    val subtitleStyle: ODSTextStyle,
    val titleStyle: ODSTextStyle,
    val arrowIconSize: Dp,
    val wavyBackgroundOpacity: Float
)

val defaultODSFeatureCardTokens = ODSFeatureCardTokens(
    padding = ODSPadding(all = DSVariables.spacingComponent5),
    cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
    minHeight = DSVariables.sizingComponent16,
    contentGap = DSVariables.spacingComponent4,
    contentHorizontalArrangement = Arrangement.Start,
    contentVerticalAlignment = Alignment.CenterVertically,
    iconContainerSize = DSVariables.sizingComponent10,
    iconCornerRadius = ODSCorners(all = DSVariables.radiusLarge),
    textContentGap = DSVariables.spacingComponent2,
    textContentVerticalArrangement = Arrangement.Top,
    subtitleStyle = DSTextStyles.bodySRegular,
    titleStyle = DSTextStyles.bodyMBold,
    arrowIconSize = DSVariables.sizingComponent10,
    wavyBackgroundOpacity = 0.05f
)

var DSFeatureCardTokens: ODSFeatureCardTokens = defaultODSFeatureCardTokens
