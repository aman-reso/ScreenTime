package com.telekom.odsystem.atoms.badgeicon

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSBadgeIcon composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSBadgeIcon(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSBadgeIconProps = ODSBadgeIconProps()
) {
    val style = ODSBadgeIconStyle().getStyle(scheme = scheme, props = props)

    ODSColumn(
        modifier = modifier.applySemantics(props = props, context = LocalContext.current),
        width = style.width,
        height = style.height,
        background = if ((style.border ?: 0.dp) > 0.dp) {
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
            width = style.width?.minus((style.border ?: 0.dp).times(BORDER_TIMES)),
            height = style.height?.minus((style.border ?: 0.dp).times(BORDER_TIMES)),
            padding = style.padding,
            cornerRadius = style.borderRadius,
            verticalArrangement = style.verticalArrangement,
            verticalAlignment = style.verticalAlignment,
            horizontalAlignment = style.horizontalAlignment,
            background = style.backgroundColor,
        ) {
            if (props.size != ODSBadgeIconSize.SMALL) {
                when (props.mode) {
                    ODSBadgeIconMode.ERROR -> {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.error_type_standard,
                            ),
                            width = style.errorWidth,
                            height = style.errorHeight,
                            tint = style.errorColor?.getColor()
                        )
                    }

                    else -> {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.checkmark_type_bold,
                            ),
                            width = style.checkmarkWidth,
                            height = style.checkmarkHeight,
                            tint = style.checkmarkColor?.getColor()
                        )
                    }
                }
            }
        }
    }
}

private fun Modifier.applySemantics(
    props: ODSBadgeIconProps,
    context: Context
): Modifier {
    val contentDescription = if (props.mode == ODSBadgeIconMode.ERROR) {
        context.getString(R.string.semantic_error, "")
    } else {
        context.getString(R.string.semantic_success, "")
    }
    return this.semantics {
        this.contentDescription = contentDescription
    }
}

private const val BORDER_TIMES = 2
