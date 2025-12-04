package com.telekom.odsystem.molecules.inputstepper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSInputStepperTokens(
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val backgroundBorderRadius: ODSCorners,
    val backgroundBorderTypeOutline: Dp,
    val backgroundBorderTypeGhost: Dp,
    val backgroundClipContent: Boolean,
    val contentBorderRadius: ODSCorners,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalArrangement: Arrangement.Horizontal,
    val contentPaddingSizeLarge: ODSPadding,
    val valueTextStyleSizeSmall: ODSTextStyle,
    val valueTextStyleSizeLarge: ODSTextStyle,
    val valueTextAlign: TextAlign,
    val valueMinWidth: Dp,
    val valueTextOverflow: TextOverflow,
    val backgroundPaddingSmall: ODSPadding // Not exported from the plugin
)

val defaultODSInputStepperTokens = ODSInputStepperTokens(
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    contentAlignment = Alignment.TopStart,
    backgroundBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    backgroundBorderTypeOutline = DSVariables.strokes1,
    backgroundBorderTypeGhost = DSVariables.strokes1,
    backgroundClipContent = true,
    contentBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalArrangement = Arrangement.SpaceBetween,
    contentPaddingSizeLarge = ODSPadding(all = DSVariables.spacingComponent3),
    valueTextStyleSizeSmall = DSTextStyles.bodyMBold,
    valueTextStyleSizeLarge = DSTextStyles.bodyL,
    valueTextAlign = TextAlign.Center,
    valueMinWidth = DSVariables.sizingComponent10,
    valueTextOverflow = TextOverflow.Ellipsis,
    backgroundPaddingSmall = ODSPadding(all = 4.dp)
)

var DSInputStepperTokens: ODSInputStepperTokens = defaultODSInputStepperTokens
