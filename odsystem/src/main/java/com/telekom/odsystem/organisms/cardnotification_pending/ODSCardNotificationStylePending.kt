// package com.telekom.odsystem.organisms.cardnotification
//
// import androidx.compose.foundation.layout.Arrangement
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.layout.ContentScale
// import androidx.compose.ui.text.style.TextAlign
// import androidx.compose.ui.unit.Dp
// import com.telekom.odsystem.foundations.HexColor
// import com.telekom.odsystem.foundations.ODSActions
// import com.telekom.odsystem.foundations.ODSColorModel
// import com.telekom.odsystem.foundations.ODSCorners
// import com.telekom.odsystem.foundations.ODSPadding
// import com.telekom.odsystem.foundations.ODSTextStyle
// import com.telekom.odsystem.tokens.ODSTheme
//
// @Suppress("LongMethod")
// class ODSCardNotificationStyle {
//    var padding: ODSPadding? = null
//    var verticalAlignment: Alignment.Vertical? = null
//    var horizontalAlignment: Alignment.Horizontal? = null
//    var verticalArrangement: Arrangement.Vertical? = null
//    var actionGap: Dp? = null
//    var actionPadding: ODSPadding? = null
//    var actionVerticalAlignment: Alignment.Vertical? = null
//    var actionHorizontalAlignment: Alignment.Horizontal? = null
//    var actionHorizontalArrangement: Arrangement.Horizontal? = null
//    var cardBgBackgroundColor: List<ODSColorModel>? = null
//    var cardBgBorderRadius: ODSCorners? = null
//    var cardBgWidth: Dp? = null // Not used in mobile
//    var cardBgHeight: Dp? = null // Not used in mobile
//    var cardBgVerticalAlignment: Alignment.Vertical? = null
//    var cardBgHorizontalAlignment: Alignment.Horizontal? = null
//    var cardBgVerticalArrangement: Arrangement.Vertical? = null
//    var headerContainerPadding: ODSPadding? = null
//    var headerContainerVerticalAlignment: Alignment.Vertical? = null
//    var headerContainerHorizontalAlignment: Alignment.Horizontal? = null
//    var headerContainerHorizontalArrangement: Arrangement.Horizontal? = null
//    var imageContainerBorderRadius: ODSCorners? = null // Not used in mobile
//    var imageContainerWidth: Dp? = null
//    var imageContainerHeight: Dp? = null
//    var imageWidth: Dp? = null
//    var imageHeight: Dp? = null
//    var imageObjectFit: ContentScale? = null
//    var buttonContainerPadding: ODSPadding? = null
//    var buttonContainerHeight: Dp? = null
//    var buttonContainerVerticalAlignment: Alignment.Vertical? = null
//    var buttonContainerHorizontalAlignment: Alignment.Horizontal? = null
//    var buttonContainerHorizontalArrangement: Arrangement.Horizontal? = null
//    var textContainerPadding: ODSPadding? = null
//    var textContainerVerticalAlignment: Alignment.Vertical? = null
//    var textContainerHorizontalAlignment: Alignment.Horizontal? = null
//    var textContainerHorizontalArrangement: Arrangement.Horizontal? = null
//    var headerTextStyle: ODSTextStyle? = null
//    var headerColor: HexColor? = null
//    var headerTextAlign: TextAlign? = null
//    var headerMaxWidth: Dp? = null
//    var imageContainerEndPadding: Dp? = null
//    var imageContainerVerticalOffset: Dp? = null
//    var scaleFactor: Float? = null
//    fun getStyle(
//        scheme: ODSTheme,
//        props: ODSCardNotificationProps,
//        state: ODSActions
//    ): ODSCardNotificationStyle {
//        var style = ODSCardNotificationStyle()
//        style.padding = DSCardNotificationTokens.padding
//        style.verticalAlignment = DSCardNotificationTokens.verticalAlignment
//        style.horizontalAlignment = DSCardNotificationTokens.horizontalAlignment
//        style.verticalArrangement = DSCardNotificationTokens.verticalArrangement
//        style.actionGap = DSCardNotificationTokens.actionGap
//        style.actionVerticalAlignment = DSCardNotificationTokens.actionVerticalAlignment
//        style.actionHorizontalAlignment = DSCardNotificationTokens.actionHorizontalAlignment
//        style.actionHorizontalArrangement = DSCardNotificationTokens.actionHorizontalArrangement
//        if (props.showImage) {
//            style.actionPadding = DSCardNotificationTokens.actionPaddingShowImage
//        }
//        if (!props.showImage) {
//            style.actionPadding = DSCardNotificationTokens.actionPadding
//        }
//        style.cardBgBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
//        style.cardBgBorderRadius = DSCardNotificationTokens.cardBgBorderRadius
//        style.cardBgWidth = DSCardNotificationTokens.cardBgWidth
//        style.cardBgHeight = DSCardNotificationTokens.cardBgHeight
//        style.cardBgVerticalAlignment = DSCardNotificationTokens.cardBgVerticalAlignment
//        style.cardBgHorizontalAlignment = DSCardNotificationTokens.cardBgHorizontalAlignment
//        style.cardBgVerticalArrangement = DSCardNotificationTokens.cardBgVerticalArrangement
//        if (state == ODSActions.HOVERED) {
//            style.cardBgWidth = DSCardNotificationTokens.cardBgWidthStateHovered
//            style.cardBgHeight = DSCardNotificationTokens.cardBgHeightStateHovered
//        }
//        style.headerContainerPadding = DSCardNotificationTokens.headerContainerPadding
//        style.headerContainerVerticalAlignment = DSCardNotificationTokens.headerContainerVerticalAlignment
//        style.headerContainerHorizontalAlignment = DSCardNotificationTokens.headerContainerHorizontalAlignment
//        style.headerContainerHorizontalArrangement = DSCardNotificationTokens.headerContainerHorizontalArrangement
//        style.imageContainerBorderRadius = DSCardNotificationTokens.imageContainerBorderRadius
//        style.imageContainerWidth = DSCardNotificationTokens.imageContainerWidth
//        style.imageContainerHeight = DSCardNotificationTokens.imageContainerHeight
//        if (state == ODSActions.HOVERED) {
//            style.imageContainerWidth = DSCardNotificationTokens.imageContainerWidthStateHovered
//            style.imageContainerHeight = DSCardNotificationTokens.imageContainerHeightStateHovered
//        }
//        if (props.showImage) {
//            style.imageWidth = DSCardNotificationTokens.imageWidthShowImage
//            style.imageHeight = DSCardNotificationTokens.imageHeightShowImage
//            style.imageObjectFit = DSCardNotificationTokens.imageObjectFitShowImage
//        }
//        style.buttonContainerPadding = DSCardNotificationTokens.buttonContainerPadding
//        style.buttonContainerHeight = DSCardNotificationTokens.buttonContainerHeight
//        style.buttonContainerVerticalAlignment = DSCardNotificationTokens.buttonContainerVerticalAlignment
//        style.buttonContainerHorizontalAlignment = DSCardNotificationTokens.buttonContainerHorizontalAlignment
//        style.buttonContainerHorizontalArrangement = DSCardNotificationTokens.buttonContainerHorizontalArrangement
//        style.textContainerPadding = DSCardNotificationTokens.textContainerPadding
//        style.textContainerVerticalAlignment = DSCardNotificationTokens.textContainerVerticalAlignment
//        style.textContainerHorizontalAlignment = DSCardNotificationTokens.textContainerHorizontalAlignment
//        style.textContainerHorizontalArrangement = DSCardNotificationTokens.textContainerHorizontalArrangement
//        style.headerTextStyle = DSCardNotificationTokens.headerTextStyle
//        style.headerColor = scheme.basicText
//        style.headerTextAlign = DSCardNotificationTokens.headerTextAlign
//        style.headerMaxWidth = DSCardNotificationTokens.headerMaxWidth
//        style.imageContainerEndPadding = DSCardNotificationTokens.imageContainerEndPadding
//        style.imageContainerVerticalOffset = DSCardNotificationTokens.imageContainerVerticalOffset
//        style.scaleFactor = DSCardNotificationTokens.scaleFactor
//        return style
//    }
// }
