package com.telekom.odsystem.molecules.radiobuttongroup

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

data class ODSRadioButtonGroupTokens(
    var gap: Dp,
    var padding: ODSPadding,
    var borderRadius: ODSCorners,
    var minWidth: Dp,
    var clipContent: Boolean,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var titleGap: Dp,
    var titlePadding: ODSPadding,
    var titleVerticalAlignment: Alignment.Vertical,
    var titleHorizontalAlignment: Alignment.Horizontal,
    var titleVerticalArrangement: Arrangement.Vertical,
    var listContainerGap: Dp,
    var listContainerVerticalAlignment: Alignment.Vertical,
    var listContainerHorizontalAlignment: Alignment.Horizontal,
    var listContainerVerticalArrangement: Arrangement.Vertical,
    var supportMessagePadding: ODSPadding,
    var supportMessageVerticalAlignment: Alignment.Vertical,
    var supportMessageHorizontalAlignment: Alignment.Horizontal,
    var supportMessageVerticalArrangement: Arrangement.Vertical,
    var titleTextStyle: ODSTextStyle,
    var titleTextAlign: TextAlign,
    var titleTextOverflow: TextOverflow
)

var defaultODSRadioButtonGroupTokens = ODSRadioButtonGroupTokens(
    gap = DSVariables.spacingComponent3,
    padding = ODSPadding(
        top = DSVariables.spacingComponent3,
        bottom = DSVariables.spacingComponent3,
        left = DSVariables.spacingComponent0,
        right = DSVariables.spacingComponent0
    ),
    borderRadius = ODSCorners(all = DSVariables.radiusExtraSmall),
    minWidth = 165.dp,
    clipContent = true,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    titleGap = DSVariables.spacingComponent0,
    titlePadding = ODSPadding(
        top = DSVariables.spacingComponent2,
        bottom = DSVariables.spacingComponent2,
        left = DSVariables.spacingComponent1,
        right = DSVariables.spacingComponent1
    ),
    titleVerticalAlignment = Alignment.Top,
    titleHorizontalAlignment = Alignment.Start,
    titleVerticalArrangement = Arrangement.Top,
    listContainerGap = DSVariables.spacingComponent1,
    listContainerVerticalAlignment = Alignment.Top,
    listContainerHorizontalAlignment = Alignment.Start,
    listContainerVerticalArrangement = Arrangement.Top,
    supportMessagePadding = ODSPadding(
        left = DSVariables.spacingComponent1,
        right = DSVariables.spacingComponent1
    ),
    supportMessageVerticalAlignment = Alignment.Top,
    supportMessageHorizontalAlignment = Alignment.Start,
    supportMessageVerticalArrangement = Arrangement.Top,
    titleTextStyle = DSTextStyles.subtitle,
    titleTextAlign = TextAlign.Left,
    titleTextOverflow = TextOverflow.Ellipsis
)

var DSRadioButtonGroupTokens: ODSRadioButtonGroupTokens = defaultODSRadioButtonGroupTokens
