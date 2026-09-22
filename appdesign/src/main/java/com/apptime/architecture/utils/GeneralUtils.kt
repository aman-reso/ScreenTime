package com.telekom.odsystem.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.foundations.REQUIRED

/**
 * Created by dmarinopoulos on 3/10/25
 */

/**
 *  Builds an [AnnotatedString] for a label with an optional required indicator.
 *  Affects SDK's input components.
 *  Ex: ODS Text Field, ODS Text Area, ODS Date Picker Input Field.
 *
 *  @param label The label text to display.
 *  @param isRequired Boolean indicating if the field is required.
 *  @param labelStyle The [ODSTextStyle] to apply to the label text
 *  @param labelTextAlign The [TextAlign] to apply to the label text.
 *  @param requiredStyle The [ODSTextStyle] to apply to the required indicator text.
 *  @param requiredTextAlign The [TextAlign] to apply to the required indicator text.
 *  @param requiredColor The [HexColor] to apply to the required indicator text.
 *  @return An [AnnotatedString] combining the label and required indicator with their respective styles
 */
fun buildLabelAnnotatedString(
    label: String?,
    isRequired: Boolean,
    labelStyle: TextStyle,
    labelTextAlign: TextAlign,
    labelColor: Color,
    requiredStyle: TextStyle,
    requiredTextAlign: TextAlign,
    requiredColor: Color,
): AnnotatedString {
    return buildAnnotatedString {
        if (!label.isNullOrEmpty()) {
            withStyle(
                style = labelStyle.copy(
                    color = labelColor,
                    textAlign = labelTextAlign
                ).toSpanStyle()
            ) {
                append(label)
            }
            if (isRequired) {
                withStyle(
                    style = requiredStyle
                        .copy(
                            color = requiredColor,
                            textAlign = requiredTextAlign
                        ).toSpanStyle()
                ) {
                    append(" ")
                    append(REQUIRED)
                }
            }
        }
    }
}
