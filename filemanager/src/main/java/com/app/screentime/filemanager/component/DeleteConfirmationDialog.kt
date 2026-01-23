package com.app.screentime.filemanager.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.app.screentime.filemanager.model.FileItem
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    fileItem: FileItem,
    scheme: ODSTheme,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
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
                showCloseButton = true,
                showScrollbar = false,
                title = if (fileItem.isDirectory) stringResource(R.string.delete_folder_question) else stringResource(
                    R.string.delete_file_question
                ),
                bodyText = null
            ),
            contentSlot = {
                ODSText(
                    text = stringResource(R.string.delete_confirmation_message, fileItem.name),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            actionSlot = {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3)
                ) {
                    // Delete button
                    ODSButton(
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = stringResource(R.string.delete),
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = {
                            onConfirm()
                            onDismiss()
                        }
                    )
                }
            }
        )
    }
}

