package com.telekom.odsystem.atoms.inputitem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
/**
 * ODSInputItem composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
fun ODSInputItem(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSInputItemProps = ODSInputItemProps(),
) {
    val style = ODSInputItemStyle().getStyle(scheme = scheme, props = props)

    ODSRow(
        modifier = modifier
            .sizeWithinBounds(minHeight = MIN_HEIGHT.dp, minWidth = INPUT_ITEM_MIN_WIDTH.dp),
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement,
        cornerRadius = style.borderRadius,
        border = ODSBorder(width = style.border, colorList = style.borderColor),
        background = style.backgroundColor,
        width = style.width,
        height = style.height
    ) {
        if (props.masked && !props.inputText.isNullOrEmpty()) {
            ODSBox(
                cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
                background = style.dotColor,
                width = style.dotWidth,
                height = style.dotHeight
            ) {
            }
        } else if (!props.placeholder.isNullOrEmpty() && props.inputText.isNullOrEmpty()) {
            ODSText(
                text = props.placeholder,
                style = style.placeholderTextStyle,
                color = style.placeholderColor,
                textAlign = style.placeholderTextAlign
            )
        } else if (!props.inputText.isNullOrEmpty()) {
            ODSText(
                text = props.inputText,
                style = style.inputValueTextStyle,
                color = style.inputValueColor,
                textAlign = style.inputValueTextAlign
            )
        }
    }
}

private const val INPUT_ITEM_MIN_WIDTH = 44
