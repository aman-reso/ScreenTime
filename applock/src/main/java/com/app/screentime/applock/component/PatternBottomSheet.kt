package com.app.screentime.applock.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Bottom sheet for pattern entry using ODS system
 */
@OptIn(ExperimentalMaterial3Api::class)
@androidx.compose.ui.ExperimentalComposeUiApi
@Composable
fun PatternBottomSheet(
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean,
    isSettingPattern: Boolean = false,
    scheme: ODSTheme = neutralScheme,
    onDismiss: () -> Unit,
    onPatternEntered: (String) -> Unit
) {
    var pattern by remember { mutableStateOf<String?>(null) }
    var confirmPattern by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(showBottomSheet) {
        if (!showBottomSheet) {
            pattern = null
            confirmPattern = null
            errorMessage = null
        }
    }

    ODSBottomSheet(
        scheme = scheme,
        props = ODSBottomSheetProps(),
        showBottomSheet = showBottomSheet,
        bottomSheetState = sheetState,
        onDismissRequest = onDismiss,
        onCloseClicked = onDismiss,
        titleSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent2
            ) {
                ODSText(
                    text = if (isSettingPattern) "Set Pattern" else "Enter Pattern",
                    style = DSTextStyles.titleM,
                    color = scheme.basicText
                )
                ODSText(
                    text = if (isSettingPattern) {
                        if (pattern == null) {
                            "Draw a pattern to secure your apps (minimum 4 dots)"
                        } else {
                            "Confirm your pattern"
                        }
                    } else {
                        "Draw your pattern to unlock this app"
                    },
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )
            }
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3,
                padding = ODSPadding(
                    horizontal = DSVariables.spacingComponent4,
                    vertical = DSVariables.spacingComponent3
                )
            ) {
                // Pattern Lock View
                if (isSettingPattern && pattern == null) {
                    // First pattern entry
                    PatternLockView(
                        modifier = Modifier,
                        scheme = scheme,
                        onPatternComplete = { patternString ->
                            if (patternString.split(",").size >= 4) {
                                pattern = patternString
                                errorMessage = null
                            } else {
                                errorMessage = "Pattern must have at least 4 dots"
                            }
                        },
                        errorMessage = errorMessage
                    )
                } else if (isSettingPattern && pattern != null && confirmPattern == null) {
                    // Confirm pattern entry
                    ODSText(
                        text = "Draw the pattern again to confirm",
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                    PatternLockView(
                        modifier = Modifier,
                        scheme = scheme,
                        onPatternComplete = { patternString ->
                            if (patternString == pattern) {
                                confirmPattern = patternString
                                errorMessage = null
                            } else {
                                errorMessage = "Patterns do not match"
                                confirmPattern = null
                            }
                        },
                        errorMessage = errorMessage
                    )
                } else {
                    // Verifying pattern (not setting)
                    PatternLockView(
                        modifier = Modifier,
                        scheme = scheme,
                        onPatternComplete = { patternString ->
                            onPatternEntered(patternString)
                        },
                        errorMessage = errorMessage
                    )
                }

                // Error message
                if (errorMessage != null) {
                    ODSText(
                        text = errorMessage ?: "",
                        style = DSTextStyles.bodySRegular,
                        color = scheme.functionalDestructiveStandard
                    )
                }
            }
        },
        actionSlot = {
            if (isSettingPattern && confirmPattern != null) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Set Pattern",
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = {
                        onPatternEntered(confirmPattern!!)
                        pattern = null
                        confirmPattern = null
                    }
                )
            }
        }
    )
}

