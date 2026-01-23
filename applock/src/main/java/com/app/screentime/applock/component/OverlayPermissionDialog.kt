package com.app.screentime.applock.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Dialog to request overlay permission when user tries to lock an app
 */
@Composable
fun OverlayPermissionDialog(
    showDialog: Boolean,
    scheme: ODSTheme = neutralScheme,
    onDismiss: () -> Unit,
    onAllowClick: () -> Unit
) {
    if (showDialog) {
        ODSDialog(
            scheme = scheme,
            onDismissRequest = onDismiss,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            props = ODSDialogProps(
                bodyText = "Permission Required",
                showCloseButton = true
            ),
            contentSlot = {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent3,
                    padding = ODSPadding(vertical = DSVariables.spacingComponent4)
                ) {
                    ODSText(
                        text = "To lock apps, ScreenTime needs permission to display over other apps",
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicText
                    )

                    ODSBox(height = DSVariables.spacingComponent2) {}

                    ODSText(
                        text = "Please grant this permission in the system settings to continue.",
                        style = DSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            },
            actionSlot = {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    ODSButton(
                        modifier = Modifier,
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Allow",
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = {
                            onAllowClick()
                            onDismiss()
                        }
                    )
                }
            }
        )
    }
}

