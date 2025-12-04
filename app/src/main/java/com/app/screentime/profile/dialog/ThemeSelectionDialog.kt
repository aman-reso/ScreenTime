package com.app.screentime.profile.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.controls.ODSControlsType
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControls
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsProps
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsVariant
import com.telekom.odsystem.neutralScheme

import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Theme selection dialog with only 3 options: Light, Dark, and System
 */
@Composable
fun ThemeSelectionDialog(
    currentTheme: String,
    onDismiss: () -> Unit,
    onThemeSelected: (String) -> Unit,
    scheme: ODSTheme = neutralScheme
) {
    val themes = listOf(
        "Light" to R.string.theme_light,
        "Dark" to R.string.theme_dark,
        "System" to R.string.theme_system
    )

    ODSDialog(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight(),
        scheme = scheme,
        onDismissRequest = onDismiss,
        props = ODSDialogProps(
            showCloseButton = true,
            showScrollbar = false,
            title = stringResource(R.string.select_theme),
            bodyText = null
        ),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                themes.forEach { (themeValue, themeStringRes) ->
                    ODSListRowControls(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSListRowControlsProps(
                            variant = ODSListRowControlsVariant.STANDARD,
                            type = ODSControlsType.RADIO_ICON,
                            labelText = stringResource(themeStringRes),
                            selected = currentTheme.equals(themeValue, ignoreCase = true),
                        ),
                        onRadioClick = {
                            onThemeSelected(themeValue)
                        }
                    )
                }
            }
        },
        actionSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = stringResource(R.string.close),
                        variant = ODSButtonVariant.PRIMARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onDismiss
                )
            }
        }
    )
}
