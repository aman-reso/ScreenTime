package com.telekom.odsystem.atoms.productcardtag

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

data class ODSProductCardTagTokens(
    val padding: ODSPadding,
    val cornerRadius: ODSCorners,
    val height: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val labelStyle: ODSTextStyle,
    val labelTextAlign: TextAlign,
    var labelTextOverflow: TextOverflow? // Not exported from the plugin
)

val defaultODSProductCardTagTokens = ODSProductCardTagTokens(
    padding = ODSPadding(
        top = DSVariables.spacingComponent1,
        bottom = DSVariables.spacingComponent1,
        left = DSVariables.spacingComponent3,
        right = DSVariables.spacingComponent3
    ),
    cornerRadius = ODSCorners(all = DSVariables.radiusExtraSmall),
    height = DSVariables.sizingComponent11,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    labelStyle = DSTextStyles.bodyMBold,
    labelTextAlign = TextAlign.Left,
    labelTextOverflow = TextOverflow.Ellipsis
)

var DSProductCardTagTokens: ODSProductCardTagTokens = defaultODSProductCardTagTokens
