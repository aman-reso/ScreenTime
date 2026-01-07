package com.app.screentime.reward.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Claim Confirmation Dialog Component
 * Displays confirmation message after form submission
 */
@Composable
fun ClaimConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = {},
    scheme: ODSTheme
) {
    if (showDialog) {
        ODSDialog(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            scheme = scheme,
            onDismissRequest = onDismiss,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            props = ODSDialogProps(
                showCloseButton = false,
                showScrollbar = false,
                title = null,
                bodyText = null
            ),
            contentSlot = {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent4
                ) {
                    ODSText(
                        text = "Confirm Claim",
                        style = DSTextStyles.subtitle,
                        color = scheme.basicText,
                        modifier = Modifier.fillMaxWidth()
                    )

                    ODSText(
                        text = "Are you sure you want to proceed with this claim?",
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            actionSlot = {
                ODSButton(
                    modifier = Modifier,
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Confirm",
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = {
                        onConfirm()
                        onDismiss()
                    }
                )
            }
        )
    }
}




















