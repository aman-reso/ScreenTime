package com.app.screentime.permission.component.herosection

import androidx.compose.ui.Alignment
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Style configuration for HeroSection component.
 */
class HeroSectionStyle {
    var iconTint: HexColor? = null
    var titleColor: HexColor? = null
    var diamondGradient: List<ODSColorModel>? = null
    var contentAlignment: Alignment? = null

    fun getStyle(scheme: ODSTheme): HeroSectionStyle {
        val style = HeroSectionStyle()
        style.iconTint =  HexColor("D0BCFF")
        style.titleColor = scheme.basicText
        style.diamondGradient = listOf(
            ODSColorModel(
                gradient = ODSLinearGradientModel(
                    Pair(0f, HexColor("FF0080")),
                    Pair(0.5f, HexColor("7928CA")),
                    Pair(1f, HexColor("FF0080")),
                    angleInDegrees = 0f
                )
            )
        )
        style.contentAlignment = Alignment.Center
        return style
    }
}

