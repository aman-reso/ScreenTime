package com.telekom.odsystem.slots.featurelistitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSFeatureListItemTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val iconWidth: Dp,
    val iconHeight: Dp,
    val labelStyle: ODSTextStyle,
    val labelTextAlign: TextAlign
)

val defaultODSFeatureListItemTokens = ODSFeatureListItemTokens(
    gap = DSVariables.spacingComponent2,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    iconWidth = DSVariables.sizingComponent7,
    iconHeight = DSVariables.sizingComponent7,
    labelStyle = DSTextStyles.bodyMRegular,
    labelTextAlign = TextAlign.Left
)

var DSFeatureListItemTokens: ODSFeatureListItemTokens = defaultODSFeatureListItemTokens
