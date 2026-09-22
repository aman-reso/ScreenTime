package com.telekom.odsystem.organisms.ratingstarsinteractive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSRatingStarsInteractiveTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSRatingStarsInteractiveStyle {
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var labelContainerGap: Dp? = null
    var labelContainerPadding: ODSPadding? = null
    var labelContainerVerticalAlignment: Alignment.Vertical? = null
    var labelContainerHorizontalAlignment: Alignment.Horizontal? = null
    var labelContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var ratingLabelTextStyle: ODSTextStyle? = null
    var ratingLabelColor: HexColor? = null
    var ratingLabelTextAlign: TextAlign? = null
    var ratingGap: Dp? = null
    var ratingVerticalAlignment: Alignment.Vertical? = null
    var ratingHorizontalAlignment: Alignment.Horizontal? = null
    var ratingHorizontalArrangement: Arrangement.Horizontal? = null
    var starsListContainerVerticalAlignment: Alignment.Vertical? = null
    var starsListContainerHorizontalAlignment: Alignment.Horizontal? = null
    var starsListContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var helperTextTextStyle: ODSTextStyle? = null
    var helperTextColor: HexColor? = null
    var helperTextTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSRatingStarsInteractiveProps
    ): ODSRatingStarsInteractiveStyle {
        val style = ODSRatingStarsInteractiveStyle()
        style.verticalAlignment = DSRatingStarsInteractiveTokens.verticalAlignment
        style.horizontalAlignment = DSRatingStarsInteractiveTokens.horizontalAlignment
        style.verticalArrangement = DSRatingStarsInteractiveTokens.verticalArrangement
        style.labelContainerGap = DSRatingStarsInteractiveTokens.labelContainerGap
        style.labelContainerPadding = DSRatingStarsInteractiveTokens.labelContainerPadding
        style.labelContainerVerticalAlignment =
            DSRatingStarsInteractiveTokens.labelContainerVerticalAlignment
        style.labelContainerHorizontalAlignment =
            DSRatingStarsInteractiveTokens.labelContainerHorizontalAlignment
        style.labelContainerHorizontalArrangement =
            DSRatingStarsInteractiveTokens.labelContainerHorizontalArrangement
        style.ratingLabelTextStyle = DSRatingStarsInteractiveTokens.ratingLabelTextStyle
        style.ratingLabelTextAlign = DSRatingStarsInteractiveTokens.ratingLabelTextAlign
        if (!props.readOnly && !props.disabled) {
            style.ratingLabelColor = scheme.basicText
        }
        if (!props.readOnly && props.disabled) {
            style.ratingLabelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.readOnly && !props.disabled) {
            style.ratingLabelColor = scheme.basicTextRecessive
        }
        style.ratingGap = DSRatingStarsInteractiveTokens.ratingGap
        style.ratingVerticalAlignment = DSRatingStarsInteractiveTokens.ratingVerticalAlignment
        style.ratingHorizontalAlignment = DSRatingStarsInteractiveTokens.ratingHorizontalAlignment
        style.ratingHorizontalArrangement =
            DSRatingStarsInteractiveTokens.ratingHorizontalArrangement
        style.starsListContainerVerticalAlignment =
            DSRatingStarsInteractiveTokens.starsListContainerVerticalAlignment
        style.starsListContainerHorizontalAlignment =
            DSRatingStarsInteractiveTokens.starsListContainerHorizontalAlignment
        style.starsListContainerHorizontalArrangement =
            DSRatingStarsInteractiveTokens.starsListContainerHorizontalArrangement
        style.helperTextTextStyle = DSRatingStarsInteractiveTokens.helperTextTextStyle
        style.helperTextTextAlign = DSRatingStarsInteractiveTokens.helperTextTextAlign
        if (!props.disabled) {
            style.helperTextColor = scheme.basicTextRecessive
        }
        if (!props.readOnly && props.disabled) {
            style.helperTextColor = scheme.interactionStatesDisabledTextDisabled
        }
        return style
    }
}
