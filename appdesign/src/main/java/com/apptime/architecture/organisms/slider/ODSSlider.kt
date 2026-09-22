package com.telekom.odsystem.organisms.slider

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.sliderinputfieldgroup.ODSSliderInputFieldGroup
import com.telekom.odsystem.atoms.sliderinputfieldgroup.ODSSliderInputFieldGroupVariant
import com.telekom.odsystem.atoms.sliderthumb.ODSSliderThumb
import com.telekom.odsystem.extensions.upToDecimals
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.util.Locale

/**
 * ODSSlider composable.
 *
 * @param modifier Modifier for the component.
 * @param scheme Color scheme.
 * @param props Visual configuration.
 * @param onMinValueSubmit Callback for min value input submission.
 * @param onMinValueFocusChange Callback for min value input focus change.
 * @param onMaxValueSubmit Callback for max value input submission.
 * @param onMaxValueFocusChange Callback for max value input focus change.
 * @param maxValueFocusRequester FocusRequester for max value input.
 * @param onMaxValueChange Callback for max value input text change.
 * @param minValueFocusRequester FocusRequester for min value input.
 * @param onMinValueChange Callback for min value input text change.
 * @param onSliderValueChange Callback for single slider value change.
 * @param onRangeSliderValueChange Callback for range slider value change.
 */
