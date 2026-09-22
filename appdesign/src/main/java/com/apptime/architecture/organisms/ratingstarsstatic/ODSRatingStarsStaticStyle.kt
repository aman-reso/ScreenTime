package com.telekom.odsystem.organisms.ratingstarsstatic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSRatingStarsStaticTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSRatingStarsStaticStyle {
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var labelContainerGap: Dp? = null
    var labelContainerPadding: ODSPadding? = null
    var labelContainerVerticalAlignment: Alignment.Vertical? = null
    var labelContainerHorizontalAlignment: Alignment.Horizontal? = null
    var labelContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var ratingGap: Dp? = null
    var ratingVerticalAlignment: Alignment.Vertical? = null
    var ratingHorizontalAlignment: Alignment.Horizontal? = null
    var ratingHorizontalArrangement: Arrangement.Horizontal? = null
    var starsListContainerGap: Dp? = null
    var starsListContainerVerticalAlignment: Alignment.Vertical? = null
    var starsListContainerHorizontalAlignment: Alignment.Horizontal? = null
    var starsListContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var starColor: HexColor? = null
    var starWidth: Dp? = null
    var starHeight: Dp? = null
    var helperTextTextStyle: ODSTextStyle? = null
    var helperTextColor: HexColor? = null
    var helperTextTextAlign: TextAlign? = null
    var ratingLabelTextStyle: ODSTextStyle? = null
    var ratingLabelColor: HexColor? = null
    var ratingLabelTextAlign: TextAlign? = null
    var starColorUnselected: HexColor? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSRatingStarsStaticProps
    ): ODSRatingStarsStaticStyle {
        var style = ODSRatingStarsStaticStyle()
        style.verticalAlignment = DSRatingStarsStaticTokens.verticalAlignment
        style.horizontalAlignment = DSRatingStarsStaticTokens.horizontalAlignment
        style.verticalArrangement = DSRatingStarsStaticTokens.verticalArrangement
        style.labelContainerGap = DSRatingStarsStaticTokens.labelContainerGap
        style.labelContainerPadding = DSRatingStarsStaticTokens.labelContainerPadding
        style.labelContainerVerticalAlignment =
            DSRatingStarsStaticTokens.labelContainerVerticalAlignment
        style.labelContainerHorizontalAlignment =
            DSRatingStarsStaticTokens.labelContainerHorizontalAlignment
        style.labelContainerHorizontalArrangement =
            DSRatingStarsStaticTokens.labelContainerHorizontalArrangement
        style.ratingGap = DSRatingStarsStaticTokens.ratingGap
        style.ratingVerticalAlignment = DSRatingStarsStaticTokens.ratingVerticalAlignment
        style.ratingHorizontalAlignment = DSRatingStarsStaticTokens.ratingHorizontalAlignment
        style.ratingHorizontalArrangement = DSRatingStarsStaticTokens.ratingHorizontalArrangement
        style.starsListContainerGap = DSRatingStarsStaticTokens.starsListContainerGap
        style.starsListContainerVerticalAlignment =
            DSRatingStarsStaticTokens.starsListContainerVerticalAlignment
        style.starsListContainerHorizontalAlignment =
            DSRatingStarsStaticTokens.starsListContainerHorizontalAlignment
        style.starsListContainerHorizontalArrangement =
            DSRatingStarsStaticTokens.starsListContainerHorizontalArrangement
        style.starColor = scheme.basicTextDominant
        style.starWidth = DSRatingStarsStaticTokens.starWidth
        style.starHeight = DSRatingStarsStaticTokens.starHeight
        style.helperTextTextStyle = DSRatingStarsStaticTokens.helperTextTextStyle
        style.helperTextColor = scheme.basicTextRecessive
        style.helperTextTextAlign = DSRatingStarsStaticTokens.helperTextTextAlign
        style.ratingLabelTextStyle = DSRatingStarsStaticTokens.ratingLabelTextStyle
        style.ratingLabelColor = scheme.basicText
        style.ratingLabelTextAlign = DSRatingStarsStaticTokens.ratingLabelTextAlign
        style.starColorUnselected = scheme.basicStroke
        return style
    }
}
