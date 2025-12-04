package com.telekom.odsystem.organisms.cardquickactiondeprecated

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

data class ODSCardQuickActionDeprecatedTokens(
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
    var cardBgWidth: Dp,
    var cardBgWidthStatePressed: Dp,
    var cardBgHeight: Dp,
    var cardBgHeightStatePressed: Dp,
    var cardBgVerticalAlignment: Alignment.Vertical,
    var cardBgHorizontalAlignment: Alignment.Horizontal,
    var cardBgVerticalArrangement: Arrangement.Vertical,
    var copyAndSparklineGap: Dp,
    var copyAndSparklineVerticalAlignment: Alignment.Vertical,
    var copyAndSparklineHorizontalAlignment: Alignment.Horizontal,
    var copyAndSparklineVerticalArrangement: Arrangement.Vertical,
    var tagsContainerGap: Dp,
    var tagsContainerVerticalAlignment: Alignment.Vertical,
    var tagsContainerHorizontalAlignment: Alignment.Horizontal,
    var tagsContainerHorizontalArrangement: Arrangement.Horizontal,
    var arrowRightWidth: Dp,
    var arrowRightHeight: Dp,
    var logoContainerHeightTypeBrand: Dp,
    var logoContainerVerticalAlignmentTypeBrand: Alignment.Vertical,
    var logoContainerHorizontalAlignmentTypeBrand: Alignment.Horizontal,
    var logoContainerVerticalArrangementTypeBrand: Arrangement.Vertical,
    var logoObjectFitTypeBrand: ContentScale, // Not exported by plugin
    var titleTextStyleTypeTitle: ODSTextStyle,
    var titleTextAlignTypeTitle: TextAlign,
    var subtitleTextStyle: ODSTextStyle,
    var subtitleTextAlign: TextAlign,
    var scaleFactor: Float, // Not exported by plugin, Custom addition should be documented
)

var defaultODSCardQuickActionTokens = ODSCardQuickActionDeprecatedTokens(
    borderRadius = ODSCorners(all = DSVariables.radiusMedium),
    width = DSVariables.columns6Columns,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    contentGap = DSVariables.spacingComponent7,
    contentPadding = ODSPadding(
        top = DSVariables.spacingComponent7,
        bottom = DSVariables.spacingComponent7,
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent8
    ),
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalAlignment = Alignment.CenterHorizontally,
    contentHorizontalArrangement = Arrangement.Center,
    cardContentGap = DSVariables.spacingComponent6,
    cardContentVerticalAlignment = Alignment.Top,
    cardContentHorizontalAlignment = Alignment.Start,
    cardContentVerticalArrangement = Arrangement.Top,
    cardBgBorderRadius = ODSCorners(all = DSVariables.radiusMedium),
    cardBgWidth = 312.dp,
    cardBgWidthStatePressed = 312.dp,
    cardBgHeight = 160.dp,
    cardBgHeightStatePressed = 160.dp,
    cardBgVerticalAlignment = Alignment.Top,
    cardBgHorizontalAlignment = Alignment.CenterHorizontally,
    cardBgVerticalArrangement = Arrangement.Top,
    copyAndSparklineGap = DSVariables.spacingComponent2,
    copyAndSparklineVerticalAlignment = Alignment.CenterVertically,
    copyAndSparklineHorizontalAlignment = Alignment.Start,
    copyAndSparklineVerticalArrangement = Arrangement.Center,
    tagsContainerGap = DSVariables.spacingComponent3,
    tagsContainerVerticalAlignment = Alignment.Top,
    tagsContainerHorizontalAlignment = Alignment.Start,
    tagsContainerHorizontalArrangement = Arrangement.Start,
    arrowRightWidth = DSVariables.sizingComponent7,
    arrowRightHeight = DSVariables.sizingComponent7,
    logoContainerHeightTypeBrand = DSVariables.sizingComponent10,
    logoContainerVerticalAlignmentTypeBrand = Alignment.CenterVertically,
    logoContainerHorizontalAlignmentTypeBrand = Alignment.Start,
    logoContainerVerticalArrangementTypeBrand = Arrangement.Center,
    logoObjectFitTypeBrand = ContentScale.Fit,
    titleTextStyleTypeTitle = DSTextStyles.bodyMBold,
    titleTextAlignTypeTitle = TextAlign.Left,
    subtitleTextStyle = DSTextStyles.bodySRegular,
    subtitleTextAlign = TextAlign.Left,
    scaleFactor = SCALE_FACTOR
)

var DSCardQuickActionTokens: ODSCardQuickActionDeprecatedTokens = defaultODSCardQuickActionTokens
