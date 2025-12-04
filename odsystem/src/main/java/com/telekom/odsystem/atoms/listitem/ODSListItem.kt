package com.telekom.odsystem.atoms.listitem

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.foundations.underline
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSListItem composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 */
@Suppress("LongMethod")
@Composable
fun ODSListItem(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSListItemProps = ODSListItemProps(),
    onClick: (() -> Unit)? = null
) {
    val style = ODSListItemStyle().getStyle(scheme = scheme, props = props)
    ODSRow(
        modifier = modifier
            .applySemantics(props = props, context = LocalContext.current)
            .sizeWithinBounds(
                minHeight = style.minHeight ?: Dp.Unspecified
            )
            .applyClickableIfLink(
                props = props,
                onClick = onClick
            ),
        gap = style.gap,
        padding = style.padding,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement
    ) {
        ODSRow(
            modifier = Modifier.sizeWithinBounds(
                minWidth = style.markerMinWidth ?: Dp.Unspecified
            ),
            width = style.markerWidth,
            height = style.markerHeight,
            horizontalAlignment = style.markerHorizontalAlignment,
            verticalAlignment = style.markerVerticalAlignment,
            horizontalArrangement = style.markerHorizontalArrangement
        ) {

            when (props.variant) {
                ODSListItemVariant.NUMBER -> {
                    ODSText(
                        text = props.number ?: "",
                        style = style.marker2TextStyle,
                        color = style.marker2Color,
                        textAlign = style.marker2TextAlign ?: TextAlign.Start
                    )
                }

                ODSListItemVariant.BULLETPOINT -> {
                    ODSBox(
                        cornerRadius = style.innerCircleBorderRadius,
                        background = style.innerCircleBackgroundColor,
                        width = style.innerCircleWidth,
                        height = style.innerCircleHeight
                    ) {
                    }
                }

                ODSListItemVariant.ICON -> {
                    ODSIcon(
                        iconModel = props.icon,
                        tint = style.iconColor?.getColor(),
                        width = style.iconWidth,
                        height = style.iconHeight
                    )
                }

                ODSListItemVariant.OUTLINE_BULLET -> {
                    ODSBox(
                        cornerRadius = style.innerCircleBorderRadius,
                        border = ODSBorder(
                            width = style.innerCircleBorder ?: 0.dp,
                            colorList = style.innerCircleBorderColor
                        ),
                        background = style.innerCircleBackgroundColor,
                        width = style.innerCircleWidth,
                        height = style.innerCircleHeight
                    ) {
                    }
                }
            }
        }
        if (!props.text.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier
                    .applyUnderlineIfLink(props = props, style = style),
                text = props.text,
                style = style.labelTextStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign ?: TextAlign.Start
            )
        }
    }
}

@Composable
private fun Modifier.applyClickableIfLink(
    props: ODSListItemProps,
    onClick: (() -> Unit)?
) = if (props.link) {
    customClickable(
        isPressed = {},
        onClick = onClick
    )
} else {
    this
}

@Composable
private fun Modifier.applyUnderlineIfLink(
    props: ODSListItemProps,
    style: ODSListItemStyle,
) = if (props.link) {
    underline(
        thickness = style.underlineThickness ?: 0.dp,
        color = style.labelColor?.getColor() ?: Color.Transparent
    )
} else {
    this
}

private fun Modifier.applySemantics(props: ODSListItemProps, context: Context): Modifier {
    return this.clearAndSetSemantics {

        val prefixText = when (props.variant) {
            ODSListItemVariant.ICON -> {
                props.icon?.contentDescription ?: context.getString(R.string.semantic_icon)
            }

            ODSListItemVariant.NUMBER -> {
                context.getString(R.string.semantic_number) + props.number
            }

            else -> ""
        }
        val listItemLabel =
            if (props.link) context.getString(R.string.semantic_link) else context.getString(R.string.semantic_list_item)
        this.contentDescription =
            "$prefixText ${props.text ?: ""} $listItemLabel"
    }
}
