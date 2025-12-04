package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.SCALE_FACTOR

data class ODSCardBasicTokens(
    val width: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentGap: Dp,
    val contentPadding: ODSPadding,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentVerticalArrangement: Arrangement.Vertical,
    val contentVerticalArrangementCustomHeight: Arrangement.Vertical,
    val contentContentAlignment: Alignment,
    val cardBgBorderRadius: ODSCorners,
    val cardBgClipContent: Boolean,
    val cardBgVerticalAlignment: Alignment.Vertical,
    val cardBgHorizontalAlignment: Alignment.Horizontal,
    val cardBgVerticalArrangement: Arrangement.Vertical,
    val copyGap: Dp,
    val copyVerticalAlignment: Alignment.Vertical,
    val copyHorizontalAlignment: Alignment.Horizontal,
    val copyVerticalArrangement: Arrangement.Vertical,
    val actionContainerVerticalAlignment: Alignment.Vertical,
    val actionContainerVerticalAlignmentCustomHeight: Alignment.Vertical,
    val actionContainerHorizontalAlignment: Alignment.Horizontal,
    val actionContainerHorizontalAlignmentCustomHeight: Alignment.Horizontal,
    val actionContainerVerticalArrangement: Arrangement.Vertical,
    val actionContainerVerticalArrangementCustomHeight: Arrangement.Vertical,
    var scaleFactor: Float // Custom addition needs to be documented
)

val defaultODSCardBasicTokens = ODSCardBasicTokens(
    width = DSVariables.columns6Columns,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    contentGap = DSVariables.spacingLayout5,
    contentPadding = ODSPadding(all = DSVariables.spacingLayout2),
    contentVerticalAlignment = Alignment.Top,
    contentHorizontalAlignment = Alignment.CenterHorizontally,
    contentVerticalArrangement = Arrangement.Top,
    contentVerticalArrangementCustomHeight = Arrangement.SpaceBetween,
    contentContentAlignment = Alignment.TopCenter,
    cardBgBorderRadius = ODSCorners(all = DSVariables.radiusMedium),
    cardBgClipContent = true,
    cardBgVerticalAlignment = Alignment.Top,
    cardBgHorizontalAlignment = Alignment.CenterHorizontally,
    cardBgVerticalArrangement = Arrangement.Top,
    copyGap = DSVariables.spacingComponent5,
    copyVerticalAlignment = Alignment.Top,
    copyHorizontalAlignment = Alignment.Start,
    copyVerticalArrangement = Arrangement.Top,
    actionContainerVerticalAlignment = Alignment.Top,
    actionContainerVerticalAlignmentCustomHeight = Alignment.Bottom,
    actionContainerHorizontalAlignment = Alignment.Start,
    actionContainerHorizontalAlignmentCustomHeight = Alignment.CenterHorizontally,
    actionContainerVerticalArrangement = Arrangement.Bottom, // Custom value is being used
    actionContainerVerticalArrangementCustomHeight = Arrangement.Bottom,
    scaleFactor = SCALE_FACTOR,
)

var DSCardBasicTokens: ODSCardBasicTokens = defaultODSCardBasicTokens
