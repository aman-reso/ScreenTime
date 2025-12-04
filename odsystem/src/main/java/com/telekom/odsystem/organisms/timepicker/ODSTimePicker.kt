package com.telekom.odsystem.organisms.timepicker

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.timepickerinputfield.ODSTimePickerInputField
import com.telekom.odsystem.atoms.timepickerinputfield.ODSTimePickerInputFieldProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * ODSTimePicker composable.
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param focusRequester The [FocusRequester] to be used for requesting focus to the input field.
 * @param onValueChange Callback triggered when the value of the time input field changes.
 * @param onValueSubmit Callback triggered when the user submits the value in the time input field (e.g., by pressing Enter).
 * @param onClockIconClick Callback triggered when the clock icon in the time input field is clicked.
 * @param onCancel Callback triggered when the time picker dialog is dismissed without confirming a time.
 * @param onConfirm Callback triggered when a time is confirmed in the time picker dialog. The selected time is provided as a String.
 * @param onFocusChange Callback triggered when the focus state of the time input field changes.
 */
@Composable
fun ODSTimePicker(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSTimePickerProps = ODSTimePickerProps(),
    focusRequester: FocusRequester = remember { FocusRequester() },
    onValueChange: (String) -> Unit = { },
    onValueSubmit: () -> Unit = { },
    onClockIconClick: () -> Unit = { },
    onCancel: () -> Unit = { },
    onConfirm: (String) -> Unit = { },
    onFocusChange: (FocusState) -> Unit = { }
) {
    val style = ODSTimePickerStyle().getStyle(scheme = scheme, props = props)
    ODSBox(
        contentAlignment = style.contentAlignment,
        modifier = modifier.fillMaxWidth()
    ) {
        ODSColumn(
            gap = style.gap,
            verticalArrangement = style.verticalArrangement,
            verticalAlignment = style.verticalAlignment,
            horizontalAlignment = style.horizontalAlignment,
        ) {
            val timePickerInputFieldProps =
                props.timePickerInputFieldProps ?: ODSTimePickerInputFieldProps()
            ODSTimePickerInputField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged(onFocusChange),
                scheme = scheme,
                props = timePickerInputFieldProps,
                onValueChange = onValueChange,
                onValueSubmit = onValueSubmit,
                onClockIconClick = onClockIconClick
            )
        }
        if (props.showTimePicker) {
            ODSTimePickerFlyout(
                scheme = scheme,
                style = style,
                props = props,
                onCancel = onCancel,
                onConfirm = onConfirm
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ODSTimePickerFlyout(
    style: ODSTimePickerStyle,
    scheme: ODSTheme,
    props: ODSTimePickerProps,
    onCancel: () -> Unit,
    onConfirm: (selectedTime: String) -> Unit
) {
    val time = props.parseAndValidateODSTime
    val timePickerState = rememberTimePickerState(
        initialHour = time.first,
        initialMinute = time.second,
        is24Hour = true,
    )
    AlertDialog(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        containerColor = scheme.basicBackground.getColor(),
        onDismissRequest = onCancel,
        dismissButton = {
            ODSButton(
                scheme = scheme,
                props = ODSButtonProps(
                    label = stringResource(R.string.semantics_cancel),
                    size = ODSButtonSize.SMALL,
                    variant = ODSButtonVariant.GHOST
                ),
                onClick = onCancel
            )
        },
        confirmButton = {
            ODSButton(
                scheme = scheme,
                props = ODSButtonProps(
                    label = stringResource(R.string.semantics_ok),
                    size = ODSButtonSize.SMALL,
                    variant = ODSButtonVariant.GHOST,
                ),
                onClick = {
                    onConfirm(
                        formatTime(
                            hour = timePickerState.hour,
                            minute = timePickerState.minute
                        )
                    )
                }
            )
        },
        text = {
            TimePicker(
                state = timePickerState,
                colors = style.timePickerColors
            )
        }
    )
}

private val ODSTimePickerProps.parseAndValidateODSTime: Pair<Int, Int>
    get() {
        val inputText = this.timePickerInputFieldProps?.inputText
        val calendar = Calendar.getInstance()
        val timeFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).apply {
            isLenient = false
        }
        return inputText?.let {
            runCatching {
                timeFormat.parse(it)?.let {
                    val inputTimeCalendar = calendar.apply { time = it }
                    val hour = inputTimeCalendar.get(Calendar.HOUR_OF_DAY)
                    val minute = inputTimeCalendar.get(Calendar.MINUTE)
                    Pair(hour, minute)
                }
            }.getOrNull()
        } ?: run {
            getCurrentTime(calendar = calendar)
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
private val ODSTimePickerStyle.timePickerColors: TimePickerColors
    @Composable get() {
        return TimePickerDefaults.colors(
            containerColor = this.containerColor?.getColor() ?: Color.Transparent,
            clockDialColor = this.clockDialColor?.getColor() ?: Color.Transparent,
            selectorColor = this.selectorColor?.getColor() ?: Color.Transparent,
            timeSelectorUnselectedContainerColor = this.timeSelectorUnselectedContainerColor?.getColor()
                ?: Color.Transparent,
            timeSelectorUnselectedContentColor = this.timeSelectorUnselectedContentColor?.getColor()
                ?: Color.Transparent,
            timeSelectorSelectedContainerColor = this.timeSelectorSelectedContainerColor?.getColor()
                ?: Color.Transparent,
            timeSelectorSelectedContentColor = this.timeSelectorSelectedContentColor?.getColor()
                ?: Color.Transparent,
            clockDialSelectedContentColor = this.clockDialSelectedContentColor?.getColor()
                ?: Color.Transparent,
            clockDialUnselectedContentColor = this.clockDialUnselectedContentColor?.getColor()
                ?: Color.Transparent,
        )
    }

private fun getCurrentTime(calendar: Calendar): Pair<Int, Int> {
    val inputTimeCalendar = calendar.apply { time = Date() }
    val hour = inputTimeCalendar.get(Calendar.HOUR_OF_DAY)
    val minute = inputTimeCalendar.get(Calendar.MINUTE)
    return Pair(hour, minute)
}

private fun formatTime(hour: Int, minute: Int): String {
    val formattedHour = hour.toString().padStart(2, PAD_CHARACTER)
    val formattedMinute = minute.toString().padStart(2, PAD_CHARACTER)
    return "$formattedHour:$formattedMinute"
}

private const val TIME_FORMAT = "HH:mm"
private const val PAD_CHARACTER = '0'
