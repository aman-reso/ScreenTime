package com.app.screentime.ui.atom

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.app.screentime.R
import com.app.screentime.ui.theme.InterFontFamily
import com.app.screentime.ui.theme.LocalAppColors

enum class AppTextStyle {
    Body, Title, Footnote, Label, SubTitle, Caption
}

@Composable
fun AppText(
    modifier: Modifier = Modifier,
    text: String,
    style: AppTextStyle = AppTextStyle.Body,
    color: Color = LocalAppColors.current?.textPrimary ?: Color.Unspecified,
    textAlign: TextAlign? = null,
    fontSize: TextUnit? = null,
    lineHeight: TextUnit? = null,
    fontWeight: FontWeight? = null,
    baseFontFamily: FontFamily = InterFontFamily,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textDecoration: TextDecoration = TextDecoration.None
) {
    val textStyle = when (style) {
        AppTextStyle.Body -> TextStyle(
            fontSize = fontSize ?: 14.sp,
            lineHeight = lineHeight ?: 18.sp,
            fontWeight = fontWeight ?: FontWeight(500),
            fontFamily = baseFontFamily,
            color = color
        )

        AppTextStyle.Title -> TextStyle(
            fontSize = fontSize ?: 24.sp,
            lineHeight = lineHeight ?: 26.sp,
            fontWeight = fontWeight ?: FontWeight(700), // Bold = 700
            fontFamily = baseFontFamily,
            color = color
        )

        AppTextStyle.Footnote -> TextStyle(
            fontSize = fontSize ?: 8.sp,
            lineHeight = lineHeight ?: 12.sp,
            fontWeight = fontWeight ?: FontWeight(500), // Medium = 500
            fontFamily = baseFontFamily,
            color = color
        )

        AppTextStyle.Label -> TextStyle(
            fontSize = fontSize ?: 12.sp,
            lineHeight = lineHeight ?: 16.sp,
            fontWeight = fontWeight ?: FontWeight(500), // Medium = 500
            fontFamily = baseFontFamily,
            color = color
        )

        AppTextStyle.SubTitle -> TextStyle(
            fontSize = fontSize ?: 18.sp,
            lineHeight = lineHeight ?: 22.sp,
            fontWeight = fontWeight ?: FontWeight(700), // Medium = 500
            fontFamily = baseFontFamily,
            color = color
        )

        AppTextStyle.Caption -> TextStyle(
            fontSize = fontSize ?: 11.sp,
            lineHeight = lineHeight ?: 14.sp,
            fontWeight = fontWeight ?: FontWeight(500),
            fontFamily = baseFontFamily,
            color = color
        )
    }

    Text(
        maxLines = maxLines,
        text = text,
        textAlign = textAlign,
        style = textStyle,
        modifier = modifier, overflow = overflow, textDecoration = textDecoration
    )
}

