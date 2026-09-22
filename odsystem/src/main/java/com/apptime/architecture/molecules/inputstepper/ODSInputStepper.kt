package com.telekom.odsystem.molecules.inputstepper

import android.content.Context
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.inputstepperbutton.ODSInputStepperButton
import com.telekom.odsystem.atoms.inputstepperbutton.ODSInputStepperButtonProps
import com.telekom.odsystem.atoms.inputstepperbutton.ODSInputStepperButtonSize
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * A composable function that displays an ODS Input Stepper.
 *
 * The Input Stepper allows users to increment or decrement a numerical value within a defined range.
 * It can also optionally display a remove icon.
 *
 * @param modifier Optional [Modifier] for theming and custom styling.
 * @param scheme The [ODSTheme] to apply, defaults to [neutralScheme].
 * @param props Visual configuration for the component.
 * @param onDecrement Optional callback invoked when the decrement button is pressed.
 * @param onIncrement Optional callback invoked when the increment button is pressed.
 * @param onRemove Optional callback invoked when the remove button (if shown) is pressed.
 */
@Composable
fun ODSInputStepper(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSInputStepperProps = ODSInputStepperProps(),
    onDecrement: (() -> Unit)? = null,
    onIncrement: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
) {
    val style = ODSInputStepperStyle().getStyle(scheme = scheme, props = props)
    val min = props.minValue?.toIntOrNull()
    val parsed = props.value?.toIntOrNull() ?: min ?: 0
    ODSInputStepperContainer(
        modifier = modifier,
        scheme = scheme,
        style = style,
        props = props,
        currentValue = parsed,
        onDecrement = onDecrement,
        onIncrement = onIncrement,
        onRemove = onRemove
    )
}

@Composable
private fun ODSInputStepperContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSInputStepperStyle,
    props: ODSInputStepperProps,
    currentValue: Int,
    onDecrement: (() -> Unit)?,
    onIncrement: (() -> Unit)?,
    onRemove: (() -> Unit)?,
) {
    ODSBox(
        modifier = modifier.width(IntrinsicSize.Max), // Custom addition,
        contentAlignment = style.contentAlignment
    ) {
        if (props.type != ODSInputStepperType.GHOST || props.disabled || props.readOnly) {
            ODSInputStepperBackgroundContainer(style = style)
        }
        ODSColumn(
            verticalArrangement = style.verticalArrangement,
            verticalAlignment = style.verticalAlignment,
            horizontalAlignment = style.horizontalAlignment
        ) {
            ODSInputStepperContentContainer(
                style = style,
                props = props,
                scheme = scheme,
                currentValue = currentValue,
                onDecrement = onDecrement,
                onIncrement = onIncrement,
                onRemove = onRemove
            )
        }
    }
}

@Composable
private fun BoxScope.ODSInputStepperBackgroundContainer(
    style: ODSInputStepperStyle,
) {
    ODSBox(
        modifier = Modifier
            .matchParentSize()
            .padding(
                start = style.backgroundPadding?.left ?: Dp.Unspecified,
                top = style.backgroundPadding?.top ?: Dp.Unspecified,
                end = style.backgroundPadding?.right ?: Dp.Unspecified,
                bottom = style.backgroundPadding?.bottom ?: Dp.Unspecified
            ),
        cornerRadius = style.backgroundBorderRadius,
        clipContent = style.backgroundClipContent != false,
        border = ODSBorder(
            width = style.backgroundBorder,
            colorList = style.backgroundBorderColor
        ),
        background = style.backgroundBackgroundColor,
    ) { }
}

@Suppress("LongMethod")
@Composable
private fun ODSInputStepperContentContainer(
    style: ODSInputStepperStyle,
    props: ODSInputStepperProps,
    scheme: ODSTheme = neutralScheme,
    currentValue: Int,
    onDecrement: (() -> Unit)? = null,
    onIncrement: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        padding = style.contentPadding,
        cornerRadius = style.contentBorderRadius,
        horizontalArrangement = style.contentHorizontalArrangement,
        verticalAlignment = style.contentVerticalAlignment,
    ) {
        ODSInputStepperButton(
            scheme = scheme,
            props = ODSInputStepperButtonProps(
                buttonIcon = getLeftInputStepperButtonIcon(
                    props = props,
                    context = context
                ),
                size = getInputStepperButtonSize(
                    size = props.size
                ),
                disabled = isDecrementDisabled(
                    props = props,
                    currentValue = currentValue
                )
            )
        ) {
            if (props.showRemoveIcon) {
                onRemove?.invoke()
            } else {
                onDecrement?.invoke()
            }
        }
        if (!props.value.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.sizeWithinBounds(
                    minWidth = style.valueMinWidth ?: Dp.Unspecified
                ),
                text = props.value,
                style = style.valueTextStyle,
                color = style.valueColor,
                textAlign = style.valueTextAlign,
                overflow = style.valueTextOverflow
            )
        }
        ODSInputStepperButton(
            scheme = scheme,
            props = ODSInputStepperButtonProps(
                buttonIcon = ODSIconModel(
                    drawableRes = R.drawable.add_type_standard,
                    contentDescription = context.getString(R.string.semantic_increase_icon)
                ),
                size = getInputStepperButtonSize(
                    size = props.size
                ),
                disabled = isIncrementDisabled(
                    props = props,
                    currentValue = currentValue
                )
            )
        ) {
            onIncrement?.invoke()
        }
    }
}

private fun getLeftInputStepperButtonIcon(
    props: ODSInputStepperProps,
    context: Context
): ODSIconModel {
    return if (props.showRemoveIcon) {
        ODSIconModel(
            drawableRes = R.drawable.remove_type_standard,
            contentDescription = context.getString(R.string.semantic_remove_icon)
        )
    } else {
        ODSIconModel(
            drawableRes = R.drawable.minus_type_standard,
            contentDescription = context.getString(R.string.semantic_decrease_icon)
        )
    }
}

private fun getInputStepperButtonSize(
    size: ODSInputStepperSize
): ODSInputStepperButtonSize {
    return when (size) {
        ODSInputStepperSize.SMALL -> ODSInputStepperButtonSize.SMALL
        ODSInputStepperSize.LARGE -> ODSInputStepperButtonSize.LARGE
    }
}

private fun isIncrementDisabled(props: ODSInputStepperProps, currentValue: Int): Boolean {
    val max = props.maxValue?.toIntOrNull()
    return (max != null && currentValue >= max) || props.disabled || props.readOnly
}

private fun isDecrementDisabled(props: ODSInputStepperProps, currentValue: Int): Boolean {
    val min = props.minValue?.toIntOrNull()
    return ((min != null && currentValue <= min) || props.disabled || props.readOnly) && !props.showRemoveIcon
}
