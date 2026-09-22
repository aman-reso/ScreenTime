package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSDialogPreferredContentTokens(
    var gap: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var imageContainerMaxHeightTypeGeneralWithImage: Dp,
    var imageContainerClipContentTypeGeneralWithImage: Boolean,
    var imageContainerVerticalAlignmentTypeGeneralWithImage: Alignment.Vertical,
    var imageContainerHorizontalAlignmentTypeGeneralWithImage: Alignment.Horizontal,
    var imageContainerHorizontalArrangementTypeGeneralWithImage: Arrangement.Horizontal,
    var imageWidthTypeGeneralWithImage: Dp,
    var imageHeightTypeGeneralWithImage: Dp,
    var imageObjectFitTypeGeneralWithImage: ContentScale,
    var labelHeadingGapTypeGeneralWithImage: Dp,
    var labelHeadingVerticalAlignmentTypeGeneralWithImage: Alignment.Vertical,
    var labelHeadingHorizontalAlignmentTypeGeneralWithImage: Alignment.Horizontal,
    var labelHeadingVerticalArrangementTypeGeneralWithImage: Arrangement.Vertical,
    var bodyContentGap: Dp,
    var bodyContentVerticalAlignment: Alignment.Vertical,
    var bodyContentHorizontalAlignment: Alignment.Horizontal,
    var bodyContentVerticalArrangement: Arrangement.Vertical,
    var headerTextStyleTypeGeneralWithImage: ODSTextStyle,
    var headerTextAlignTypeGeneralWithImage: TextAlign,
    var subtitleTextStyleTypeGeneralWithImage: ODSTextStyle,
    var subtitleTextAlignTypeGeneralWithImage: TextAlign,
    var bodyTextTextStyle: ODSTextStyle,
    var bodyTextTextAlign: TextAlign
)

var defaultODSDialogPreferredContentTokens = ODSDialogPreferredContentTokens(
    gap = DSVariables.spacingComponent7,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Center,
    imageContainerMaxHeightTypeGeneralWithImage = 160.dp,
    imageContainerClipContentTypeGeneralWithImage = true,
    imageContainerVerticalAlignmentTypeGeneralWithImage = Alignment.Top,
    imageContainerHorizontalAlignmentTypeGeneralWithImage = Alignment.CenterHorizontally,
    imageContainerHorizontalArrangementTypeGeneralWithImage = Arrangement.Center,
    imageWidthTypeGeneralWithImage = 535.dp,
    imageHeightTypeGeneralWithImage = 160.dp,
    imageObjectFitTypeGeneralWithImage = ContentScale.Crop,
    labelHeadingGapTypeGeneralWithImage = DSVariables.spacingComponent3,
    labelHeadingVerticalAlignmentTypeGeneralWithImage = Alignment.CenterVertically,
    labelHeadingHorizontalAlignmentTypeGeneralWithImage = Alignment.Start,
    labelHeadingVerticalArrangementTypeGeneralWithImage = Arrangement.Center,
    bodyContentGap = DSVariables.spacingComponent5,
    bodyContentVerticalAlignment = Alignment.CenterVertically,
    bodyContentHorizontalAlignment = Alignment.Start,
    bodyContentVerticalArrangement = Arrangement.Center,
    headerTextStyleTypeGeneralWithImage = DSTextStyles.titleS,
    headerTextAlignTypeGeneralWithImage = TextAlign.Left,
    subtitleTextStyleTypeGeneralWithImage = DSTextStyles.subtitle,
    subtitleTextAlignTypeGeneralWithImage = TextAlign.Left,
    bodyTextTextStyle = DSTextStyles.bodyMRegular,
    bodyTextTextAlign = TextAlign.Left
)

var DSDialogPreferredContentTokens: ODSDialogPreferredContentTokens =
    defaultODSDialogPreferredContentTokens
