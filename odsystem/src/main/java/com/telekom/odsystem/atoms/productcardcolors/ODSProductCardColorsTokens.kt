package com.telekom.odsystem.atoms.productcardcolors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSProductCardColorsTokens(
    val gap: Dp,
    val padding: ODSPadding,
    val height: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val listContainerGap: Dp,
    val listContainerVerticalAlignment: Alignment.Vertical,
    val listContainerHorizontalAlignment: Alignment.Horizontal,
    val listContainerHorizontalArrangement: Arrangement.Horizontal,
    val overflowCountStyle: ODSTextStyle,
    val overflowCountTextAlign: TextAlign
)

val defaultODSProductCardColorsTokens = ODSProductCardColorsTokens(
    gap = DSVariables.spacingComponent2,
    padding = ODSPadding(
        top = 3.dp,
        bottom = 3.dp,
        left = DSVariables.spacingComponent3,
        right = DSVariables.spacingComponent3
    ),
    height = DSVariables.sizingComponent7,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    listContainerGap = DSVariables.spacingComponent2,
    listContainerVerticalAlignment = Alignment.CenterVertically,
    listContainerHorizontalAlignment = Alignment.Start,
    listContainerHorizontalArrangement = Arrangement.Start,
    overflowCountStyle = DSTextStyles.microcopyRegular,
    overflowCountTextAlign = TextAlign.Left
)

var DSProductCardColorsTokens: ODSProductCardColorsTokens = defaultODSProductCardColorsTokens
