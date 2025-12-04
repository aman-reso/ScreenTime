package com.telekom.odsystem.organisms.cardproduct

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-19 (v1.31.6) - uid: 38f7406a
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=15585-710
 */

@Suppress("LongMethod")
class ODSCardProductStyle {
    var gap: Dp? = null
    var width: Dp? = null // Not used in mobile
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var cardZStackContentAlignment: Alignment? = null
    var cardGap: Dp? = null
    var cardPadding: ODSPadding? = null
    var cardVerticalAlignment: Alignment.Vertical? = null
    var cardHorizontalAlignment: Alignment.Horizontal? = null
    var cardVerticalArrangement: Arrangement.Vertical? = null
    var cardContentAlignment: Alignment? = null
    var cardBgBackground: List<ODSColorModel>? = null
    var cardBgCornerRadius: ODSCorners? = null
    var tagContainerVerticalAlignment: Alignment.Vertical? = null
    var tagContainerHorizontalAlignment: Alignment.Horizontal? = null
    var tagContainerVerticalArrangement: Arrangement.Vertical? = null
    var tagContainerPadding: ODSPadding? = null
    var imageContainerHeight: Dp? = null
    var imageContainerVerticalAlignment: Alignment.Vertical? = null
    var imageContainerHorizontalAlignment: Alignment.Horizontal? = null
    var imageContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var colourSwatchContainerPadding: ODSPadding? = null
    var colourSwatchContainerMinHeight: Dp? = null
    var colourSwatchContainerVerticalAlignment: Alignment.Vertical? = null
    var colourSwatchContainerHorizontalAlignment: Alignment.Horizontal? = null
    var colourSwatchContainerVerticalArrangement: Arrangement.Vertical? = null
    var descriptionGap: Dp? = null
    var descriptionVerticalAlignment: Alignment.Vertical? = null
    var descriptionHorizontalAlignment: Alignment.Horizontal? = null
    var descriptionVerticalArrangement: Arrangement.Vertical? = null
    var descriptionPadding: ODSPadding? = null
    var contentSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var contentSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var contentSlotContainerVerticalArrangement: Arrangement.Vertical? = null
    var featuresSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var featuresSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var featuresSlotContainerVerticalArrangement: Arrangement.Vertical? = null
    var priceSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var priceSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var priceSlotContainerVerticalArrangement: Arrangement.Vertical? = null
    var actionSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var actionSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var actionSlotContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var scaleFactor: Float? = null // Not exported by plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardProductProps
    ): ODSCardProductStyle {
        val style = ODSCardProductStyle()
        style.gap = DSCardProductTokens.gap
        style.width = DSCardProductTokens.width
        style.verticalAlignment = DSCardProductTokens.verticalAlignment
        style.horizontalAlignment = DSCardProductTokens.horizontalAlignment
        if (props.size == ODSCardProductSize.LARGE) {
            style.verticalArrangement = DSCardProductTokens.verticalArrangementSizeLarge
        }
        if (props.size == ODSCardProductSize.MEDIUM) {
            style.verticalArrangement = DSCardProductTokens.verticalArrangementSizeMedium
        }
        if (props.size == ODSCardProductSize.SMALL_V) {
            style.width = DSCardProductTokens.widthSizeSmallV
            style.verticalArrangement = DSCardProductTokens.verticalArrangementSizeSmallV
        }
        if (props.size == ODSCardProductSize.SMALL_H) {
            style.horizontalArrangement = DSCardProductTokens.horizontalArrangementSizeSmallH
        }
        style.cardZStackContentAlignment = DSCardProductTokens.cardZStackContentAlignment
        style.cardVerticalAlignment = DSCardProductTokens.cardVerticalAlignment
        style.cardHorizontalAlignment = DSCardProductTokens.cardHorizontalAlignment
        style.cardVerticalArrangement = DSCardProductTokens.cardVerticalArrangement
        style.cardContentAlignment = DSCardProductTokens.cardContentAlignment
        if (props.size == ODSCardProductSize.LARGE) {
            style.cardGap = DSCardProductTokens.cardGapSizeLarge
            style.cardPadding = DSCardProductTokens.cardPaddingSizeLarge
        }
        if (props.size == ODSCardProductSize.MEDIUM) {
            style.cardGap = DSCardProductTokens.cardGapSizeMedium
            style.cardPadding = DSCardProductTokens.cardPaddingSizeMedium
        }
        if (props.size == ODSCardProductSize.SMALL_V) {
            style.cardGap = DSCardProductTokens.cardGapSizeSmallV
            style.cardPadding = DSCardProductTokens.cardPaddingSizeSmallV
        }
        if (props.size == ODSCardProductSize.SMALL_H) {
            style.cardGap = DSCardProductTokens.cardGapSizeSmallH
            style.cardPadding = DSCardProductTokens.cardPaddingSizeSmallH
        }
        style.cardBgBackground = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        if (props.size == ODSCardProductSize.LARGE) {
            style.cardBgCornerRadius = DSCardProductTokens.cardBgCornerRadiusSizeLarge
        }
        if (props.size == ODSCardProductSize.MEDIUM) {
            style.cardBgCornerRadius = DSCardProductTokens.cardBgCornerRadiusSizeMedium
        }
        if (props.size == ODSCardProductSize.SMALL_V) {
            style.cardBgCornerRadius = DSCardProductTokens.cardBgCornerRadiusSizeSmallV
        }
        if (props.size == ODSCardProductSize.SMALL_H) {
            style.cardBgCornerRadius = DSCardProductTokens.cardBgCornerRadiusSizeSmallH
        }
        style.tagContainerVerticalAlignment = DSCardProductTokens.tagContainerVerticalAlignment
        style.tagContainerHorizontalAlignment = DSCardProductTokens.tagContainerHorizontalAlignment
        style.tagContainerVerticalArrangement = DSCardProductTokens.tagContainerVerticalArrangement
        if (props.size == ODSCardProductSize.LARGE) {
            style.tagContainerPadding = DSCardProductTokens.tagContainerPaddingSizeLarge
        }
        style.imageContainerVerticalAlignment = DSCardProductTokens.imageContainerVerticalAlignment
        style.imageContainerHorizontalAlignment =
            DSCardProductTokens.imageContainerHorizontalAlignment
        style.imageContainerHorizontalArrangement =
            DSCardProductTokens.imageContainerHorizontalArrangement
        if (props.size == ODSCardProductSize.LARGE) {
            style.imageContainerHeight = DSCardProductTokens.imageContainerHeightSizeLarge
        }
        if (props.size == ODSCardProductSize.MEDIUM) {
            style.imageContainerHeight = DSCardProductTokens.imageContainerHeightSizeMedium
        }
        if (props.size == ODSCardProductSize.SMALL_V) {
            style.imageContainerHeight = DSCardProductTokens.imageContainerHeightSizeSmallV
        }
        if (props.size == ODSCardProductSize.SMALL_H) {
            style.imageContainerHeight = DSCardProductTokens.imageContainerHeightSizeSmallH
        }
        style.colourSwatchContainerVerticalAlignment =
            DSCardProductTokens.colourSwatchContainerVerticalAlignment
        style.colourSwatchContainerHorizontalAlignment =
            DSCardProductTokens.colourSwatchContainerHorizontalAlignment
        style.colourSwatchContainerVerticalArrangement =
            DSCardProductTokens.colourSwatchContainerVerticalArrangement
        if (props.size == ODSCardProductSize.LARGE) {
            style.colourSwatchContainerPadding =
                DSCardProductTokens.colourSwatchContainerPaddingSizeLarge
            style.colourSwatchContainerMinHeight =
                DSCardProductTokens.colourSwatchContainerMinHeightSizeLarge
        }
        if (props.size == ODSCardProductSize.MEDIUM) {
            style.colourSwatchContainerPadding =
                DSCardProductTokens.colourSwatchContainerPaddingSizeMedium
            style.colourSwatchContainerMinHeight =
                DSCardProductTokens.colourSwatchContainerMinHeightSizeMedium
        }
        if (props.size == ODSCardProductSize.SMALL_V) {
            style.colourSwatchContainerPadding =
                DSCardProductTokens.colourSwatchContainerPaddingSizeSmallV
            style.colourSwatchContainerMinHeight =
                DSCardProductTokens.colourSwatchContainerMinHeightSizeSmallV
        }
        if (props.size == ODSCardProductSize.SMALL_H) {
            style.colourSwatchContainerPadding =
                DSCardProductTokens.colourSwatchContainerPaddingSizeSmallH
            style.colourSwatchContainerMinHeight =
                DSCardProductTokens.colourSwatchContainerMinHeightSizeSmallH
        }
        style.descriptionGap = DSCardProductTokens.descriptionGap
        style.descriptionVerticalAlignment = DSCardProductTokens.descriptionVerticalAlignment
        style.descriptionHorizontalAlignment = DSCardProductTokens.descriptionHorizontalAlignment
        style.descriptionVerticalArrangement = DSCardProductTokens.descriptionVerticalArrangement
        if (props.size == ODSCardProductSize.SMALL_H) {
            style.descriptionPadding = DSCardProductTokens.descriptionPaddingSizeSmallH
        }
        style.contentSlotContainerVerticalAlignment =
            DSCardProductTokens.contentSlotContainerVerticalAlignment
        style.contentSlotContainerHorizontalAlignment =
            DSCardProductTokens.contentSlotContainerHorizontalAlignment
        style.contentSlotContainerVerticalArrangement =
            DSCardProductTokens.contentSlotContainerVerticalArrangement
        style.featuresSlotContainerVerticalAlignment =
            DSCardProductTokens.featuresSlotContainerVerticalAlignment
        style.featuresSlotContainerHorizontalAlignment =
            DSCardProductTokens.featuresSlotContainerHorizontalAlignment
        style.featuresSlotContainerVerticalArrangement =
            DSCardProductTokens.featuresSlotContainerVerticalArrangement
        style.priceSlotContainerVerticalAlignment =
            DSCardProductTokens.priceSlotContainerVerticalAlignment
        style.priceSlotContainerHorizontalAlignment =
            DSCardProductTokens.priceSlotContainerHorizontalAlignment
        style.priceSlotContainerVerticalArrangement =
            DSCardProductTokens.priceSlotContainerVerticalArrangement
        style.actionSlotContainerVerticalAlignment =
            DSCardProductTokens.actionSlotContainerVerticalAlignment
        style.actionSlotContainerHorizontalAlignment =
            DSCardProductTokens.actionSlotContainerHorizontalAlignment
        style.actionSlotContainerHorizontalArrangement =
            DSCardProductTokens.actionSlotContainerHorizontalArrangement
        // Custom addition
        style.scaleFactor = DSCardProductTokens.scaleFactor
        return style
    }
}
