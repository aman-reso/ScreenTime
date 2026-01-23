package com.app.screentime.landing.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldMode
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Bottom sheet for editing daily goal
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyGoalBottomSheet(
    showBottomSheet: Boolean,
    currentGoalHours: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit,
    scheme: ODSTheme = neutralScheme
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var goalHoursText by remember(showBottomSheet, currentGoalHours) {
        mutableStateOf(currentGoalHours.toString())
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val invalid = stringResource(R.string.daily_goal_invalid_number)
    val errorMsg = stringResource(R.string.daily_goal_exceeds_limit)
    ODSBottomSheet(
        scheme = scheme,
        props = ODSBottomSheetProps(),
        showBottomSheet = showBottomSheet,
        bottomSheetState = bottomSheetState,
        onDismissRequest = onDismiss,
        onCloseClicked = onDismiss,
        titleSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent2
            ) {
                ODSText(
                    text = stringResource(R.string.set_daily_goal),
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
                ODSText(
                    text = stringResource(R.string.enter_daily_goal_description),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )
            }
        },
        contentSlot = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DSVariables.spacingComponent4),
                verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent4)
            ) {

                ODSTextField(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        label = stringResource(R.string.daily_goal_hours),
                        placeholderText = "6",
                        inputText = goalHoursText,
                        size = ODSTextFieldSize.SMALL,
                        mode = if (errorMessage != null) ODSTextFieldMode.ERROR else ODSTextFieldMode.STANDARD,
                        supportMessageProps = if (errorMessage != null) {
                            com.telekom.odsystem.atoms.textfield.ODSTextFieldSupportMessageProps(
                                message = errorMessage
                            )
                        } else null
                    ),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = { newValue ->
                        // Only allow numeric input
                        if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                            goalHoursText = newValue
                            errorMessage = null
                        }
                    }
                )

                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = stringResource(R.string.save),
                        buttonType = ODSButtonButtonType.STANDARD,
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = {
                        val hours = goalHoursText.toIntOrNull()
                        if (hours == null || hours <= 0) {
                            errorMessage = invalid
                        } else if (hours > 24) {
                            errorMessage = errorMsg
                        } else {
                            onSave(hours)
                            onDismiss()
                        }
                    }
                )
            }
        }
    )
}

