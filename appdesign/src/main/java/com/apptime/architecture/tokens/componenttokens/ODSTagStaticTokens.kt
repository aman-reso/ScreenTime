package com.telekom.odsystem.componenttokens

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

data class ODSTagStaticTokens(
    val gap: Dp,
    val padding: ODSPadding,
    val borderRadius: ODSCorners,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val iconWidth: Dp,
    val iconHeight: Dp,
    val labelTextStyle: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val labelTextOverflow: TextOverflow
)

val defaultODSTagStaticTokens = ODSTagStaticTokens(
    gap = DSVariables.spacingComponent3,
    padding = ODSPadding(
        top = DSVariables.spacingComponent2,
        bottom = DSVariables.spacingComponent2,
        left = DSVariables.spacingComponent3,
        right = DSVariables.spacingComponent3
    ),
    borderRadius = ODSCorners(all = DSVariables.radiusExtraSmall),
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    iconWidth = DSVariables.sizingComponent7,
    iconHeight = DSVariables.sizingComponent7,
    labelTextStyle = DSTextStyles.microcopyBold,
    labelTextAlign = TextAlign.Left,
    labelTextOverflow = TextOverflow.Ellipsis
)

var DSTagStaticTokens: ODSTagStaticTokens = defaultODSTagStaticTokens
