package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: a70490f
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-18024
 */

data class ODSListTokens(
    val gap: Dp,
    val padding: ODSPadding,
    val cornerRadius: ODSCorners,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val listContainerGap: Dp,
    val listContainerVerticalAlignment: Alignment.Vertical,
    val listContainerHorizontalAlignment: Alignment.Horizontal,
    val listContainerVerticalArrangement: Arrangement.Vertical,
    val secondLevelPadding: ODSPadding,
    val secondLevelVerticalAlignment: Alignment.Vertical,
    val secondLevelHorizontalAlignment: Alignment.Horizontal,
    val secondLevelVerticalArrangement: Arrangement.Vertical,
    val listContainer2Gap: Dp,
    val listContainer2VerticalAlignment: Alignment.Vertical,
    val listContainer2HorizontalAlignment: Alignment.Horizontal,
    val listContainer2VerticalArrangement: Arrangement.Vertical,
    val thirdLevelPadding: ODSPadding,
    val thirdLevelVerticalAlignment: Alignment.Vertical,
    val thirdLevelHorizontalAlignment: Alignment.Horizontal,
    val thirdLevelVerticalArrangement: Arrangement.Vertical
)

val defaultODSListTokens = ODSListTokens(
    gap = DSVariables.spacingComponent1,
    padding = ODSPadding(
        top = DSVariables.spacingComponent3,
        bottom = DSVariables.spacingComponent3
    ),
    cornerRadius = ODSCorners(all = 4.dp),
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    listContainerGap = DSVariables.spacingComponent1,
    listContainerVerticalAlignment = Alignment.Top,
    listContainerHorizontalAlignment = Alignment.Start,
    listContainerVerticalArrangement = Arrangement.Top,
    secondLevelPadding = ODSPadding(left = DSVariables.spacingLayout1),
    secondLevelVerticalAlignment = Alignment.Top,
    secondLevelHorizontalAlignment = Alignment.Start,
    secondLevelVerticalArrangement = Arrangement.Top,
    listContainer2Gap = DSVariables.spacingComponent1,
    listContainer2VerticalAlignment = Alignment.Top,
    listContainer2HorizontalAlignment = Alignment.Start,
    listContainer2VerticalArrangement = Arrangement.Top,
    thirdLevelPadding = ODSPadding(left = DSVariables.spacingLayout3),
    thirdLevelVerticalAlignment = Alignment.Top,
    thirdLevelHorizontalAlignment = Alignment.Start,
    thirdLevelVerticalArrangement = Arrangement.Top
)

var DSListTokens: ODSListTokens = defaultODSListTokens
