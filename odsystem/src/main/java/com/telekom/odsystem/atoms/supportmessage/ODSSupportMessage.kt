package com.telekom.odsystem.atoms.supportmessage

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSSupportMessage composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSSupportMessage(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSupportMessageProps = ODSSupportMessageProps()
) {
    val style = ODSSupportMessageStyle().getStyle(scheme = scheme, props = props)
    ODSSupportMessageContainer(
        modifier = modifier,
        style = style,
        props = props
    )
}

@Composable
private fun ODSSupportMessageContainer(
    modifier: Modifier,
    style: ODSSupportMessageStyle,
    props: ODSSupportMessageProps
) {
    val context = LocalContext.current
    ODSRow(
        modifier = modifier.supportMessageSemantics(props = props, context = context),
        gap = style.gap,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        horizontalArrangement = style.horizontalArrangement
    ) {
        when (props.mode) {
            ODSSupportMessageMode.INFORMATIVE -> {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.information_type_bold,
                    ),
                    tint = style.informationColor?.getColor(),
                    width = style.informationWidth,
                    height = style.informationHeight
                )
            }

            ODSSupportMessageMode.ERROR -> {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.error_type_bold,
                    ),
                    tint = style.errorColor?.getColor(),
                    width = style.errorWidth,
                    height = style.errorHeight
                )
            }

            ODSSupportMessageMode.SUCCESS -> {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.success_type_bold,
                    ),
                    tint = style.successColor?.getColor(),
                    width = style.successWidth,
                    height = style.successHeight
                )
            }
        }

        ODSText(
            modifier = Modifier.clearAndSetSemantics { },
            text = props.helperText,
            style = style.labelTextStyle,
            color = style.labelColor,
            textAlign = style.labelTextAlign
        )
    }
}

fun Modifier.supportMessageSemantics(props: ODSSupportMessageProps, context: Context): Modifier {
    val mode = when (props.mode) {
        ODSSupportMessageMode.INFORMATIVE -> context.getString(R.string.semantic_info_sentence)
        ODSSupportMessageMode.ERROR -> context.getString(R.string.semantic_error_sentence)
        ODSSupportMessageMode.SUCCESS -> context.getString(R.string.semantic_success_sentence)
    }
    val fullContentDescription = mode + props.helperText
    return this.semantics {
        this.contentDescription = fullContentDescription
    }
}
