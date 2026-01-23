package com.app.screentime.filemanager.component

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.app.screentime.config.R
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.link.ODSLinkAlignment
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.atoms.link.ODSLinkType
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun PermissionRequestView(
    scheme: ODSTheme,
    needsManageStorage: Boolean,
    onRequestPermission: () -> Unit
) {
    ODSColumn(
        modifier = Modifier.fillMaxSize(),
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent3
        ),
        gap = DSVariables.spacingComponent3
    ) {

        Spacer(modifier = Modifier.height(DSVariables.spacingComponent4))
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.file_manager_description),
            style = DSTextStyles.bodyMRegular,
            color = scheme.basicText
        )

        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            padding = ODSPadding(
                top = DSVariables.spacingComponent7,
                bottom = DSVariables.spacingComponent3
            )
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.storage_permission_required),
                style = DSTextStyles.bodyMRegular,
                color = scheme.basicText,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }

        ODSInlineNotification(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSInlineNotificationProps(
                mode = ODSInlineNotificationMode.ERROR,
                title = stringResource(R.string.permission_required),
                text = if (needsManageStorage) {
                    stringResource(R.string.file_manager_manage_storage_permission)
                } else {
                    stringResource(R.string.file_manager_storage_permission)
                },
                link1Props = ODSLinkProps(
                    type = ODSLinkType.SECONDARY,
                    alignment = ODSLinkAlignment.LEFT,
                    label = stringResource(R.string.grant_permission)
                ),
                showCloseButton = false
            ),
            onFirstLinkClicked = onRequestPermission,
            onDismiss = {}
        )
        Spacer(modifier = Modifier.height(DSVariables.spacingComponent6))
        ODSButton(
            modifier = Modifier.fillMaxWidth(),
            props = ODSButtonProps(
                label = "Grant permission",
                variant = ODSButtonVariant.SECONDARY,
                size = ODSButtonSize.SMALL
            ), onClick = onRequestPermission
        )
    }
}
