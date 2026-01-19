package com.app.screentime.permission.component.infocard

import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Style configuration for InfoCard component.
 */
class InfoCardStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var titleColor: HexColor? = null
    var descriptionColor: HexColor? = null

    fun getStyle(scheme: ODSTheme): InfoCardStyle {
        val style = InfoCardStyle()
        style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        style.titleColor = scheme.basicText
        style.descriptionColor = scheme.basicTextRecessive
        return style
    }
}

