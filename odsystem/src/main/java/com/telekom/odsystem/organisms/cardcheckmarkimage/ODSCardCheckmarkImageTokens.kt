package com.telekom.odsystem.organisms.cardcheckmarkimage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-11 (v1.33.1) - uid: 5ac57ab5
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=8756-24496
 */

data class ODSCardCheckmarkImageTokens(
    val zStackWidth: Dp,
    val zStackContentAlignment: Alignment,
    val width: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val imageAspectRatioZStackContentAlignment: Alignment,
    val imageAspectRatioVerticalAlignment: Alignment.Vertical,
    val imageAspectRatioHorizontalAlignment: Alignment.Horizontal,
    val imageAspectRatioVerticalArrangement: Arrangement.Vertical,
    val imageAspectRatioContentAlignment: Alignment,
    val imageContainerZStackClipContent: Boolean,
    val imageContainerCornerRadius: ODSCorners,
    val imageContainerVerticalAlignment: Alignment.Vertical,
    val imageContainerHorizontalAlignment: Alignment.Horizontal,
    val imageContainerVerticalArrangement: Arrangement.Vertical,
    val imageContentScale: ContentScale,
    val containerZStackContentAlignment: Alignment,
    val containerGap: Dp,
    val containerPadding: ODSPadding,
    val containerVerticalAlignment: Alignment.Vertical,
    val containerHorizontalAlignment: Alignment.Horizontal,
    val containerVerticalArrangement: Arrangement.Vertical,
    val containerContentAlignment: Alignment,
    val cardBgCornerRadius: ODSCorners,
    val cardBgVerticalAlignment: Alignment.Vertical,
    val cardBgHorizontalAlignment: Alignment.Horizontal,
    val cardBgVerticalArrangement: Arrangement.Vertical,
    val contentGap: Dp,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentHorizontalArrangement: Arrangement.Horizontal,
    val contentContainerVerticalAlignment: Alignment.Vertical,
    val contentContainerHorizontalAlignment: Alignment.Horizontal,
    val contentContainerVerticalArrangement: Arrangement.Vertical,
    val selectorContainerRightWidth: Dp,
    val selectorContainerRightVerticalAlignment: Alignment.Vertical,
    val selectorContainerRightHorizontalAlignment: Alignment.Horizontal,
    val selectorContainerRightHorizontalArrangement: Arrangement.Horizontal,
    val checkmarkRightWidth: Dp,
    val checkmarkRightHeight: Dp,
    val cardStrokeCornerRadius: ODSCorners,
    val cardStrokeVerticalAlignment: Alignment.Vertical,
    val cardStrokeHorizontalAlignment: Alignment.Horizontal,
    val cardStrokeVerticalArrangement: Arrangement.Vertical,
    val cardStrokeBorder: Dp,
    val cardStrokeBorderSelected: Dp,
    var scaleFactor: Float // Not exported from plugin
)

val defaultODSCardCheckmarkImageTokens = ODSCardCheckmarkImageTokens(
    zStackWidth = DSVariables.columns4Columns,
    zStackContentAlignment = Alignment.TopStart,
    width = DSVariables.columns4Columns,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    contentAlignment = Alignment.TopStart,
    imageAspectRatioZStackContentAlignment = Alignment.BottomCenter,
    imageAspectRatioVerticalAlignment = Alignment.Bottom,
    imageAspectRatioHorizontalAlignment = Alignment.CenterHorizontally,
    imageAspectRatioVerticalArrangement = Arrangement.Bottom,
    imageAspectRatioContentAlignment = Alignment.BottomCenter,
    imageContainerZStackClipContent = true,
    imageContainerCornerRadius = ODSCorners(
        topLeft = DSVariables.radiusMedium,
        topRight = DSVariables.radiusMedium,
        bottomLeft = 0.dp,
        bottomRight = 0.dp
    ),
    imageContainerVerticalAlignment = Alignment.Top,
    imageContainerHorizontalAlignment = Alignment.Start,
    imageContainerVerticalArrangement = Arrangement.Top,
    imageContentScale = ContentScale.Crop,
    containerZStackContentAlignment = Alignment.TopCenter,
    containerGap = DSVariables.spacingLayout5,
    containerPadding = ODSPadding(all = DSVariables.spacingLayout2),
    containerVerticalAlignment = Alignment.Top,
    containerHorizontalAlignment = Alignment.CenterHorizontally,
    containerVerticalArrangement = Arrangement.Top,
    containerContentAlignment = Alignment.TopCenter,
    cardBgCornerRadius = ODSCorners(
        topLeft = 0.dp,
        topRight = 0.dp,
        bottomLeft = DSVariables.radiusMedium,
        bottomRight = DSVariables.radiusMedium
    ),
    cardBgVerticalAlignment = Alignment.Top,
    cardBgHorizontalAlignment = Alignment.CenterHorizontally,
    cardBgVerticalArrangement = Arrangement.Top,
    contentGap = DSVariables.spacingComponent5,
    contentVerticalAlignment = Alignment.Top,
    contentHorizontalAlignment = Alignment.Start,
    contentHorizontalArrangement = Arrangement.Start,
    contentContainerVerticalAlignment = Alignment.Top,
    contentContainerHorizontalAlignment = Alignment.Start,
    contentContainerVerticalArrangement = Arrangement.Top,
    selectorContainerRightWidth = DSVariables.sizingComponent10,
    selectorContainerRightVerticalAlignment = Alignment.Top,
    selectorContainerRightHorizontalAlignment = Alignment.CenterHorizontally,
    selectorContainerRightHorizontalArrangement = Arrangement.Center,
    checkmarkRightWidth = 24.dp,
    checkmarkRightHeight = 24.dp,
    cardStrokeCornerRadius = ODSCorners(all = DSVariables.radiusMedium),
    cardStrokeVerticalAlignment = Alignment.Top,
    cardStrokeHorizontalAlignment = Alignment.CenterHorizontally,
    cardStrokeVerticalArrangement = Arrangement.Top,
    cardStrokeBorder = DSVariables.strokes1,
    cardStrokeBorderSelected = DSVariables.strokes2,
    scaleFactor = 8f // Not exported by the plugin
)

var DSCardCheckmarkImageTokens: ODSCardCheckmarkImageTokens = defaultODSCardCheckmarkImageTokens