@Suppress("LongMethod")
@Composable
fun ODSSlider(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSliderProps = ODSSliderProps(),
    onMinValueSubmit: () -> Unit = {},
    onMinValueFocusChange: (FocusState) -> Unit = {},
    onMaxValueSubmit: () -> Unit = {},
    onMaxValueFocusChange: (FocusState) -> Unit = {},
    maxValueFocusRequester: FocusRequester = remember { FocusRequester() },
    onMaxValueChange: (String) -> Unit = { },
    minValueFocusRequester: FocusRequester = remember { FocusRequester() },
    onMinValueChange: (String) -> Unit = { },
    onSliderValueChange: (Float) -> Unit = { },
    onRangeSliderValueChange: (ClosedFloatingPointRange<Float>) -> Unit = { }
) {

    val style = ODSSliderStyle().getStyle(scheme = scheme, props = props)

    ODSColumn(
        modifier = modifier,
        gap = style.gap,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement
    ) {
        ODSColumn(
            gap = style.sliderContainerGap?.minus(SLIDER_DEFAULT_VERTICAL_PADDING),
            verticalAlignment = style.sliderContainerVerticalAlignment,
            horizontalAlignment = style.sliderContainerHorizontalAlignment,
            verticalArrangement = style.sliderContainerVerticalArrangement
        ) {
            ODSSliderContainer(
                scheme = scheme,
                props = props,
                style = style,
                onMinValueSubmit = onMinValueSubmit,
                onMinValueFocusChange = onMinValueFocusChange,
                onMaxValueSubmit = onMaxValueSubmit,
                onMaxValueFocusChange = onMaxValueFocusChange,
                onSliderValueChange = onSliderValueChange,
                onRangeSliderValueChange = onRangeSliderValueChange,
                maxValueFocusRequester = maxValueFocusRequester,
                onMaxValueChange = onMaxValueChange,
                minValueFocusRequester = minValueFocusRequester,
                onMinValueChange = onMinValueChange
            )
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun ODSSliderContainer(
    scheme: ODSTheme,
    props: ODSSliderProps,
    style: ODSSliderStyle,
    onMinValueSubmit: () -> Unit = {},
    onMinValueFocusChange: (FocusState) -> Unit = {},
    onMaxValueSubmit: () -> Unit = {},
    onMaxValueFocusChange: (FocusState) -> Unit = {},
    onSliderValueChange: (Float) -> Unit = {},
    onRangeSliderValueChange: (ClosedFloatingPointRange<Float>) -> Unit = {},
    maxValueFocusRequester: FocusRequester = remember { FocusRequester() },
    onMaxValueChange: (String) -> Unit = { },
    minValueFocusRequester: FocusRequester = remember { FocusRequester() },
    onMinValueChange: (String) -> Unit = { },
) {
    val context = LocalContext.current
    ODSSliderInputFieldGroup(
        scheme = scheme,
        props = props.inputFieldGroupProps,
        onMinValueSubmit = onMinValueSubmit,
        onMinValueFocusChange = onMinValueFocusChange,
        onMaxValueSubmit = onMaxValueSubmit,
        onMaxValueFocusChange = onMaxValueFocusChange,
        maxValueFocusRequester = maxValueFocusRequester,
        onMaxValueChange = onMaxValueChange,
        minValueFocusRequester = minValueFocusRequester,
        onMinValueChange = onMinValueChange
    )
    if (props.inputFieldGroupProps.variant == ODSSliderInputFieldGroupVariant.SINGLE) {
        ODSSingleTrackAndLabelContainer(
            modifier = Modifier.applySemanticsForSlider(props = props, context = context),
            scheme = scheme,
            props = props,
            style = style,
            onSliderValueChange = onSliderValueChange,
        )
    } else {
        ODSRangeTrackAndLabelContainer(
            modifier = Modifier.applySemanticsForSlider(props = props, context = context),
            scheme = scheme,
            props = props,
            style = style,
            onRangeSliderValueChange = onRangeSliderValueChange,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ODSSingleTrackAndLabelContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    props: ODSSliderProps,
    style: ODSSliderStyle,
    onSliderValueChange: (Float) -> Unit = {},
    onValueChangeFinished: () -> Unit = {},
) {
    ODSColumn(
        modifier = modifier,
        gap = style.trackLabelsGap?.minus(SLIDER_DEFAULT_VERTICAL_PADDING),
        verticalArrangement = style.trackLabelsVerticalArrangement,
        verticalAlignment = style.trackLabelsVerticalAlignment,
        horizontalAlignment = style.trackLabelsHorizontalAlignment
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
            Slider(
                modifier = Modifier
                    .clearAndSetSemantics { }
                    .padding(0.dp)
                    .fillMaxWidth(),
                value = props.sliderInputMaxInputValue,
                valueRange = props.sliderValueRange,
                onValueChange = {
                    val fractionValue = it.upToDecimals(props.fractionDecimal ?: 0)
                    onSliderValueChange(fractionValue)
                    onValueChangeFinished()
                },
                track = { state ->
                    SliderDefaults.Track(
                        modifier = Modifier
                            .height(style.trackContainerHeight ?: MIN_HEIGHT.dp)
                            .fillMaxWidth(),
                        sliderState = state,
                        thumbTrackGapSize = 0.dp,
                        trackInsideCornerSize = 0.dp,
                        colors = getTrackColors(style)
                    )
                },
                thumb = {
                    ODSSliderThumb(
                        modifier = Modifier.focusProperties { canFocus = false },
                        scheme = scheme
                    )
                }
            )
        }
        ODSSliderLabelContainer(
            minValue = getInputText(props.min ?: 0f, props),
            maxValue = getInputText(props.max ?: DEFAULT_MAX_TRACK_VALUE, props),
            style = style
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
private fun ODSRangeTrackAndLabelContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    props: ODSSliderProps,
    style: ODSSliderStyle,
    onRangeSliderValueChange: (ClosedFloatingPointRange<Float>) -> Unit = {},
    onValueChangeFinished: () -> Unit = {},
) {

    ODSColumn(
        modifier = modifier,
        gap = style.trackLabelsGap?.minus(SLIDER_DEFAULT_VERTICAL_PADDING),
        verticalArrangement = style.trackLabelsVerticalArrangement,
        verticalAlignment = style.trackLabelsVerticalAlignment,
        horizontalAlignment = style.trackLabelsHorizontalAlignment
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
            RangeSlider(
                modifier = Modifier
                    .clearAndSetSemantics { }
                    .padding(0.dp)
                    .fillMaxWidth(),
                value = props.sliderInputValueRange,
                valueRange = props.sliderValueRange,
                onValueChange = {
                    val fractionValueStart = it.start.upToDecimals(props.fractionDecimal ?: 0)
                    val fractionValueEnd = it.endInclusive.upToDecimals(props.fractionDecimal ?: 0)
                    onRangeSliderValueChange(fractionValueStart.rangeTo(fractionValueEnd))
                    onValueChangeFinished()
                },
                endThumb = {
                    ODSSliderThumb(
                        modifier = Modifier.focusProperties { canFocus = false },
                        scheme = scheme
                    )
                },
                startThumb = {
                    ODSSliderThumb(
                        modifier = Modifier.focusProperties { canFocus = false },
                        scheme = scheme
                    )
                },
                track = { state ->
                    SliderDefaults.Track(
                        modifier = Modifier
                            .height(style.trackContainerHeight ?: 0.dp)
                            .fillMaxWidth(),
                        rangeSliderState = state,
                        thumbTrackGapSize = 0.dp,
                        trackInsideCornerSize = 0.dp,
                        colors = getTrackColors(style)
                    )
                },
            )
        }

        ODSSliderLabelContainer(
            minValue = getInputText(props.min ?: 0f, props),
            maxValue = getInputText(props.max ?: DEFAULT_MAX_TRACK_VALUE, props),
            style = style
        )
    }
}

@Composable
private fun ODSSliderLabelContainer(
    minValue: String,
    maxValue: String,
    style: ODSSliderStyle,
) {
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .clearAndSetSemantics { },
        horizontalArrangement = style.trackLabelContainerHorizontalArrangement,
        verticalAlignment = style.trackLabelContainerVerticalAlignment
    ) {
        ODSText(
            text = minValue,
            style = style.minLabelTextStyle,
            color = style.minLabelColor,
            textAlign = style.minLabelTextAlign
        )

        ODSText(
            text = maxValue,
            style = style.maxLabelTextStyle,
            color = style.maxLabelColor,
            textAlign = style.maxLabelTextAlign
        )
    }
}

private fun getInputText(value: Float, props: ODSSliderProps): String {
    return if (props.fractionDecimal == 0) {
        String.format(Locale.ROOT, "%.0f", value)
    } else {
        value.upToDecimals(
            props.fractionDecimal ?: 0
        ).toString()
    }
}

@Composable
private fun getTrackColors(style: ODSSliderStyle): SliderColors {
    return SliderDefaults.colors(
        inactiveTickColor = style.progressBackgroundColor?.getOrNull(0)?.hexColor?.getColor()
            ?: Color.Transparent,
        activeTickColor = style.trackContainerBackgroundColor?.getOrNull(0)?.hexColor?.getColor()
            ?: Color.Transparent,
        activeTrackColor = style.progressBackgroundColor?.getOrNull(0)?.hexColor?.getColor()
            ?: Color.Transparent,
        inactiveTrackColor = style.trackContainerBackgroundColor?.getOrNull(0)?.hexColor?.getColor()
            ?: Color.Transparent,
    )
}

private val ODSSliderProps.sliderInputMaxInputValue: Float
    get() = inputFieldGroupProps.sliderInputMaxProps?.inputValue?.toFloatOrNull() ?: 0f

private val ODSSliderProps.sliderInputMinInputValue: Float
    get() = inputFieldGroupProps.sliderInputMinProps?.inputValue?.toFloatOrNull() ?: 0f

private val ODSSliderProps.sliderInputValueRange: ClosedFloatingPointRange<Float>
    get() {
        val min = inputFieldGroupProps.sliderInputMinProps?.inputValue?.toFloatOrNull() ?: 0f
        val max = inputFieldGroupProps.sliderInputMaxProps?.inputValue?.toFloatOrNull()
            ?: DEFAULT_MAX_TRACK_VALUE
        return min.rangeTo(max)
    }

private val ODSSliderProps.sliderValueRange: ClosedFloatingPointRange<Float>
    get() = min?.rangeTo(max ?: DEFAULT_MAX_TRACK_VALUE) ?: 0f.rangeTo(DEFAULT_MAX_TRACK_VALUE)

@Suppress("All")
private fun Modifier.applySemanticsForSlider(
    props: ODSSliderProps,
    context: Context
): Modifier {
    return this.semantics(mergeDescendants = true) {
        var singleValue = ""
        var rangeValue = ""
        var value = context.getString(R.string.semantic_slider)
        if (props.inputFieldGroupProps.variant == ODSSliderInputFieldGroupVariant.SINGLE) {
            singleValue =
                props.inputFieldGroupProps.labelMax.orEmpty() +
                        "\n" + props.inputFieldGroupProps.sliderInputMaxProps?.inputValue
        } else {
            rangeValue =
                props.inputFieldGroupProps.labelMin.orEmpty() +
                        "\n" + props.inputFieldGroupProps.sliderInputMinProps?.inputValue.orEmpty() +
                        "\n" + props.inputFieldGroupProps.labelMax.orEmpty() +
                        "\n" + props.inputFieldGroupProps.sliderInputMaxProps?.inputValue
        }
        value = value + "\n" + context.getString(R.string.semantic_with) +
                "\n" + context.getString(R.string.semantic_range) +
                "\n" + props.min.toString() +
                context.getString(R.string.semantic_to) +
                props.max.toString()

        value = "\n $value \n $singleValue $rangeValue"

        stateDescription = value
    }
}

const val DEFAULT_MAX_TRACK_VALUE = 100f
val SLIDER_DEFAULT_VERTICAL_PADDING = 8.dp
