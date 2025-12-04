package com.telekom.odsystem.atoms.badgenumber

import android.content.Context
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSBadgeNumber composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSBadgeNumber(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSBadgeNumberProps = ODSBadgeNumberProps()
) {
    val style = ODSBadgeNumberStyle().getStyle(scheme = scheme, props = props)

    ODSColumn(
        modifier = modifier
            .minMaxSize(style = style)
            .applySemantics(props = props, context = LocalContext.current),
        background = if ((style.border
                ?: 0.dp) > 0.dp
        ) {
            style.borderColor
        } else {
            style.backgroundColor
        },
        cornerRadius = style.borderRadius,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        clipContent = style.clipContent ?: true,
    ) {
        ODSColumn(
            width = if (props.size == ODSBadgeNumberSize.SMALL) {
                style.minWidth?.minus((style.border ?: 0.dp).times(BORDER_TIMES))
            } else {
                Dp.Unspecified
            },
            height = if (props.size == ODSBadgeNumberSize.SMALL) {
                style.minHeight?.minus((style.border ?: 0.dp).times(BORDER_TIMES))
            } else {
                Dp.Unspecified
            },
            padding = style.padding,
            cornerRadius = style.borderRadius,
            verticalArrangement = style.verticalArrangement,
            verticalAlignment = style.verticalAlignment,
            horizontalAlignment = style.horizontalAlignment,
            background = style.backgroundColor,
        ) {
            if (props.size != ODSBadgeNumberSize.SMALL) {
                ODSText(
                    modifier = Modifier
                        .clearAndSetSemantics { },
                    text = props.notificationNumber ?: "",
                    textAlign = style.digitsTextAlign,
                    style = style.digitsTextStyle,
                    color = style.digitsColor
                )
            }
        }
    }
}

private fun Modifier.minMaxSize(
    style: ODSBadgeNumberStyle,
): Modifier {
    val internalModifier = Modifier
        .heightIn(
            min = style.minHeight ?: Dp.Unspecified,
            max = style.maxHeight ?: Dp.Unspecified
        )
        .widthIn(
            min = style.minWidth ?: Dp.Unspecified,
            max = style.maxWidth ?: Dp.Unspecified
        )
    return internalModifier.then(this)
}

private fun Modifier.applySemantics(
    props: ODSBadgeNumberProps,
    context: Context
): Modifier {
    return this.semantics {
        val notificationNumber = if (props.size != ODSBadgeNumberSize.SMALL) {
            props.notificationNumber ?: ""
        } else {
            ""
        }
        val notificationMessage =
            context.getString(R.string.semantic_notification_message, notificationNumber)
        this.contentDescription = notificationMessage
    }
}

private const val BORDER_TIMES = 2
