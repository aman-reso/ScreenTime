package com.telekom.odsystem.organisms.datepicker

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
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
import com.telekom.odsystem.atoms.datepickerinputfield.ODSDatePickerInputField
import com.telekom.odsystem.atoms.datepickerinputfield.ODSDatePickerInputFieldProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * ODSDatePicker composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param focusRequester The focus requester for this component.
 * @param onValueChange Callback triggered when the input value changes.
 * @param onValueSubmit Callback triggered when the user submits the input value.
 * @param onCalendarIconClick Callback triggered when the calendar icon is clicked.
 * @param onCancel Callback triggered when the date picker dialog is cancelled.
 * @param onConfirm Callback triggered when a date is confirmed in the date picker dialog.
 * @param onFocusChange Callback triggered when the focus state of the input field changes.
 */
@Composable
fun ODSDatePicker(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSDatePickerProps = ODSDatePickerProps(),
    focusRequester: FocusRequester = remember { FocusRequester() },
    onValueChange: (String) -> Unit = { },
    onValueSubmit: () -> Unit = { },
    onCalendarIconClick: () -> Unit = { },
    onCancel: () -> Unit = { },
    onConfirm: (String) -> Unit = { },
    onFocusChange: (FocusState) -> Unit = { }
) {
    val style = ODSDatePickerStyle().getStyle(scheme = scheme, props = props)
    ODSBox(
        modifier = modifier,
        contentAlignment = style.contentAlignment
    ) {
        ODSColumn(
            gap = style.gap,
            verticalArrangement = style.verticalArrangement,
            verticalAlignment = style.verticalAlignment,
            horizontalAlignment = style.horizontalAlignment,
        ) {
            val datePickerInputFieldProps =
                props.datePickerInputFieldProps ?: ODSDatePickerInputFieldProps()
            ODSDatePickerInputField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged(onFocusChange),
                scheme = scheme,
                props = datePickerInputFieldProps,
                onValueSubmit = onValueSubmit,
                onValueChange = onValueChange,
                onCalendarIconClick = onCalendarIconClick,
            )
        }
        if (props.expanded) {
            ODSDatePickerFlyout(
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
fun ODSDatePickerFlyout(
    style: ODSDatePickerStyle,
    scheme: ODSTheme,
    props: ODSDatePickerProps,
    onCancel: () -> Unit,
    onConfirm: (selectedDate: String) -> Unit
) {
    val dateFormat = props.datePickerInputFieldProps?.dateFormat
    val inputText = props.datePickerInputFieldProps?.inputText
    val sdf = remember(dateFormat) { dateFormat?.simpleDateFormatInstance }
    val selectedDateMillis = remember(inputText, dateFormat) {
        props.parseAndValidateDate(sdf = sdf)
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis,
        yearRange = props.getYearRange,
        selectableDates = props.getSelectableDates
    )
    DatePickerDialog(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        onDismissRequest = onCancel,
        confirmButton = {
            ODSButton(
                scheme = scheme,
                props = ODSButtonProps(
                    label = stringResource(R.string.semantics_ok),
                    size = ODSButtonSize.SMALL,
                    variant = ODSButtonVariant.GHOST,
                ),
                onClick = {
                    val pickedDateMillis = datePickerState.selectedDateMillis
                    if (sdf != null && pickedDateMillis != null) {
                        onConfirm(sdf.format(Date(pickedDateMillis)))
                        return@ODSButton
                    }
                    onCancel()
                }
            )
        },
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
        colors = style.getDatePickerColors
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false,
            colors = style.getDatePickerColors
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private val ODSDatePickerProps.getYearRange: IntRange
    get() {
        val datePickerDefaultRange = DatePickerDefaults.YearRange
        val calendar = Calendar.getInstance()
        val sdf = datePickerInputFieldProps?.dateFormat?.simpleDateFormatInstance
        return runCatching {
            val startYear = startDate?.let { sdf?.parse(it) }
                ?.let { calendar.apply { time = it }.get(Calendar.YEAR) }
                ?: datePickerDefaultRange.first
            val lastYear = endDate?.let { sdf?.parse(it) }
                ?.let { calendar.apply { time = it }.get(Calendar.YEAR) }
                ?: datePickerDefaultRange.last
            startYear.rangeTo(lastYear)
        }.getOrNull() ?: datePickerDefaultRange
    }

private val String.simpleDateFormatInstance: SimpleDateFormat
    get() = SimpleDateFormat(
        this, Locale.getDefault()
    ).apply {
        isLenient = false
        timeZone = TimeZone.getTimeZone("UTC")
    }

private fun ODSDatePickerProps.parseAndValidateDate(
    sdf: SimpleDateFormat?
): Long? {
    return this.datePickerInputFieldProps?.inputText?.let {
        runCatching {
            val parsedDate = sdf?.parse(it)?.time ?: return@runCatching null
            Calendar.getInstance().apply { timeInMillis = parsedDate }.let { calendar ->
                if (calendar.get(Calendar.YEAR) in this.getYearRange) parsedDate else null
            }
        }.getOrNull()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private val ODSDatePickerStyle.getDatePickerColors: DatePickerColors
    @Composable get() {
        return DatePickerDefaults.colors().copy(
            containerColor = backgroundColor?.getColor() ?: Color.Transparent,
            selectedYearContentColor = selectedYearContentColor?.getColor()
                ?: Color.Transparent,
            selectedYearContainerColor = selectedYearContainerColor?.getColor()
                ?: Color.Transparent,
            selectedDayContentColor = selectedDayContentColor?.getColor() ?: Color.Transparent,
            selectedDayContainerColor = selectedDayContainerColor?.getColor()
                ?: Color.Transparent,
            disabledDayContentColor = disabledDayContentColor?.getColor() ?: Color.Transparent,
            disabledYearContentColor = disabledYearContentColor?.getColor()
                ?: Color.Transparent,
            todayContentColor = todayContentColor?.getColor() ?: Color.Transparent,
            todayDateBorderColor = todayDateBorderColor?.getColor() ?: Color.Transparent,
            titleContentColor = titleContentColor?.getColor() ?: Color.Transparent,
            yearContentColor = yearContentColor?.getColor() ?: Color.Transparent,
            dividerColor = dividerColor?.getColor() ?: Color.Transparent,
            headlineContentColor = headlineContentColor?.getColor() ?: Color.Transparent,
            dayContentColor = dayContentColor?.getColor() ?: Color.Transparent,
            navigationContentColor = navigationContentColor?.getColor() ?: Color.Transparent,
            weekdayContentColor = weekdayContentColor?.getColor() ?: Color.Transparent,
            currentYearContentColor = currentYearContentColor?.getColor() ?: Color.Transparent,
        )
    }

@OptIn(ExperimentalMaterial3Api::class)
private val ODSDatePickerProps.getSelectableDates: SelectableDates
    get() = object : SelectableDates {
        val sdf = datePickerInputFieldProps?.dateFormat?.simpleDateFormatInstance
        val timeRange = runCatching {
            startDate?.let { sdf?.parse(startDate)?.time } to endDate?.let { sdf?.parse(endDate)?.time }
        }.getOrNull()
        val startTime = timeRange?.first
        val endTime = timeRange?.second
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            if (startTime != null && utcTimeMillis < startTime) return false
            if (endTime != null && utcTimeMillis > endTime) return false
            return shouldDisableDate?.invoke(utcTimeMillis)?.not() != false
        }
    }
