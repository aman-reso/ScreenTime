package com.app.screentime.profile.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.app.screentime.profile.utils.LanguageUtils
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.controls.ODSControlsType
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControls
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsProps
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardchoicesimple.ODSCardChoiceSimple
import com.telekom.odsystem.organisms.cardchoicesimple.ODSCardChoiceSimpleProps
import com.telekom.odsystem.organisms.cardchoicesimple.ODSCardChoiceSimpleType
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Language selection dialog with radio buttons
 */
@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onDismiss: () -> Unit = {},
    onLanguageSelected: (String) -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {
    ODSDialog(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight(),
        scheme = scheme,
        onDismissRequest = onDismiss,
        props = ODSDialogProps(
            showCloseButton = true,
            showScrollbar = false,
            title = stringResource(R.string.select_language),
            bodyText = null
        ),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                LanguageUtils.languages.forEach { language ->
                    ODSListRowControls(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSListRowControlsProps(
                            variant = ODSListRowControlsVariant.STANDARD,
                            type = ODSControlsType.RADIO_ICON,
                            labelText = stringResource(language.displayName),
                            selected = currentLanguage == language.value,
                        ),
                        onRadioClick = {
                            onLanguageSelected(language.value)
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
