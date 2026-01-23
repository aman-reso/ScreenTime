package com.app.screentime.wallpaper.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.screentime.wallpaper.model.Wallpaper
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun WallpaperSetDialog(
    wallpaper: Wallpaper,
    scheme: ODSTheme,
    onDismiss: () -> Unit,
    onSetHome: () -> Unit,
    onSetLock: () -> Unit,
    onSetBoth: () -> Unit,
    title: String = "Set Wallpaper",
    homeScreenLabel: String = "Home Screen",
    lockScreenLabel: String = "Lock Screen",
    bothLabel: String = "Both"
) {
    ODSDialog(
        scheme = scheme,
        onDismissRequest = onDismiss,
        props = ODSDialogProps(
            title = title,
            bodyText = wallpaper.name,
            showCloseButton = true
        ),
        contentSlot = {
            ODSColumn(
                gap = DSVariables.spacingComponent3,
                modifier = Modifier.fillMaxWidth()
            ) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = homeScreenLabel,
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onSetHome
                )

                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = lockScreenLabel,
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onSetLock
                )

                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = bothLabel,
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onSetBoth
                )
            }
        }
    )
}

