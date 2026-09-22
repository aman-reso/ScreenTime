package com.telekom.odsystem.atoms.tagdismissible

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSTagDismissibleTokens(
    val gap: Dp,
    val padding: ODSPadding,
    val borderRadius: ODSCorners,
    val minHeight: Dp,
    val minWidth: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val dismissibleTagGap: Dp,
    val dismissibleTagPadding: ODSPadding,
    val dismissibleTagBorderRadius: ODSCorners,
    val dismissibleTagVerticalAlignment: Alignment.Vertical,
    val dismissibleTagHorizontalAlignment: Alignment.Horizontal,
    val dismissibleTagHorizontalArrangement: Arrangement.Horizontal,
    val iconWidth: Dp,
    val iconHeight: Dp,
    val labelTextStyle: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val labelTextOverflow: TextOverflow,
    val closeButtonBorderRadius: ODSCorners,
    val closeButtonVerticalAlignment: Alignment.Vertical,
    val closeButtonHorizontalAlignment: Alignment.Horizontal,
    val closeButtonVerticalArrangement: Arrangement.Vertical,
    val icon2Width: Dp,
    val icon2Height: Dp
)

val defaultODSTagDismissibleTokens = ODSTagDismissibleTokens(
    gap = DSVariables.spacingComponent0,
    padding = ODSPadding(all = DSVariables.spacingComponent3),
    borderRadius = ODSCorners(all = DSVariables.radiusExtraSmall),
    minHeight = DSVariables.sizingComponent14,
    minWidth = DSVariables.sizingComponent14,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    dismissibleTagGap = DSVariables.spacingComponent3,
    dismissibleTagPadding = ODSPadding(
        top = DSVariables.spacingComponent2,
        bottom = DSVariables.spacingComponent2,
        left = DSVariables.spacingComponent3,
        right = DSVariables.spacingComponent3
    ),
    dismissibleTagBorderRadius = ODSCorners(all = DSVariables.radiusExtraSmall),
    dismissibleTagVerticalAlignment = Alignment.CenterVertically,
    dismissibleTagHorizontalAlignment = Alignment.CenterHorizontally,
    dismissibleTagHorizontalArrangement = Arrangement.Center,
    iconWidth = DSVariables.sizingComponent7,
    iconHeight = DSVariables.sizingComponent7,
    labelTextStyle = DSTextStyles.bodyMBold,
    labelTextAlign = TextAlign.Left,
    labelTextOverflow = TextOverflow.Ellipsis,
    closeButtonBorderRadius = ODSCorners(all = DSVariables.radiusExtraSmall),
    closeButtonVerticalAlignment = Alignment.CenterVertically,
    closeButtonHorizontalAlignment = Alignment.CenterHorizontally,
    closeButtonVerticalArrangement = Arrangement.Center,
    icon2Width = DSVariables.sizingComponent7,
    icon2Height = DSVariables.sizingComponent7
)

var DSTagDismissibleTokens: ODSTagDismissibleTokens = defaultODSTagDismissibleTokens
