package com.telekom.odsystem.atoms.sliderinputfieldgroup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.sliderinputfield.ODSSliderInputField
import com.telekom.odsystem.atoms.sliderinputfield.ODSSliderInputFieldProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSSliderInputFieldGroup composable.
 *
 * @param modifier Modifier for the component.
 * @param scheme Color scheme.
 * @param props Visual configuration.
 * @param onMinValueSubmit Callback for min value input submission.
 * @param onMinValueFocusChange Callback for min value input focus change.
 * @param onMaxValueSubmit Callback for max value input submission.
 * @param onMaxValueFocusChange Callback for max value input focus change.
 * @param maxValueFocusRequester FocusRequester for max value input.
 * @param onMaxValueChange Callback for max value input change.
 * @param minValueFocusRequester FocusRequester for min value input.
 * @param onMinValueChange Callback for min value input change.
 */
@Suppress("LongMethod")
@Composable
fun ODSSliderInputFieldGroup(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSliderInputFieldGroupProps = ODSSliderInputFieldGroupProps(),
    onMinValueSubmit: () -> Unit = {},
    onMinValueFocusChange: (FocusState) -> Unit = {},
    onMaxValueSubmit: () -> Unit = {},
    onMaxValueFocusChange: (FocusState) -> Unit = {},
    maxValueFocusRequester: FocusRequester = remember { FocusRequester() },
    onMaxValueChange: (String) -> Unit = { },
    minValueFocusRequester: FocusRequester = remember { FocusRequester() },
    onMinValueChange: (String) -> Unit = { },
) {

    val style = ODSSliderInputFieldGroupStyle().getStyle(scheme = scheme, props = props)
    when (props.variant) {
        ODSSliderInputFieldGroupVariant.SINGLE -> {
            ODSRow(
                gap = style.gap,
                horizontalArrangement = style.horizontalArrangement,
                horizontalAlignment = style.horizontalAlignment,
                verticalAlignment = style.verticalAlignment,
                modifier = modifier
            ) {
                ODSRow(
                    gap = style.labelMaxGap,
                    horizontalArrangement = style.labelMaxHorizontalArrangement,
                    verticalAlignment = style.labelMaxVerticalAlignment,
                    horizontalAlignment = style.labelMaxHorizontalAlignment
                ) {
                    SliderInputFieldLabel(
                        modifier = Modifier.weight(1f),
                        style = style,
                        label = props.labelMax
                    )
                    SliderInputField(
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(maxValueFocusRequester)
                            .onFocusChanged(onMaxValueFocusChange),
                        scheme = scheme,
                        props = props.sliderInputMaxProps ?: ODSSliderInputFieldProps(),
                        onValueSubmit = onMaxValueSubmit,
                        onValueChange = onMaxValueChange,
                    )
                }
            }
        }

        ODSSliderInputFieldGroupVariant.STACKED -> {
            ODSColumn(
                modifier = modifier,
                gap = style.gap,
                verticalArrangement = style.verticalArrangement,
                horizontalAlignment = style.horizontalAlignment,
                verticalAlignment = style.verticalAlignment,
            ) {
                SliderInputFieldMinLabelContainer(
                    scheme = scheme,
                    props = props,
                    style = style,
                    minValueFocusRequester = minValueFocusRequester,
                    onMinValueSubmit = onMinValueSubmit,
                    onMinFocusChange = onMinValueFocusChange,
                    onMinValueChange = onMinValueChange,
                )

                SliderInputFieldMaxWithLabelContainer(
                    scheme = scheme,
                    props = props,
                    style = style,
                    onMaxValueSubmit = onMaxValueSubmit,
                    onMaxFocusChange = onMaxValueFocusChange,
                    maxValueFocusRequester = maxValueFocusRequester,
                    onMaxValueChange = onMaxValueChange,
                )
            }
        }

        ODSSliderInputFieldGroupVariant.SIDE_BY_SIDE -> {
            ODSRow(
                gap = style.gap,
                horizontalArrangement = style.horizontalArrangement,
                horizontalAlignment = style.horizontalAlignment,
                verticalAlignment = style.verticalAlignment,
                modifier = modifier
            ) {
                SliderInputFieldMinLabelContainer(
                    modifier = Modifier
                        .weight(1f)
                        .semantics { isTraversalGroup = true },
                    scheme = scheme,
                    props = props,
                    style = style,
                    minValueFocusRequester = minValueFocusRequester,
                    onMinValueSubmit = onMinValueSubmit,
                    onMinFocusChange = onMinValueFocusChange,
                    onMinValueChange = onMinValueChange,
                )

                SliderInputFieldMaxWithLabelContainer(
                    modifier = Modifier
                        .weight(1f)
                        .semantics { isTraversalGroup = true },
                    scheme = scheme,
                    props = props,
                    style = style,
                    onMaxValueSubmit = onMaxValueSubmit,
                    onMaxFocusChange = onMaxValueFocusChange,
                    maxValueFocusRequester = maxValueFocusRequester,
                    onMaxValueChange = onMaxValueChange,
                )
            }
        }
    }
}

