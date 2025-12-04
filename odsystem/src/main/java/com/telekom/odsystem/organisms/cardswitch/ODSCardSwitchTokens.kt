package com.telekom.odsystem.organisms.cardswitch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.foundations.SCALE_FACTOR

data class ODSCardSwitchTokens(
    var borderRadius: ODSCorners,
    var width: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangement: Arrangement.Horizontal,
    var contentGap: Dp,
    var contentPadding: ODSPadding,
    var contentVerticalAlignment: Alignment.Vertical,
    var contentHorizontalAlignment: Alignment.Horizontal,
    var contentHorizontalArrangement: Arrangement.Horizontal,
    var cardContentGap: Dp,
    var cardContentVerticalAlignment: Alignment.Vertical,
    var cardContentHorizontalAlignment: Alignment.Horizontal,
    var cardContentVerticalArrangement: Arrangement.Vertical,
    var cardBgBorderRadius: ODSCorners,
    var cardBgWidthStatePressed: Dp,
    var cardBgWidth: Dp,
    var cardBgHeightStatePressed: Dp,
    var cardBgHeight: Dp,
    var cardBgClipContent: Boolean,
    var cardBgVerticalAlignment: Alignment.Vertical,
    var cardBgHorizontalAlignment: Alignment.Horizontal,
    var cardBgVerticalArrangement: Arrangement.Vertical,
    var cardBgBorderSelected: Dp,
    var copyGap: Dp,
    var copyVerticalAlignment: Alignment.Vertical,
    var copyHorizontalAlignment: Alignment.Horizontal,
    var copyVerticalArrangement: Arrangement.Vertical,
    var tagsContainerGap: Dp,
    var tagsContainerVerticalAlignment: Alignment.Vertical,
    var tagsContainerHorizontalAlignment: Alignment.Horizontal,
    var tagsContainerHorizontalArrangement: Arrangement.Horizontal,
    var logoContainerHeightTypeBrand: Dp,
    var logoContainerClipContentTypeBrand: Boolean,
    var logoContainerVerticalAlignmentTypeBrand: Alignment.Vertical,
    var logoContainerHorizontalAlignmentTypeBrand: Alignment.Horizontal,
    var logoContainerVerticalArrangementTypeBrand: Arrangement.Vertical,
    var logoObjectFitTypeBrand: ContentScale, // Not exported from the plugin
    var titleTextStyleTypeTitle: ODSTextStyle,
    var titleTextAlignTypeTitle: TextAlign,
    var subtitleTextStyle: ODSTextStyle,
    var subtitleTextAlign: TextAlign,
    var scaleFactor: Float // Not exported from the plugin
)

var defaultODSCardSwitchTokens = ODSCardSwitchTokens(
    borderRadius = ODSCorners(all = DSVariables.radiusMedium),
    width = DSVariables.columns6Columns,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    contentGap = DSVariables.spacingComponent4,
    contentPadding = ODSPadding(
        top = DSVariables.spacingComponent7,
        bottom = DSVariables.spacingComponent7,
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent5
    ),
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalAlignment = Alignment.CenterHorizontally,
    contentHorizontalArrangement = Arrangement.Center,
    cardContentGap = DSVariables.spacingComponent6,
    cardContentVerticalAlignment = Alignment.Top,
    cardContentHorizontalAlignment = Alignment.Start,
    cardContentVerticalArrangement = Arrangement.Top,
    cardBgBorderRadius = ODSCorners(all = DSVariables.radiusMedium),
    cardBgWidthStatePressed = 312.dp,
    cardBgWidth = 312.dp,
    cardBgHeightStatePressed = 140.dp,
    cardBgHeight = 140.dp,
    cardBgClipContent = true,
    cardBgVerticalAlignment = Alignment.Top,
    cardBgHorizontalAlignment = Alignment.CenterHorizontally,
    cardBgVerticalArrangement = Arrangement.Top,
    cardBgBorderSelected = DSVariables.strokes2,
    copyGap = DSVariables.spacingComponent2,
    copyVerticalAlignment = Alignment.CenterVertically,
    copyHorizontalAlignment = Alignment.Start,
    copyVerticalArrangement = Arrangement.Center,
    tagsContainerGap = DSVariables.spacingComponent3,
    tagsContainerVerticalAlignment = Alignment.Top,
    tagsContainerHorizontalAlignment = Alignment.Start,
    tagsContainerHorizontalArrangement = Arrangement.Start,
    logoContainerHeightTypeBrand = DSVariables.sizingComponent10,
    logoContainerClipContentTypeBrand = true,
    logoContainerVerticalAlignmentTypeBrand = Alignment.CenterVertically,
    logoContainerHorizontalAlignmentTypeBrand = Alignment.Start,
    logoContainerVerticalArrangementTypeBrand = Arrangement.Center,
    logoObjectFitTypeBrand = ContentScale.Fit,
    titleTextStyleTypeTitle = DSTextStyles.bodyL,
    titleTextAlignTypeTitle = TextAlign.Left,
    subtitleTextStyle = DSTextStyles.bodyMBold,
    subtitleTextAlign = TextAlign.Left,
    scaleFactor = SCALE_FACTOR
)

var DSCardSwitchTokens: ODSCardSwitchTokens = defaultODSCardSwitchTokens
