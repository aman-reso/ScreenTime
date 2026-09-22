// package com.telekom.odsystem.organisms.cardnotificationstack
//
// import androidx.compose.foundation.layout.Arrangement
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.unit.Dp
// import com.telekom.odsystem.foundations.ODSPadding
// import com.telekom.odsystem.tokens.ODSTheme
//
// class ODSCardNotificationStackStyle {
//    var minWidth: Dp? = null
//    var width: Dp? = null
//    var verticalAlignment: Alignment.Vertical? = null
//    var horizontalAlignment: Alignment.Horizontal? = null
//    var verticalArrangement: Arrangement.Vertical? = null
//    var gap: Dp? = null
//    var cardHolderGap: Dp? = null
//    var cardHolderHeight: Dp? = null
//    var cardHolderVerticalAlignment: Alignment.Vertical? = null
//    var cardHolderHorizontalAlignment: Alignment.Horizontal? = null
//    var cardHolderVerticalArrangement: Arrangement.Vertical? = null
//    var actionPadding: ODSPadding? = null
//    var actionVerticalAlignment: Alignment.Vertical? = null
//    var actionHorizontalAlignment: Alignment.Horizontal? = null
//    var actionVerticalArrangement: Arrangement.Vertical? = null
//    var actionHorizontalArrangement: Arrangement.Horizontal? = null
//    var odsNotificationCard2Width: Dp? = null
//    var odsNotificationCard3Width: Dp? = null
//    fun getStyle(
//        scheme: ODSTheme,
//        props: ODSCardNotificationStackProps
//    ): ODSCardNotificationStackStyle {
//        var style = ODSCardNotificationStackStyle()
//        style.minWidth = DSCardNotificationStackTokens.minWidth
//        style.width = DSCardNotificationStackTokens.width
//        style.verticalAlignment = DSCardNotificationStackTokens.verticalAlignment
//        style.horizontalAlignment = DSCardNotificationStackTokens.horizontalAlignment
//        style.verticalArrangement = DSCardNotificationStackTokens.verticalArrangement
//        if (props.expanded) {
//            style.gap = DSCardNotificationStackTokens.gapExpanded
//        }
//        style.cardHolderGap = DSCardNotificationStackTokens.cardHolderGap
//        style.cardHolderVerticalAlignment =
//            DSCardNotificationStackTokens.cardHolderVerticalAlignment
//        style.cardHolderHorizontalAlignment =
//            DSCardNotificationStackTokens.cardHolderHorizontalAlignment
//        style.cardHolderVerticalArrangement =
//            DSCardNotificationStackTokens.cardHolderVerticalArrangement
//        if (!props.expanded) {
//            style.cardHolderHeight = DSCardNotificationStackTokens.cardHolderHeight
//        }
//        style.actionVerticalAlignment = DSCardNotificationStackTokens.actionVerticalAlignment
//        if (props.expanded) {
//            style.actionPadding = DSCardNotificationStackTokens.actionPaddingExpanded
//            style.actionHorizontalAlignment =
//                DSCardNotificationStackTokens.actionHorizontalAlignmentExpanded
//            style.actionHorizontalArrangement =
//                DSCardNotificationStackTokens.actionHorizontalArrangementExpanded
//        }
//        if (!props.expanded) {
//            style.actionPadding = DSCardNotificationStackTokens.actionPadding
//            style.actionHorizontalAlignment =
//                DSCardNotificationStackTokens.actionHorizontalAlignment
//            style.actionVerticalArrangement =
//                DSCardNotificationStackTokens.actionVerticalArrangement
//        }
//        if (!props.expanded) {
//            style.odsNotificationCard2Width =
//                DSCardNotificationStackTokens.odsNotificationCard2Width
//        }
//        if (!props.expanded) {
//            style.odsNotificationCard3Width =
//                DSCardNotificationStackTokens.odsNotificationCard3Width
//        }
//        return style
//    }
// }
