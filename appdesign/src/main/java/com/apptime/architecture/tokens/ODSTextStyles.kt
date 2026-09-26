// Do not edit directly
// Generated on Thu, 06 Nov 2025 15:32:44 GMT


package com.telekom.odsystem.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import com.telekom.odsystem.foundations.ODSTextStyle
import androidx.compose.ui.unit.*
import com.telekom.odsystem.R

object ODSTextStyles {
    val bodyL =
        ODSTextStyle(fontFamily = R.font.funnelsans_medium, fontSize = 20, lineHeight = 24)
    val bodyLBold =
        ODSTextStyle(fontFamily = R.font.funnelsans_semibold, fontSize = 20, lineHeight = 24)
    val bodyMBold =
        ODSTextStyle(fontFamily = R.font.funnelsans_semibold, fontSize = 16, lineHeight = 20)
    val bodyMRegular =
        ODSTextStyle(fontFamily = R.font.funnelsans_regular, fontSize = 16, lineHeight = 20)
    val bodySBold =
        ODSTextStyle(fontFamily = R.font.funnelsans_semibold, fontSize = 14, lineHeight = 18)
    val bodySRegular =
        ODSTextStyle(fontFamily = R.font.funnelsans_regular, fontSize = 14, lineHeight = 18)
    val display =
        ODSTextStyle(fontFamily = R.font.funnelsans_regular, fontSize = 72, lineHeight = 72)
    val linkMBold =
        ODSTextStyle(fontFamily = R.font.funnelsans_semibold, fontSize = 16, lineHeight = 20)
    val linkMRegular =
        ODSTextStyle(fontFamily = R.font.funnelsans_regular, fontSize = 16, lineHeight = 20)
    val linkSBold =
        ODSTextStyle(fontFamily = R.font.funnelsans_semibold, fontSize = 14, lineHeight = 18)
    val linkSRegular =
        ODSTextStyle(fontFamily = R.font.funnelsans_regular, fontSize = 14, lineHeight = 18)
    val microcopyBold =
        ODSTextStyle(fontFamily = R.font.funnelsans_semibold, fontSize = 14, lineHeight = 16)
    val microcopyRegular =
        ODSTextStyle(fontFamily = R.font.funnelsans_regular, fontSize = 14, lineHeight = 16)
    val microcopyMedium =
        ODSTextStyle(fontFamily = R.font.funnelsans_medium, fontSize = 12, lineHeight = 16)
    val paragraph =
        ODSTextStyle(fontFamily = R.font.funnelsans_medium, fontSize = 20, lineHeight = 26)
    val subtitle =
        ODSTextStyle(fontFamily = R.font.funnelsans_semibold, fontSize = 22, lineHeight = 22)
    val titleL =
        ODSTextStyle(fontFamily = R.font.funnelsans_semibold, fontSize = 32, lineHeight = 32)
    val titleM =
        ODSTextStyle(fontFamily = R.font.funnelsans_semibold, fontSize = 28, lineHeight = 28)
    val titleS =
        ODSTextStyle(fontFamily = R.font.funnelsans_semibold, fontSize = 24, lineHeight = 24)

    val logoBody =
        ODSTextStyle(
            fontFamily = R.font.pacifico_regular,
            fontSize = 24,
            lineHeight = 24,
            fontWeight = FontWeight.Bold
        )
}

@Composable
fun ODSTextStyle.toTextStyleIgnoreScale(): TextStyle {
    val fontScale = LocalDensity.current.fontScale
    val weight = fontWeight ?: FontWeight.Normal
    return TextStyle(
        fontFamily = FontFamily(
            Font(fontFamily, weight = weight)
        ),
        fontWeight = weight,
        fontSize = (fontSize.sp / fontScale),
        lineHeight = (lineHeight.sp / fontScale),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(
            includeFontPadding = true
        )
    )
}
