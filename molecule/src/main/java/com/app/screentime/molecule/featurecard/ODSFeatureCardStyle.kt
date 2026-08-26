package com.app.screentime.molecule.featurecard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Style configuration for the feature card component.
 */
class ODSFeatureCardStyle {
    var padding: ODSPadding? = null
    var cornerRadius: ODSCorners? = null
    var minHeight: Dp? = null
    var background: List<ODSColorModel>? = null
    var contentGap: Dp? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var iconContainerSize: Dp? = null
    var iconCornerRadius: ODSCorners? = null
    var textContentGap: Dp? = null
    var textContentVerticalArrangement: Arrangement.Vertical? = null
    var subtitleStyle: ODSTextStyle? = null
    var subtitleColor: HexColor? = null
    var titleStyle: ODSTextStyle? = null
    var titleColor: HexColor? = null
    var arrowIconColor: HexColor? = null
    var arrowIconSize: Dp? = null
    var wavyBackgroundOpacity: Float? = null

    fun getStyle(
        scheme: ODSTheme,
        props: ODSFeatureCardProps,
        state: ODSActions
    ): ODSFeatureCardStyle {
        val style = ODSFeatureCardStyle()
        val tokens = DSFeatureCardTokens
        
        // Card padding - from tokens
        style.padding = tokens.padding
        
        // Card corner radius - from tokens
        style.cornerRadius = tokens.cornerRadius
        
        // Minimum height - from tokens
        style.minHeight = tokens.minHeight
        
        // Background color - from scheme based on state
        style.background = when (state) {
            ODSActions.HOVERED -> listOf(
                ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover)
            )
            ODSActions.PRESSED -> listOf(
                ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed)
            )
            else -> listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        }
        
        // Content gap between icon and text - from tokens
        style.contentGap = tokens.contentGap
        
        // Content arrangement - from tokens
        style.contentHorizontalArrangement = tokens.contentHorizontalArrangement
        style.contentVerticalAlignment = tokens.contentVerticalAlignment
        
        // Icon container size (circular) - from tokens
        style.iconContainerSize = tokens.iconContainerSize
        // Icon corner radius (circular) - from tokens
        style.iconCornerRadius = tokens.iconCornerRadius
        
        // Text content gap - from tokens
        style.textContentGap = tokens.textContentGap
        style.textContentVerticalArrangement = tokens.textContentVerticalArrangement
        
        // Subtitle style - from tokens
        style.subtitleStyle = tokens.subtitleStyle
        style.subtitleColor = scheme.basicText
        
        // Title style - from tokens
        style.titleStyle = tokens.titleStyle
        style.titleColor = scheme.basicText
        
        // Arrow icon - from tokens
        style.arrowIconColor = scheme.basicText
        style.arrowIconSize = tokens.arrowIconSize
        
        // Wavy background decoration opacity - from tokens
        style.wavyBackgroundOpacity = tokens.wavyBackgroundOpacity
        
        return style
    }
}
