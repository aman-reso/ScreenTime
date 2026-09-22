//package com.telekom.odsystem.organisms.cardpromo
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.unit.Dp
//import com.telekom.odsystem.foundations.HexColor
//import com.telekom.odsystem.foundations.ODSColorModel
//import com.telekom.odsystem.foundations.ODSCorners
//import com.telekom.odsystem.foundations.ODSLinearGradientModel
//import com.telekom.odsystem.foundations.ODSOffset
//import com.telekom.odsystem.foundations.ODSPadding
//import com.telekom.odsystem.tokens.tokens.ODSTheme
//
///**
// * Code generated with ODS RADD Code Generator
// * 2025-08-06 (v1.32.3) - uid: 48019f32
// * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=16907-23794
// */
//
//@Suppress("All")
//class ODSCardPromoStyle {
//    var zStackMinWidth: Dp? = null
//    var zStackClipContent: Boolean? = null
//    var zStackContentAlignment: Alignment? = null
//    var cornerRadius: ODSCorners? = null
//    var minWidth: Dp? = null
//    var clipContent: Boolean? = null
//    var verticalAlignment: Alignment.Vertical? = null
//    var horizontalAlignment: Alignment.Horizontal? = null
//    var verticalArrangement: Arrangement.Vertical? = null
//    var contentAlignment: Alignment? = null
//    var spacerMinHeight: Dp? = null
//    var spacerClipContent: Boolean? = null
//    var spacerVerticalAlignment: Alignment.Vertical? = null
//    var spacerHorizontalAlignment: Alignment.Horizontal? = null
//    var spacerHorizontalArrangement: Arrangement.Horizontal? = null
//    var bottomFadeBackground: List<ODSColorModel>? = null
//    var bottomFadePadding: ODSPadding? = null
//    var bottomFadeVerticalAlignment: Alignment.Vertical? = null
//    var bottomFadeHorizontalAlignment: Alignment.Horizontal? = null
//    var bottomFadeVerticalArrangement: Arrangement.Vertical? = null
//    var contentBackground: List<ODSColorModel>? = null
//    var contentPadding: ODSPadding? = null
//    var contentCornerRadius: ODSCorners? = null
//    var contentClipContent: Boolean? = null
//    var contentVerticalAlignment: Alignment.Vertical? = null
//    var contentHorizontalAlignment: Alignment.Horizontal? = null
//    var contentVerticalArrangement: Arrangement.Vertical? = null
//    var topFadeAbsoluteOffset: ODSOffset? = null
//    var topFadeAbsoluteContentAlignment: Alignment? = null
//    var topFadeBackground: List<ODSColorModel>? = null
//    var topFadeHeight: Dp? = null
//    var topFadeVerticalAlignment: Alignment.Vertical? = null
//    var topFadeHorizontalAlignment: Alignment.Horizontal? = null
//    var topFadeVerticalArrangement: Arrangement.Vertical? = null
//    fun getStyle(
//        scheme: ODSTheme,
//        props: ODSCardPromoProps
//    ): ODSCardPromoStyle {
//        val style = ODSCardPromoStyle()
//        style.zStackMinWidth = DSCardPromoTokens.zStackMinWidth
//        style.zStackClipContent = DSCardPromoTokens.zStackClipContent
//        if (props.type == ODSCardPromoType.CARD) {
//            style.zStackContentAlignment = DSCardPromoTokens.zStackContentAlignmentTypeCard
//        }
//        if (props.type == ODSCardPromoType.FADE) {
//            style.zStackContentAlignment = DSCardPromoTokens.zStackContentAlignmentTypeFade
//        }
//        style.cornerRadius = DSCardPromoTokens.cornerRadius
//        style.minWidth = DSCardPromoTokens.minWidth
//        style.clipContent = DSCardPromoTokens.clipContent
//        style.verticalAlignment = DSCardPromoTokens.verticalAlignment
//        style.verticalArrangement = DSCardPromoTokens.verticalArrangement
//        if (props.type == ODSCardPromoType.CARD) {
//            style.horizontalAlignment = DSCardPromoTokens.horizontalAlignmentTypeCard
//            style.contentAlignment = DSCardPromoTokens.contentAlignmentTypeCard
//        }
//        if (props.type == ODSCardPromoType.FADE) {
//            style.horizontalAlignment = DSCardPromoTokens.horizontalAlignmentTypeFade
//            style.contentAlignment = DSCardPromoTokens.contentAlignmentTypeFade
//        }
//        style.spacerMinHeight = DSCardPromoTokens.spacerMinHeight
//        style.spacerClipContent = DSCardPromoTokens.spacerClipContent
//        style.spacerVerticalAlignment = DSCardPromoTokens.spacerVerticalAlignment
//        style.spacerHorizontalAlignment = DSCardPromoTokens.spacerHorizontalAlignment
//        style.spacerHorizontalArrangement = DSCardPromoTokens.spacerHorizontalArrangement
//        style.bottomFadeBackground = listOf(
//            ODSColorModel(
//                gradient = ODSLinearGradientModel(
//                    colorStops = arrayOf(
//                        0.00f to HexColor("#000000", 0.00f),
//                        0.15f to HexColor("#000000", 1.00f)
//                    ),
//                    opacity = 1.00f,
//                    angleInDegrees = 180f
//                )
//            )
//        )
//        style.bottomFadeVerticalAlignment = DSCardPromoTokens.bottomFadeVerticalAlignment
//        style.bottomFadeHorizontalAlignment = DSCardPromoTokens.bottomFadeHorizontalAlignment
//        style.bottomFadeVerticalArrangement = DSCardPromoTokens.bottomFadeVerticalArrangement
//        if (props.type == ODSCardPromoType.CARD) {
//            style.bottomFadePadding = DSCardPromoTokens.bottomFadePaddingTypeCard
//        }
//        style.contentClipContent = DSCardPromoTokens.contentClipContent
//        style.contentVerticalAlignment = DSCardPromoTokens.contentVerticalAlignment
//        style.contentHorizontalAlignment = DSCardPromoTokens.contentHorizontalAlignment
//        style.contentVerticalArrangement = DSCardPromoTokens.contentVerticalArrangement
//        if (props.type == ODSCardPromoType.CARD) {
//            style.contentBackground = listOf(ODSColorModel(hexColor = scheme.basicBackground))
//            style.contentPadding = DSCardPromoTokens.contentPaddingTypeCard
//            style.contentCornerRadius = DSCardPromoTokens.contentCornerRadiusTypeCard
//        }
//        if (props.type == ODSCardPromoType.FADE) {
//            style.contentPadding = DSCardPromoTokens.contentPaddingTypeFade
//        }
//        if (props.type == ODSCardPromoType.FADE) {
//            style.topFadeAbsoluteOffset = DSCardPromoTokens.topFadeAbsoluteOffsetTypeFade
//            style.topFadeAbsoluteContentAlignment =
//                DSCardPromoTokens.topFadeAbsoluteContentAlignmentTypeFade
//        }
//        if (props.type == ODSCardPromoType.FADE) {
//            style.topFadeBackground = listOf(
//                ODSColorModel(
//                    gradient = ODSLinearGradientModel(
//                        colorStops = arrayOf(
//                            0.00f to HexColor("#000000", 0.64f),
//                            1.00f to HexColor("#000000", 0.00f)
//                        ),
//                        opacity = 1.00f,
//                        angleInDegrees = 180f
//                    )
//                )
//            )
//            style.topFadeHeight = DSCardPromoTokens.topFadeHeightTypeFade
//            style.topFadeVerticalAlignment = DSCardPromoTokens.topFadeVerticalAlignmentTypeFade
//            style.topFadeHorizontalAlignment = DSCardPromoTokens.topFadeHorizontalAlignmentTypeFade
//            style.topFadeVerticalArrangement = DSCardPromoTokens.topFadeVerticalArrangementTypeFade
//        }
//        return style
//    }
//}
