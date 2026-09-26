@file:Suppress("ALL")

package com.telekom.odsystem.foundations

import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.TextViewCompat

data class ODSTextStyle(
    val fontFamily: Int,
    val fontSize: Int,
    val lineHeight: Int,
    val fontWeight: FontWeight? = null
) {

    fun toTextStyle(): TextStyle {
        if (fontFamily == 0) {
            return TextStyle(
                fontSize = fontSize.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = fontWeight ?: FontWeight.Normal,
                lineHeight = lineHeight.sp,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None
                ),
                platformStyle = PlatformTextStyle(
                    includeFontPadding = true
                )
            )
        }

        val weight = fontWeight ?: FontWeight.Normal
        return TextStyle(
            fontSize = fontSize.sp,
            fontFamily = FontFamily(Font(fontFamily, weight = weight)),
            fontWeight = weight,
            lineHeight = lineHeight.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
            platformStyle = PlatformTextStyle(
                includeFontPadding = true
            )
        )
    }

    fun getFontSizeAsSp(): Int {
        return fontSize.toFloat().dp.value.toInt()
    }

    // Create a function to apply the ODSTextStyle to a TextView
    fun applyTextStyle(textView: TextView) {
        val fontSize = this.fontSize.toFloat()
        val fontLineHeight = this.lineHeight.toFloat().dp.value.toInt() - fontSize.toInt()

        textView.apply {
            // Set font family
            if (fontFamily == 0) {
                setTypeface(null, if (fontWeight == FontWeight.Bold) Typeface.BOLD else Typeface.NORMAL)
            } else {
                val typeface = ResourcesCompat.getFont(context, fontFamily)
                setTypeface(typeface, if (fontWeight == FontWeight.Bold) Typeface.BOLD else Typeface.NORMAL)
            }
            TextViewCompat.setLineHeight(this, fontLineHeight)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize) // Set the desired text size
        }
    }
}
