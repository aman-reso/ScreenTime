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
import com.app.screentime.applock.repository.AppLockRepository.LockType
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
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
 * Bottom sheet for selecting lock type (PIN or Pattern)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockTypeSelectionBottomSheet(
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean,
    scheme: ODSTheme = neutralScheme,
    onDismiss: () -> Unit,
    onLockTypeSelected: (LockType) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(showBottomSheet) {
        if (!showBottomSheet) {
            // Reset when dismissed
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
                    text = "Choose Lock Type",
                    style = DSTextStyles.titleM,
                    color = scheme.basicText
                )
                ODSText(
                    text = "Select how you want to secure your apps",
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
                // PIN Option
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "PIN",
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.LARGE
                    ),
                    onClick = {
                        onLockTypeSelected(LockType.PIN)
                        onDismiss()
                    }
                )

                // Pattern Option
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Pattern",
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.LARGE
                    ),
                    onClick = {
                        onLockTypeSelected(LockType.PATTERN)
                        onDismiss()
                    }
                )
            }
        },
        actionSlot = {
            // No action buttons needed
        }
    )
}

