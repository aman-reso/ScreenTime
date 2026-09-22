package com.telekom.odsystem.atoms.resultitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSResultItemTokens(
    val gap: Dp,
    val borderRadius: ODSCorners,
    val minHeight: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val contentAlignment: Alignment,
    val backgroundBorderRadius: ODSCorners,
    val backgroundClipContent: Boolean,
    val backgroundVerticalAlignment: Alignment.Vertical,
    val backgroundHorizontalAlignment: Alignment.Horizontal,
    val backgroundVerticalArrangement: Arrangement.Vertical,
    val backgroundContentAlignment: Alignment,
    val iconContainerBorderRadius: ODSCorners,
    val iconContainerWidth: Dp,
    val iconContainerHeight: Dp,
    val iconContainerVerticalAlignment: Alignment.Vertical,
    val iconContainerHorizontalAlignment: Alignment.Horizontal,
    val iconContainerHorizontalArrangement: Arrangement.Horizontal,
    val iconWidth: Dp,
    val iconHeight: Dp,
    val odsAiIconContentAlignmentFragMagenta: Alignment,
    val odsAiIconWidthFragMagenta: Dp,
    val odsAiIconHeightFragMagenta: Dp,
    val labelContainerGap: Dp,
    val labelContainerVerticalAlignment: Alignment.Vertical,
    val labelContainerHorizontalAlignment: Alignment.Horizontal,
    val labelContainerHorizontalArrangement: Arrangement.Horizontal,
    val textRecessiveTextStyle: ODSTextStyle,
    val textRecessiveTextAlign: TextAlign,
    val textPrimaryTextStyle: ODSTextStyle,
    val textPrimaryTextAlign: TextAlign,
    val promptTextStyleFragMagenta: ODSTextStyle,
    val promptTextAlignFragMagenta: TextAlign,
)

val defaultODSResultItemTokens = ODSResultItemTokens(
    gap = DSVariables.spacingComponent3,
    borderRadius = ODSCorners(all = DSVariables.radiusSmall),
    minHeight = DSVariables.sizingMinimumTappableArea,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    contentAlignment = Alignment.TopStart,
    backgroundBorderRadius = ODSCorners(all = DSVariables.radiusSmall),
    backgroundClipContent = true,
    backgroundVerticalAlignment = Alignment.Top,
    backgroundHorizontalAlignment = Alignment.CenterHorizontally,
    backgroundVerticalArrangement = Arrangement.Top,
    backgroundContentAlignment = Alignment.TopStart,
    iconContainerBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    iconContainerWidth = DSVariables.sizingComponent14,
    iconContainerHeight = DSVariables.sizingComponent14,
    iconContainerVerticalAlignment = Alignment.CenterVertically,
    iconContainerHorizontalAlignment = Alignment.CenterHorizontally,
    iconContainerHorizontalArrangement = Arrangement.Center,
    iconWidth = DSVariables.sizingComponent8,
    iconHeight = DSVariables.sizingComponent8,
    odsAiIconContentAlignmentFragMagenta = Alignment.Center,
    odsAiIconWidthFragMagenta = DSVariables.sizingComponent10,
    odsAiIconHeightFragMagenta = DSVariables.sizingComponent10,
    labelContainerGap = DSVariables.spacingComponent2,
    labelContainerVerticalAlignment = Alignment.CenterVertically,
    labelContainerHorizontalAlignment = Alignment.Start,
    labelContainerHorizontalArrangement = Arrangement.Start,
    textRecessiveTextStyle = DSTextStyles.bodyMBold,
    textRecessiveTextAlign = TextAlign.Left,
    textPrimaryTextStyle = DSTextStyles.bodyMBold,
    textPrimaryTextAlign = TextAlign.Left,
    promptTextStyleFragMagenta = DSTextStyles.bodyMBold,
    promptTextAlignFragMagenta = TextAlign.Left,
)
var DSResultItemTokens: ODSResultItemTokens = defaultODSResultItemTokens
