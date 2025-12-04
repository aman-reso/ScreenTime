package com.app.screentime.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.controls.ODSControlsType
import com.telekom.odsystem.extensions.background
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControls
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsProps
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsVariant

@Preview(showBackground = true)
@Composable
fun ODSListRowControlsPreview() {
    var switchState by remember { mutableStateOf(false) }
    var checkboxState by remember { mutableStateOf(false) }
    var radioState by remember { mutableStateOf(false) }

    ODSBox(modifier = Modifier, background = listOf(ODSColorModel(neutralScheme.basicBackground))) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(DSVariables.spacingComponent5),
            gap = DSVariables.spacingComponent4,
        ) {
            // Switch - Standard - Unselected
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.STANDARD,
                    type = ODSControlsType.SWITCH_ICON,
                    labelText = "Switch Control",
                    labelTitle = "Standard Variant",
                    selected = switchState
                ),
                onSwitchClick = { switchState = !switchState }
            )

            // Switch - Standard - Selected
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.STANDARD,
                    type = ODSControlsType.SWITCH_ICON,
                    labelText = "Switch Control Selected",
                    labelTitle = "Standard Variant",
                    selected = true
                ),
                onSwitchClick = { }
            )

            // Switch - With Icon - Unselected
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.ICON,
                    type = ODSControlsType.SWITCH_ICON,
                    labelText = "Switch with Icon",
                    labelTitle = "Icon Variant",
                    icon = ODSIconModel(imageVector = Icons.Default.Notifications),
                    selected = switchState
                ),
                onSwitchClick = { switchState = !switchState }
            )

            // Switch - With Icon - Selected
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.ICON,
                    type = ODSControlsType.SWITCH_ICON,
                    labelText = "Switch with Icon Selected",
                    labelTitle = "Icon Variant",
                    icon = ODSIconModel(imageVector = Icons.Default.Notifications),
                    selected = true
                ),
                onSwitchClick = { }
            )

            // Checkbox - Standard - Unselected
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.STANDARD,
                    type = ODSControlsType.CHECKBOX_ICON,
                    labelText = "Checkbox Control",
                    labelTitle = "Standard Variant",
                    selected = checkboxState
                ),
                onCheckboxClick = { checkboxState = !checkboxState }
            )

            // Checkbox - Standard - Selected
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.STANDARD,
                    type = ODSControlsType.CHECKBOX_ICON,
                    labelText = "Checkbox Control Selected",
                    labelTitle = "Standard Variant",
                    selected = true
                ),
                onCheckboxClick = { }
            )

            // Checkbox - With Icon - Unselected
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.ICON,
                    type = ODSControlsType.CHECKBOX_ICON,
                    labelText = "Checkbox with Icon",
                    labelTitle = "Icon Variant",
                    icon = ODSIconModel(imageVector = Icons.Default.CheckCircle),
                    selected = checkboxState
                ),
                onCheckboxClick = { checkboxState = !checkboxState }
            )

            // Checkbox - With Icon - Selected
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.ICON,
                    type = ODSControlsType.CHECKBOX_ICON,
                    labelText = "Checkbox with Icon Selected",
                    labelTitle = "Icon Variant",
                    icon = ODSIconModel(imageVector = Icons.Default.CheckCircle),
                    selected = true
                ),
                onCheckboxClick = { }
            )

            // Radio - Standard - Unselected
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.STANDARD,
                    type = ODSControlsType.RADIO_ICON,
                    labelText = "Radio Control",
                    labelTitle = "Standard Variant",
                    selected = radioState
                ),
                onRadioClick = { radioState = !radioState }
            )

            // Radio - Standard - Selected
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.STANDARD,
                    type = ODSControlsType.RADIO_ICON,
                    labelText = "Radio Control Selected",
                    labelTitle = "Standard Variant",
                    selected = true
                ),
                onRadioClick = { }
            )

            // Radio - With Icon - Unselected
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.ICON,
                    type = ODSControlsType.RADIO_ICON,
                    labelText = "Radio with Icon",
                    labelTitle = "Icon Variant",
                    icon = ODSIconModel(imageVector = Icons.Default.RadioButtonChecked),
                    selected = radioState
                ),
                onRadioClick = { radioState = !radioState }
            )

            // Radio - With Icon - Selected
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.ICON,
                    type = ODSControlsType.RADIO_ICON,
                    labelText = "Radio with Icon Selected",
                    labelTitle = "Icon Variant",
                    icon = ODSIconModel(imageVector = Icons.Default.RadioButtonChecked),
                    selected = true
                ),
                onRadioClick = { }
            )

            // Disabled State
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.STANDARD,
                    type = ODSControlsType.SWITCH_ICON,
                    labelText = "Disabled Switch",
                    labelTitle = "Disabled State",
                    selected = false,
                    disabled = true
                ),
                onSwitchClick = { }
            )

            // ReadOnly State
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.STANDARD,
                    type = ODSControlsType.SWITCH_ICON,
                    labelText = "ReadOnly Switch",
                    labelTitle = "ReadOnly State",
                    selected = true,
                    readOnly = true
                ),
                onSwitchClick = { }
            )

            // With Image Variant
            ODSListRowControls(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowControlsProps(
                    variant = ODSListRowControlsVariant.IMAGE,
                    type = ODSControlsType.SWITCH_ICON,
                    labelText = "Switch with Image",
                    labelTitle = "Image Variant",
                    selected = switchState
                ),
                onSwitchClick = { switchState = !switchState }
            )
        }
    }
}

