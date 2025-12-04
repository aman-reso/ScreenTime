@file:Suppress("ALL")

package com.telekom.odsystem.foundations

import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.TextViewCompat
import com.telekom.odsystem.toSp

data class ODSTextStyle(
    val fontFamily: Int,
    val fontSize: Int,
    val lineHeight: Int,
) {

    fun toTextStyle(): TextStyle {
        if (fontFamily == 0) {
            return TextStyle(
                fontSize = fontSize.sp,
                fontFamily = FontFamily.SansSerif,
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

        return TextStyle(
            fontSize = fontSize.sp,
            fontFamily = FontFamily(Font(fontFamily)),
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
        return fontSize.toFloat().toSp.toInt()
    }

    // Create a function to apply the ODSTextStyle to a TextView
    fun applyTextStyle(textView: TextView) {
        val fontSize = this.fontSize.toFloat()
        val fontLineHeight = this.lineHeight.toFloat().toSp.toInt() - fontSize.toInt()

        textView.apply {
            // Set font family
            if (fontFamily == 0) {
                setTypeface(null, Typeface.NORMAL) // Set to default typeface and normal style
            } else {
                val typeface = ResourcesCompat.getFont(context, fontFamily)
                setTypeface(typeface)
            }
            TextViewCompat.setLineHeight(this, fontLineHeight)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize) // Set the desired text size
        }
    }
}
