package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSSwitchTokens(
    val gapSizeLarge: Dp,
    val gapSizeSmall: Dp,
    val minHeight: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val paddingSizeSmall: ODSPadding,
    val switchIconContainerPaddingSizeLarge: ODSPadding,
    val switchIconContainerVerticalAlignment: Alignment.Vertical,
    val switchIconContainerHorizontalAlignment: Alignment.Horizontal,
    val switchIconContainerVerticalArrangement: Arrangement.Vertical,
    val labelContainerPaddingSizeLarge: ODSPadding,
    val labelContainerPaddingSizeSmall: ODSPadding,
    val labelContainerVerticalAlignment: Alignment.Vertical,
    val labelContainerHorizontalAlignment: Alignment.Horizontal,
    val labelContainerHorizontalArrangement: Arrangement.Horizontal,
    val switchLabelTextStyleSizeLarge: ODSTextStyle,
    val switchLabelTextStyleSizeSmall: ODSTextStyle,
    val switchLabelTextAlign: TextAlign
)

val defaultODSSwitchTokens = ODSSwitchTokens(
    gapSizeLarge = DSVariables.spacingComponent5,
    gapSizeSmall = DSVariables.spacingComponent3,
    minHeight = DSVariables.sizingMinimumTappableArea,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    paddingSizeSmall = ODSPadding(
        top = DSVariables.spacingComponent4,
        bottom = DSVariables.spacingComponent4
    ),
    switchIconContainerPaddingSizeLarge = ODSPadding(
        top = DSVariables.spacingComponent2,
        bottom = DSVariables.spacingComponent2
    ),
    switchIconContainerVerticalAlignment = Alignment.Top,
    switchIconContainerHorizontalAlignment = Alignment.Start,
    switchIconContainerVerticalArrangement = Arrangement.Top,
    labelContainerPaddingSizeLarge = ODSPadding(
        top = DSVariables.spacingComponent4,
        bottom = DSVariables.spacingComponent4
    ),
    labelContainerPaddingSizeSmall = ODSPadding(
        top = DSVariables.spacingComponent1,
        bottom = DSVariables.spacingComponent1
    ),
    labelContainerVerticalAlignment = Alignment.CenterVertically,
    labelContainerHorizontalAlignment = Alignment.Start,
    labelContainerHorizontalArrangement = Arrangement.Start,
    switchLabelTextStyleSizeLarge = DSTextStyles.subtitle,
    switchLabelTextStyleSizeSmall = DSTextStyles.bodyMBold,
    switchLabelTextAlign = TextAlign.Left
)

var DSSwitchTokens: ODSSwitchTokens = defaultODSSwitchTokens
