package com.telekom.odsystem.molecules.searchresultlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSSearchResultListTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val labelContainerPadding: ODSPadding,
    val labelContainerVerticalAlignment: Alignment.Vertical,
    val labelContainerHorizontalAlignment: Alignment.Horizontal,
    val labelContainerHorizontalArrangement: Arrangement.Horizontal,
    val labelTextStyle: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val resultListContainerGap: Dp,
    val resultListContainerVerticalAlignment: Alignment.Vertical,
    val resultListContainerHorizontalAlignment: Alignment.Horizontal,
    val resultListContainerVerticalArrangement: Arrangement.Vertical,
    val odsResultItemContentAlignment: Alignment
)

val defaultODSSearchResultListTokens = ODSSearchResultListTokens(
    gap = DSVariables.spacingComponent3,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    labelContainerPadding = ODSPadding(
        top = DSVariables.spacingComponent4,
        left = DSVariables.spacingComponent4,
        right = DSVariables.spacingComponent4
    ),
    labelContainerVerticalAlignment = Alignment.Top,
    labelContainerHorizontalAlignment = Alignment.Start,
    labelContainerHorizontalArrangement = Arrangement.Start,
    labelTextStyle = DSTextStyles.bodyL,
    labelTextAlign = TextAlign.Left,
    resultListContainerGap = DSVariables.spacingComponent1,
    resultListContainerVerticalAlignment = Alignment.Top,
    resultListContainerHorizontalAlignment = Alignment.Start,
    resultListContainerVerticalArrangement = Arrangement.Top,
    odsResultItemContentAlignment = Alignment.TopStart
)

var DSSearchResultListTokens: ODSSearchResultListTokens = defaultODSSearchResultListTokens
