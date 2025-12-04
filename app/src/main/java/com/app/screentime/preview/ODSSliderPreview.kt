package com.app.screentime.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.sliderinputfield.ODSSliderInputFieldProps
import com.telekom.odsystem.atoms.sliderinputfieldgroup.ODSSliderInputFieldGroupVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.slider.ODSSlider
import com.telekom.odsystem.organisms.slider.ODSSliderProps

@Preview(showBackground = true)
@Composable
fun ODSSliderPreview() {
    ODSBox(
        modifier = Modifier,
        background = listOf(ODSColorModel(neutralScheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(DSVariables.spacingComponent5),
            gap = DSVariables.spacingComponent4
        ) {
            // Basic Slider Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Basic Sliders",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )

                // Simple Slider without Input Fields
                var value1 by remember { mutableFloatStateOf(50f) }
                ODSSlider(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSliderProps(
                        min = 0f,
                        max = 100f,
                        fractionDecimal = 0
                    ),
                    onSliderValueChange = { value1 = it }
                )

                ODSText(
                    text = "Value: ${value1.toInt()}",
                    style = DSTextStyles.bodyMRegular,
                    color = neutralScheme.basicTextRecessive
                )

                // Slider with Decimal Values
                var value2 by remember { mutableFloatStateOf(2.5f) }
                ODSSlider(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSliderProps(
                        min = 0f,
                        max = 5f,
                        fractionDecimal = 1
                    ),
                    onSliderValueChange = { value2 = it }
                )

                ODSText(
                    text = "Value: $value2",
                    style = DSTextStyles.bodyMRegular,
                    color = neutralScheme.basicTextRecessive
                )
            }

            // Slider with Single Input Field
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Slider with Single Input Field",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )

                var value3 by remember { mutableFloatStateOf(25f) }
                var textValue3 by remember { mutableStateOf("25") }

                ODSSlider(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSliderProps(
                        min = 0f,
                        max = 100f,
                        fractionDecimal = 0,
                        inputFieldGroupProps = com.telekom.odsystem.atoms.sliderinputfieldgroup.ODSSliderInputFieldGroupProps(
                            variant = ODSSliderInputFieldGroupVariant.SINGLE,
                            sliderInputMinProps = ODSSliderInputFieldProps(
                                inputValue = textValue3
                            )
                        )
                    ),
                    onSliderValueChange = { newValue ->
                        value3 = newValue
                        textValue3 = newValue.toInt().toString()
                    },
                    onMinValueChange = { newText ->
                        textValue3 = newText
                        newText.toFloatOrNull()?.let {
                            if (it in 0f..100f) {
                                value3 = it
                            }
                        }
                    }
                )

                ODSText(
                    text = "Current value: ${value3.toInt()}",
                    style = DSTextStyles.bodyMRegular,
                    color = neutralScheme.basicTextRecessive
                )
            }

            // Range Slider Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Range Sliders",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )

                // Range Slider without Input Fields
                var rangeValue1 by remember { mutableStateOf(20f..80f) }
                ODSSlider(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSliderProps(
                        min = 0f,
                        max = 100f,
                        fractionDecimal = 0,
                        inputFieldGroupProps = com.telekom.odsystem.atoms.sliderinputfieldgroup.ODSSliderInputFieldGroupProps(
                            variant = ODSSliderInputFieldGroupVariant.SIDE_BY_SIDE
                        )
                    ),
                    onRangeSliderValueChange = { range ->
                        rangeValue1 = range.start..range.endInclusive
                    }
                )

                ODSText(
                    text = "Range: ${rangeValue1.start.toInt()} - ${rangeValue1.endInclusive.toInt()}",
                    style = DSTextStyles.bodyMRegular,
                    color = neutralScheme.basicTextRecessive
                )

                // Range Slider with Side-by-Side Input Fields
                var rangeValue2 by remember { mutableStateOf(10f..90f) }
                var minText by remember { mutableStateOf("10") }
                var maxText by remember { mutableStateOf("90") }

                ODSSlider(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSliderProps(
                        min = 0f,
                        max = 100f,
                        fractionDecimal = 0,
                        inputFieldGroupProps = com.telekom.odsystem.atoms.sliderinputfieldgroup.ODSSliderInputFieldGroupProps(
                            variant = ODSSliderInputFieldGroupVariant.SIDE_BY_SIDE,
                            labelMin = "Min",
                            labelMax = "Max",
                            sliderInputMinProps = ODSSliderInputFieldProps(inputValue = minText),
                            sliderInputMaxProps = ODSSliderInputFieldProps(inputValue = maxText)
                        )
                    ),
                    onRangeSliderValueChange = { range ->
                        rangeValue2 = range.start..range.endInclusive
                        minText = range.start.toInt().toString()
                        maxText = range.endInclusive.toInt().toString()
                    },
                    onMinValueChange = { newText ->
                        minText = newText
                        newText.toFloatOrNull()?.let {
                            if (it in 0f..rangeValue2.endInclusive) {
                                rangeValue2 = it..rangeValue2.endInclusive
                            }
                        }
                    },
                    onMaxValueChange = { newText ->
                        maxText = newText
                        newText.toFloatOrNull()?.let {
                            if (it in rangeValue2.start..100f) {
                                rangeValue2 = rangeValue2.start..it
                            }
                        }
                    }
                )

                ODSText(
                    text = "Range: ${rangeValue2.start.toInt()} - ${rangeValue2.endInclusive.toInt()}",
                    style = DSTextStyles.bodyMRegular,
                    color = neutralScheme.basicTextRecessive
                )

                // Range Slider with Stacked Input Fields
                var rangeValue3 by remember { mutableStateOf(30f..70f) }
                var minText3 by remember { mutableStateOf("30") }
                var maxText3 by remember { mutableStateOf("70") }

                ODSSlider(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSliderProps(
                        min = 0f,
                        max = 100f,
                        fractionDecimal = 0,
                        inputFieldGroupProps = com.telekom.odsystem.atoms.sliderinputfieldgroup.ODSSliderInputFieldGroupProps(
                            variant = ODSSliderInputFieldGroupVariant.STACKED,
                            labelMin = "Minimum",
                            labelMax = "Maximum",
                            sliderInputMinProps = ODSSliderInputFieldProps(inputValue = minText3),
                            sliderInputMaxProps = ODSSliderInputFieldProps(inputValue = maxText3)
                        )
                    ),
                    onRangeSliderValueChange = { range ->
                        rangeValue3 = range.start..range.endInclusive
                        minText3 = range.start.toInt().toString()
                        maxText3 = range.endInclusive.toInt().toString()
                    },
                    onMinValueChange = { newText ->
                        minText3 = newText
                        newText.toFloatOrNull()?.let {
                            if (it in 0f..rangeValue3.endInclusive) {
                                rangeValue3 = it..rangeValue3.endInclusive
                            }
                        }
                    },
                    onMaxValueChange = { newText ->
                        maxText3 = newText
                        newText.toFloatOrNull()?.let {
                            if (it in rangeValue3.start..100f) {
                                rangeValue3 = rangeValue3.start..it
                            }
                        }
                    }
                )

                ODSText(
                    text = "Range: ${rangeValue3.start.toInt()} - ${rangeValue3.endInclusive.toInt()}",
                    style = DSTextStyles.bodyMRegular,
                    color = neutralScheme.basicTextRecessive
                )
            }

            // Custom Range Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Custom Range Examples",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )

                // Price Range Slider
                var priceRange by remember { mutableStateOf(50f..500f) }
                var minPrice by remember { mutableStateOf("50") }
                var maxPrice by remember { mutableStateOf("500") }

                ODSText(
                    text = "Price Range (€)",
                    style = DSTextStyles.bodyMBold,
                    color = neutralScheme.basicText
                )

                ODSSlider(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSliderProps(
                        min = 0f,
                        max = 1000f,
                        fractionDecimal = 0,
                        inputFieldGroupProps = com.telekom.odsystem.atoms.sliderinputfieldgroup.ODSSliderInputFieldGroupProps(
                            variant = ODSSliderInputFieldGroupVariant.SIDE_BY_SIDE,
                            labelMin = "Min",
                            labelMax = "Max",
                            sliderInputMinProps = ODSSliderInputFieldProps(inputValue = minPrice),
                            sliderInputMaxProps = ODSSliderInputFieldProps(inputValue = maxPrice)
                        )
                    ),
                    onRangeSliderValueChange = { range ->
                        priceRange = range.start..range.endInclusive
                        minPrice = range.start.toInt().toString()
                        maxPrice = range.endInclusive.toInt().toString()
                    },
                    onMinValueChange = { newText ->
                        minPrice = newText
                        newText.toFloatOrNull()?.let {
                            if (it in 0f..priceRange.endInclusive) {
                                priceRange = it..priceRange.endInclusive
                            }
                        }
                    },
                    onMaxValueChange = { newText ->
                        maxPrice = newText
                        newText.toFloatOrNull()?.let {
                            if (it in priceRange.start..1000f) {
                                priceRange = priceRange.start..it
                            }
                        }
                    }
                )

                ODSText(
                    text = "Selected: €${priceRange.start.toInt()} - €${priceRange.endInclusive.toInt()}",
                    style = DSTextStyles.bodyMRegular,
                    color = neutralScheme.basicTextRecessive
                )

                // Duration Slider (with decimals)
                var duration by remember { mutableFloatStateOf(2.5f) }
                var durationText by remember { mutableStateOf("2.5") }

                ODSText(
                    text = "Duration (hours)",
                    style = DSTextStyles.bodyMBold,
                    color = neutralScheme.basicText
                )

                ODSSlider(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSliderProps(
                        min = 0f,
                        max = 24f,
                        fractionDecimal = 1,
                        inputFieldGroupProps = com.telekom.odsystem.atoms.sliderinputfieldgroup.ODSSliderInputFieldGroupProps(
                            variant = ODSSliderInputFieldGroupVariant.SINGLE,
                            sliderInputMinProps = ODSSliderInputFieldProps(inputValue = durationText)
                        )
                    ),
                    onSliderValueChange = { newValue ->
                        duration = newValue
                        durationText = String.format("%.1f", newValue)
                    },
                    onMinValueChange = { newText ->
                        durationText = newText
                        newText.toFloatOrNull()?.let {
                            if (it in 0f..24f) {
                                duration = it
                            }
                        }
                    }
                )

                ODSText(
                    text = "Selected: ${String.format("%.1f", duration)} hours",
                    style = DSTextStyles.bodyMRegular,
                    color = neutralScheme.basicTextRecessive
                )
            }
        }
    }
}

