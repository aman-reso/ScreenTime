package com.telekom.odsystem.atoms.navigationitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-26 (v1.33.1) - uid: 218b2f9e
 * Figma link: https://figma.com/design/cpaNsDgmzEjyHilbYDSKS9/Exploration File_Part 2?node-id=5716-39429
 */

data class ODSNavigationItemTokens(
    val gap: Dp,
    val padding: ODSPadding,
    val cornerRadius: ODSCorners,
    val minHeight: Dp,
    val minWidth: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val iconContainerZStackMinWidth: Dp,
    val iconContainerZStackContentAlignment: Alignment,
    val iconContainerPadding: ODSPadding,
    val iconContainerCornerRadius: ODSCorners,
    val iconContainerMinWidth: Dp,
    val iconContainerVerticalAlignment: Alignment.Vertical,
    val iconContainerHorizontalAlignment: Alignment.Horizontal,
    val iconContainerHorizontalArrangement: Arrangement.Horizontal,
    val iconContainerContentAlignment: Alignment,
    val iconActiveWidth: Dp,
    val iconActiveHeight: Dp,
    val iconWidth: Dp,
    val iconHeight: Dp,
    val odsBadgeNumberAbsoluteContentAlignment: Alignment,
    val odsBadgeNumberAbsoluteOffset: ODSOffset,
    val labelStyle: ODSTextStyle,
    val labelTextAlign: TextAlign,
)

val defaultODSNavigationItemTokens = ODSNavigationItemTokens(
    gap = DSVariables.spacingComponent2,
    padding = ODSPadding(
        top = DSVariables.spacingComponent4,
        left = DSVariables.spacingComponent2,
        right = DSVariables.spacingComponent2
    ),
    cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
    minHeight = DSVariables.sizingMinimumTappableArea,
    minWidth = DSVariables.sizingMinimumTappableArea,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
    iconContainerZStackMinWidth = DSVariables.sizingMinimumTappableArea,
    iconContainerZStackContentAlignment = Alignment.Center,
    iconContainerPadding = ODSPadding(
        top = DSVariables.spacingComponent1,
        bottom = DSVariables.spacingComponent1
    ),
    iconContainerCornerRadius = ODSCorners(all = DSVariables.radiusFull),
    iconContainerMinWidth = DSVariables.sizingMinimumTappableArea,
    iconContainerVerticalAlignment = Alignment.CenterVertically,
    iconContainerHorizontalAlignment = Alignment.CenterHorizontally,
    iconContainerHorizontalArrangement = Arrangement.Center,
    iconContainerContentAlignment = Alignment.Center,
    iconActiveWidth = DSVariables.sizingComponent8,
    iconActiveHeight = DSVariables.sizingComponent8,
    iconWidth = DSVariables.sizingComponent8,
    iconHeight = DSVariables.sizingComponent8,
    odsBadgeNumberAbsoluteContentAlignment = Alignment.TopEnd,
    odsBadgeNumberAbsoluteOffset = ODSOffset(x = -7.dp, y = -3.dp),
    labelStyle = DSTextStyles.microcopyRegular,
    labelTextAlign = TextAlign.Center
)

var DSNavigationItemTokens: ODSNavigationItemTokens = defaultODSNavigationItemTokens
