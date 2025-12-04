// package com.telekom.odsystem.organisms.cardnotification
//
// import androidx.compose.foundation.layout.Arrangement
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.layout.ContentScale
// import androidx.compose.ui.text.style.TextAlign
// import androidx.compose.ui.unit.Dp
// import androidx.compose.ui.unit.dp
// import com.telekom.odsystem.DSTextStyles
// import com.telekom.odsystem.DSVariables
// import com.telekom.odsystem.foundations.ODSCorners
// import com.telekom.odsystem.foundations.ODSPadding
// import com.telekom.odsystem.foundations.ODSTextStyle
// import com.telekom.odsystem.foundations.SCALE_FACTOR
//
// data class ODSCardNotificationTokens(
//    var padding: ODSPadding,
//    var verticalAlignment: Alignment.Vertical,
//    var horizontalAlignment: Alignment.Horizontal,
//    var verticalArrangement: Arrangement.Vertical,
//    var actionGap: Dp,
//    var actionPaddingShowImage: ODSPadding,
//    var actionPadding: ODSPadding,
//    var actionVerticalAlignment: Alignment.Vertical,
//    var actionHorizontalAlignment: Alignment.Horizontal,
//    var actionHorizontalArrangement: Arrangement.Horizontal,
//    var cardBgBorderRadius: ODSCorners,
//    var cardBgWidth: Dp,
//    var cardBgWidthStateHovered: Dp,
//    var cardBgHeight: Dp,
//    var cardBgHeightStateHovered: Dp,
//    var cardBgVerticalAlignment: Alignment.Vertical,
//    var cardBgHorizontalAlignment: Alignment.Horizontal,
//    var cardBgVerticalArrangement: Arrangement.Vertical,
//    var headerContainerPadding: ODSPadding,
//    var headerContainerVerticalAlignment: Alignment.Vertical,
//    var headerContainerHorizontalAlignment: Alignment.Horizontal,
//    var headerContainerHorizontalArrangement: Arrangement.Horizontal,
//    var imageContainerBorderRadius: ODSCorners,
//    var imageContainerWidth: Dp,
//    var imageContainerWidthStateHovered: Dp,
//    var imageContainerHeight: Dp,
//    var imageContainerHeightStateHovered: Dp,
//    var imageWidthShowImage: Dp,
//    var imageHeightShowImage: Dp,
//    var imageObjectFitShowImage: ContentScale,
//    var buttonContainerPadding: ODSPadding,
//    var buttonContainerHeight: Dp,
//    var buttonContainerVerticalAlignment: Alignment.Vertical,
//    var buttonContainerHorizontalAlignment: Alignment.Horizontal,
//    var buttonContainerHorizontalArrangement: Arrangement.Horizontal,
//    var textContainerPadding: ODSPadding,
//    var textContainerVerticalAlignment: Alignment.Vertical,
//    var textContainerHorizontalAlignment: Alignment.Horizontal,
//    var textContainerHorizontalArrangement: Arrangement.Horizontal,
//    var headerTextStyle: ODSTextStyle,
//    var headerTextAlign: TextAlign,
//    var headerMaxWidth: Dp,
//    var imageContainerEndPadding: Dp, // Not exported from the plugin
//    var imageContainerVerticalOffset: Dp, // Not exported from the plugin
//    var scaleFactor: Float? = null
// )
//
// var defaultODSCardNotificationTokens = ODSCardNotificationTokens(
//    padding = ODSPadding(bottom = 22.dp),
//    verticalAlignment = Alignment.Top,
//    horizontalAlignment = Alignment.Start,
//    verticalArrangement = Arrangement.Top,
//    actionGap = DSVariables.spacingComponent2,
//    actionPaddingShowImage = ODSPadding(
//        top = DSVariables.spacingComponent3,
//        left = DSVariables.spacingComponent7,
//        right = 144.dp
//    ),
//    actionPadding = ODSPadding(
//        top = DSVariables.spacingComponent3,
//        left = DSVariables.spacingComponent7,
//        right = DSVariables.spacingLayout2
//    ),
//    actionVerticalAlignment = Alignment.Top,
//    actionHorizontalAlignment = Alignment.Start,
//    actionHorizontalArrangement = Arrangement.Start,
//    cardBgBorderRadius = ODSCorners(all = DSVariables.radiusMedium),
//    cardBgWidth = 480.dp,
//    cardBgWidthStateHovered = 488.dp,
//    cardBgHeight = 146.dp,
//    cardBgHeightStateHovered = 154.dp,
//    cardBgVerticalAlignment = Alignment.Top,
//    cardBgHorizontalAlignment = Alignment.CenterHorizontally,
//    cardBgVerticalArrangement = Arrangement.Top,
//    headerContainerPadding = ODSPadding(
//        left = DSVariables.spacingComponent7,
//        right = DSVariables.spacingComponent3
//    ),
//    headerContainerVerticalAlignment = Alignment.Top,
//    headerContainerHorizontalAlignment = Alignment.Start,
//    headerContainerHorizontalArrangement = Arrangement.Start,
//    imageContainerBorderRadius = ODSCorners(
//        topRight = DSVariables.radiusLarge,
//        bottomRight = DSVariables.radiusLarge
//    ),
//    imageContainerWidth = 480.dp,
//    imageContainerWidthStateHovered = 488.dp,
//    imageContainerHeight = 145.dp,
//    imageContainerHeightStateHovered = 154.dp,
//    imageWidthShowImage = 120.dp,
//    imageHeightShowImage = 120.dp,
//    imageObjectFitShowImage = ContentScale.Crop,
//    buttonContainerPadding = ODSPadding(top = DSVariables.spacingComponent3),
//    buttonContainerHeight = 68.dp,
//    buttonContainerVerticalAlignment = Alignment.Top,
//    buttonContainerHorizontalAlignment = Alignment.Start,
//    buttonContainerHorizontalArrangement = Arrangement.Start,
//    textContainerPadding = ODSPadding(
//        top = DSVariables.spacingComponent7,
//        bottom = DSVariables.spacingComponent2,
//        right = DSVariables.spacingLayout9
//    ),
//    textContainerVerticalAlignment = Alignment.Top,
//    textContainerHorizontalAlignment = Alignment.Start,
//    textContainerHorizontalArrangement = Arrangement.Start,
//    headerTextStyle = DSTextStyles.bodyMBold,
//    headerTextAlign = TextAlign.Left,
//    headerMaxWidth = 300.dp,
//    imageContainerEndPadding = 24.dp,
//    imageContainerVerticalOffset = 32.dp,
//    scaleFactor = SCALE_FACTOR
// )
//
// var DSCardNotificationTokens: ODSCardNotificationTokens = defaultODSCardNotificationTokens