@Composable
private fun SliderInputFieldLabel(
    modifier: Modifier = Modifier,
    style: ODSSliderInputFieldGroupStyle,
    label: String?
) {
    ODSRow(
        modifier = modifier,
        horizontalArrangement = style.labelHorizontalArrangement,
        horizontalAlignment = style.labelHorizontalAlignment,
        verticalAlignment = style.labelVerticalAlignment
    ) {
        if (!label.isNullOrEmpty()) {
            ODSText(
                text = label,
                style = style.labelTextStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign,
                overflow = style.labelTextOverflow
            )
        }
    }
}

@Composable
private fun SliderInputFieldMaxWithLabelContainer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    props: ODSSliderInputFieldGroupProps,
    style: ODSSliderInputFieldGroupStyle,
    maxValueFocusRequester: FocusRequester,
    onMaxFocusChange: (FocusState) -> Unit,
    onMaxValueChange: (String) -> Unit,
    onMaxValueSubmit: () -> Unit,
) {
    ODSColumn(
        modifier = modifier,
        gap = style.labelMaxGap,
        verticalArrangement = style.labelMaxVerticalArrangement,
        verticalAlignment = style.labelMaxVerticalAlignment,
        horizontalAlignment = style.labelMaxHorizontalAlignment
    ) {
        SliderInputFieldLabel(style = style, label = props.labelMax)
        SliderInputField(
            modifier = Modifier
                .focusRequester(maxValueFocusRequester)
                .onFocusChanged(onMaxFocusChange),
            scheme = scheme,
            props = props.sliderInputMaxProps ?: ODSSliderInputFieldProps(),
            onValueSubmit = onMaxValueSubmit,
            onValueChange = onMaxValueChange
        )
    }
}

@Composable
private fun SliderInputFieldMinLabelContainer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    props: ODSSliderInputFieldGroupProps,
    style: ODSSliderInputFieldGroupStyle,
    minValueFocusRequester: FocusRequester,
    onMinFocusChange: (FocusState) -> Unit,
    onMinValueChange: (String) -> Unit,
    onMinValueSubmit: () -> Unit
) {
    ODSColumn(
        modifier = modifier,
        gap = style.labelMinGap,
        verticalArrangement = style.labelMinVerticalArrangement,
        verticalAlignment = style.labelMinVerticalAlignment,
        horizontalAlignment = style.labelMinHorizontalAlignment
    ) {
        SliderInputFieldLabel(style = style, label = props.labelMin)
        SliderInputField(
            modifier = Modifier
                .focusRequester(minValueFocusRequester)
                .onFocusChanged(onMinFocusChange),
            scheme = scheme,
            props = props.sliderInputMinProps ?: ODSSliderInputFieldProps(),
            onValueChange = onMinValueChange,
            onValueSubmit = onMinValueSubmit
        )
    }
}

@Composable
private fun SliderInputField(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    props: ODSSliderInputFieldProps,
    onValueChange: (String) -> Unit,
    onValueSubmit: () -> Unit,
) {
    ODSSliderInputField(
        modifier = modifier,
        scheme = scheme,
        props = props,
        onValueChange = onValueChange,
        onValueSubmit = onValueSubmit,
    )
}
