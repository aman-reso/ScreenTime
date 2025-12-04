package com.telekom.odsystem.atoms.resultitem

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ODSResultItem(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSResultItemProps = ODSResultItemProps(),
    onItemClick: () -> Unit,
) {
    var pressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val style = ODSResultItemStyle().getStyle(
        scheme = scheme,
        props = props,
        if (pressed) ODSActions.PRESSED else if (isHovered) ODSActions.HOVERED else ODSActions.DEFAULT
    )

    ODSBox(
        contentAlignment = style.contentAlignment
    ) {
        if (isHovered || pressed) {
            ODSColumn(
                cornerRadius = style.backgroundBorderRadius,
                clipContent = style.backgroundClipContent ?: true,
                verticalArrangement = style.backgroundVerticalArrangement,
                verticalAlignment = style.backgroundVerticalAlignment,
                horizontalAlignment = style.backgroundHorizontalAlignment,
                background = style.backgroundBackgroundColor,
                modifier = Modifier
                    .matchParentSize()
            ) {}
        }

        ODSRow(
            gap = style.gap,
            cornerRadius = style.borderRadius,
            border = ODSBorder(width = style.border, colorList = style.borderColor),
            horizontalArrangement = style.horizontalArrangement,
            horizontalAlignment = style.horizontalAlignment,
            verticalAlignment = style.verticalAlignment,
            modifier = modifier
                .sizeWithinBounds(minHeight = style.minHeight ?: Dp.Unspecified)
                .fillMaxWidth()
                .customClickable(
                    isPressed = { pressed = it },
                    onClick = onItemClick,
                    interactionSource = interactionSource,
                )
        ) {
            if (props.icon != null) {
                ODSIconContainer(style = style, props = props)
            }

            ODSRow(
                gap = style.labelContainerGap,
                horizontalArrangement = style.labelContainerHorizontalArrangement,
                horizontalAlignment = style.labelContainerHorizontalAlignment,
                verticalAlignment = style.labelContainerVerticalAlignment,
                modifier = Modifier.weight(1f)
            ) {
                ODSLabelContainer(style = style, props = props)
            }
        }
    }
}

@Composable
private fun ODSIconContainer(style: ODSResultItemStyle, props: ODSResultItemProps) {
    ODSRow(
        cornerRadius = style.iconContainerBorderRadius,
        horizontalArrangement = style.iconContainerHorizontalArrangement,
        horizontalAlignment = style.iconContainerHorizontalAlignment,
        verticalAlignment = style.iconContainerVerticalAlignment,
        width = style.iconContainerWidth,
        height = style.iconContainerHeight
    ) {
        val iconWidth = if (props.fragMagenta) style.odsAiIconWidth else style.iconWidth
        val iconHeight = if (props.fragMagenta) style.odsAiIconHeight else style.iconHeight

        ODSIcon(
            alignment = style.odsAiIconContentAlignment,
            iconModel = props.icon,
            tint = style.iconColor?.getColor(),
            width = iconWidth,
            height = iconHeight
        )
    }
}

@Composable
private fun ODSLabelContainer(style: ODSResultItemStyle, props: ODSResultItemProps) {
    if (!props.fragMagenta) {
        val hasRecessiveText = !props.recessiveLabelText.isNullOrEmpty()
        val hasLabelText = !props.labelText.isNullOrEmpty()

        if (hasRecessiveText || hasLabelText) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString(
                    recessiveLabelText = props.recessiveLabelText,
                    labelText = props.labelText,
                    recessiveTextStyle = style.textRecessiveTextStyle?.toTextStyle() ?: TextStyle(),
                    recessiveColor = style.textRecessiveColor?.getColor() ?: Color.Unspecified,
                    recessiveTextAlign = style.textRecessiveTextAlign ?: TextAlign.Start,
                    primaryTextStyle = style.textPrimaryTextStyle?.toTextStyle() ?: TextStyle(),
                    primaryColor = style.textPrimaryColor?.getColor() ?: Color.Unspecified,
                    primaryTextAlign = style.textPrimaryTextAlign ?: TextAlign.Start
                )
            )
        }
    } else if (!props.fragMagentaPrompt.isNullOrEmpty()) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = props.fragMagentaPrompt,
            style = style.promptTextStyle,
            color = style.promptColor,
            textAlign = style.promptTextAlign,
            overflow = style.promptTextOverflow
        )
    }
}

private fun buildAnnotatedString(
    recessiveLabelText: String?,
    labelText: String?,
    recessiveTextStyle: TextStyle,
    recessiveColor: Color,
    recessiveTextAlign: TextAlign,
    primaryTextStyle: TextStyle,
    primaryColor: Color,
    primaryTextAlign: TextAlign,
) = buildAnnotatedString {
    val hasRecessiveText = !recessiveLabelText.isNullOrEmpty()
    val hasLabelText = !labelText.isNullOrEmpty()

    if (hasRecessiveText) {
        withStyle(
            style = recessiveTextStyle.copy(
                color = recessiveColor,
                textAlign = recessiveTextAlign
            ).toSpanStyle()
        ) {
            append(recessiveLabelText)
        }
        if (hasLabelText) {
            append(" ")
        }
    }
    if (hasLabelText) {
        withStyle(
            style = primaryTextStyle.copy(
                color = primaryColor,
                textAlign = primaryTextAlign
            ).toSpanStyle()
        ) {
            append(labelText)
        }
    }
}
