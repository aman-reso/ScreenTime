package com.telekom.odsystem.organisms.cardquickaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.SCALE_FACTOR

data class ODSCardQuickActionTokens(
    val width: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val heightSizeMediumCustomHeight: Dp,
    val heightSizeSmallCustomHeight: Dp,
    val containerZStackContentAlignment: Alignment,
    val containerGap: Dp,
    val containerPaddingSizeMedium: ODSPadding,
    val containerPaddingSizeSmall: ODSPadding,
    val containerVerticalAlignment: Alignment.Vertical,
    val containerHorizontalAlignment: Alignment.Horizontal,
    val containerVerticalArrangement: Arrangement.Vertical,
    val containerContentAlignment: Alignment,
    val cardBgCornerRadiusSizeMedium: ODSCorners,
    val cardBgCornerRadiusSizeSmall: ODSCorners,
    val cardBgVerticalAlignment: Alignment.Vertical,
    val cardBgHorizontalAlignment: Alignment.Horizontal,
    val cardBgVerticalArrangement: Arrangement.Vertical,
    val cardBgBorder: Dp,
    val contentGap: Dp,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentHorizontalArrangement: Arrangement.Horizontal,
    val contentContainerVerticalAlignment: Alignment.Vertical,
    val contentContainerHorizontalAlignment: Alignment.Horizontal,
    val contentContainerVerticalArrangement: Arrangement.Vertical,
    val selectorContainerRightVerticalAlignment: Alignment.Vertical,
    val selectorContainerRightHorizontalAlignment: Alignment.Horizontal,
    val selectorContainerRightHorizontalArrangement: Arrangement.Horizontal,
    val selectorContainerRightPaddingSizeSmall: ODSPadding,
    val arrowRightWidth: Dp,
    val arrowRightHeight: Dp,
    var scaleFactor: Float, // Not exported by plugin
)

val defaultODSCardQuickActionTokens = ODSCardQuickActionTokens(
    width = 400.dp,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    heightSizeMediumCustomHeight = 184.dp,
    heightSizeSmallCustomHeight = 168.dp,
    containerZStackContentAlignment = Alignment.TopCenter,
    containerGap = DSVariables.spacingLayout5,
    containerPaddingSizeMedium = ODSPadding(all = DSVariables.spacingLayout3),
    containerPaddingSizeSmall = ODSPadding(all = DSVariables.spacingLayout2),
    containerVerticalAlignment = Alignment.Top,
    containerHorizontalAlignment = Alignment.CenterHorizontally,
    containerVerticalArrangement = Arrangement.Top,
    containerContentAlignment = Alignment.TopCenter,
    cardBgCornerRadiusSizeMedium = ODSCorners(all = DSVariables.radiusLarge),
    cardBgCornerRadiusSizeSmall = ODSCorners(all = DSVariables.radiusMedium),
    cardBgVerticalAlignment = Alignment.Top,
    cardBgHorizontalAlignment = Alignment.CenterHorizontally,
    cardBgVerticalArrangement = Arrangement.Top,
    cardBgBorder = DSVariables.strokes1,
    contentGap = DSVariables.spacingComponent5,
    contentVerticalAlignment = Alignment.Top,
    contentHorizontalAlignment = Alignment.Start,
    contentHorizontalArrangement = Arrangement.Start,
    contentContainerVerticalAlignment = Alignment.CenterVertically,
    contentContainerHorizontalAlignment = Alignment.Start,
    contentContainerVerticalArrangement = Arrangement.Center,
    selectorContainerRightVerticalAlignment = Alignment.CenterVertically,
    selectorContainerRightHorizontalAlignment = Alignment.CenterHorizontally,
    selectorContainerRightHorizontalArrangement = Arrangement.Center,
    selectorContainerRightPaddingSizeSmall = ODSPadding(right = DSVariables.spacingComponent3),
    arrowRightWidth = DSVariables.sizingComponent7,
    arrowRightHeight = DSVariables.sizingComponent7,
    scaleFactor = SCALE_FACTOR
)

var DSCardQuickActionTokens: ODSCardQuickActionTokens = defaultODSCardQuickActionTokens
