package com.telekom.odsystem.atoms.progressstepperitem

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSProgressStepperItem composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSProgressStepperItem(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSProgressStepperItemProps = ODSProgressStepperItemProps(),
) {
    val style = ODSProgressStepperItemStyle().getStyle(scheme = scheme, props = props)
    val context = LocalContext.current
    ODSColumn(
        modifier = modifier
            .sizeWithinBounds(
                minWidth = style.minWidth ?: Dp.Unspecified,
                minHeight = style.minHeight ?: Dp.Unspecified
            )
            .applySemantics(props = props, context = context),
        padding = style.padding,
        clipContent = style.clipContent != false,
        cornerRadius = style.cornerRadius,
        border = ODSBorder(width = style.border, colorList = style.borderColor),
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        background = style.background,
        width = style.width,
        height = style.height,
    ) {
        when (props.type) {
            ODSProgressStepperItemType.CURRENT, ODSProgressStepperItemType.NEXT -> {
                if (!props.number.isNullOrEmpty() && props.size == ODSProgressStepperItemSize.STANDARD) {
                    ODSText(
                        text = props.number,
                        style = style.digitsStyle,
                        color = style.digitsColor,
                        textAlign = style.digitsTextAlign
                    )
                }
            }

            ODSProgressStepperItemType.SUCCESS -> {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.checkmark_type_bold,
                        contentDescription = context.getString(R.string.semantic_success_alert)
                    ),
                    tint = style.checkmarkColor?.getColor(),
                    width = style.checkmarkWidth,
                    height = style.checkmarkHeight
                )
            }

            ODSProgressStepperItemType.ERROR -> {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.high_priority_email_type_standard,
                        contentDescription = context.getString(R.string.semantic_error_alert)
                    ),
                    tint = style.highPriorityColor?.getColor(),
                    width = style.highPriorityWidth,
                    height = style.highPriorityHeight
                )
            }
        }
    }
}

private fun Modifier.applySemantics(
    props: ODSProgressStepperItemProps,
    context: Context,
): Modifier {
    var contentDescription = context.getString(R.string.semantics_step)
    val stepAnnouncement = when (props.type) {
        ODSProgressStepperItemType.CURRENT, ODSProgressStepperItemType.NEXT ->
            if (!props.number.isNullOrEmpty() && props.size == ODSProgressStepperItemSize.STANDARD) {
                props.number
            } else {
                ""
            }

        ODSProgressStepperItemType.SUCCESS -> context.getString(R.string.semantic_success_alert)
        ODSProgressStepperItemType.ERROR -> context.getString(R.string.semantic_error_alert)
    } ?: ""
    if (stepAnnouncement.isNotEmpty()) {
        contentDescription += " $stepAnnouncement"
    }

    return this.semantics {
        this.contentDescription = contentDescription
    }
}
