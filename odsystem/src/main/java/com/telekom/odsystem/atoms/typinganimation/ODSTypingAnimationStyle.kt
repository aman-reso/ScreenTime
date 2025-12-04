package com.telekom.odsystem.atoms.typinganimation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSTypingAnimationStyle {
    var gap: Dp? = null
    var padding: ODSPadding? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var dotBackground: List<ODSColorModel>? = null
    var dotCornerRadius: ODSCorners? = null
    var dotWidth: Dp? = null
    var dotHeight: Dp? = null
    var dotClipContent: Boolean? = null
    var dot2Background: List<ODSColorModel>? = null
    var dot2CornerRadius: ODSCorners? = null
    var dot2Width: Dp? = null
    var dot2Height: Dp? = null
    var dot2ClipContent: Boolean? = null
    var dot3Background: List<ODSColorModel>? = null
    var dot3CornerRadius: ODSCorners? = null
    var dot3Width: Dp? = null
    var dot3Height: Dp? = null
    var dot3ClipContent: Boolean? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSTypingAnimationProps
    ): ODSTypingAnimationStyle {
        val style = ODSTypingAnimationStyle()
        style.gap = DSTypingAnimationTokens.gap
        style.padding = DSTypingAnimationTokens.padding
        style.verticalAlignment = DSTypingAnimationTokens.verticalAlignment
        style.horizontalAlignment = DSTypingAnimationTokens.horizontalAlignment
        style.horizontalArrangement = DSTypingAnimationTokens.horizontalArrangement
        style.dotCornerRadius = DSTypingAnimationTokens.dotCornerRadius
        style.dotWidth = DSTypingAnimationTokens.dotWidth
        style.dotHeight = DSTypingAnimationTokens.dotHeight
        style.dotClipContent = DSTypingAnimationTokens.dotClipContent
        if (props.position == ODSTypingAnimationPosition.ONE) {
            style.dotBackground = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
        if (props.position == ODSTypingAnimationPosition.TWO) {
            style.dotBackground = listOf(ODSColorModel(hexColor = scheme.basicTextRecessive))
        }
        if (props.position == ODSTypingAnimationPosition.THREE) {
            style.dotBackground = listOf(ODSColorModel(hexColor = scheme.basicTextRecessive))
        }
        style.dot2CornerRadius = DSTypingAnimationTokens.dot2CornerRadius
        style.dot2Width = DSTypingAnimationTokens.dot2Width
        style.dot2Height = DSTypingAnimationTokens.dot2Height
        style.dot2ClipContent = DSTypingAnimationTokens.dot2ClipContent
        if (props.position == ODSTypingAnimationPosition.ONE) {
            style.dot2Background = listOf(ODSColorModel(hexColor = scheme.basicTextRecessive))
        }
        if (props.position == ODSTypingAnimationPosition.TWO) {
            style.dot2Background = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
        if (props.position == ODSTypingAnimationPosition.THREE) {
            style.dot2Background = listOf(ODSColorModel(hexColor = scheme.basicTextRecessive))
        }
        style.dot3CornerRadius = DSTypingAnimationTokens.dot3CornerRadius
        style.dot3Width = DSTypingAnimationTokens.dot3Width
        style.dot3Height = DSTypingAnimationTokens.dot3Height
        style.dot3ClipContent = DSTypingAnimationTokens.dot3ClipContent
        if (props.position == ODSTypingAnimationPosition.ONE) {
            style.dot3Background = listOf(ODSColorModel(hexColor = scheme.basicTextRecessive))
        }
        if (props.position == ODSTypingAnimationPosition.TWO) {
            style.dot3Background = listOf(ODSColorModel(hexColor = scheme.basicTextRecessive))
        }
        if (props.position == ODSTypingAnimationPosition.THREE) {
            style.dot3Background = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
        return style
    }
}
