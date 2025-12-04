package com.telekom.odsystem.atoms.controls

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIcon
import com.telekom.odsystem.atoms.radioicon.ODSRadioIcon
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIcon
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
/**
 * ODSControls composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
fun ODSControls(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSControlsProps = ODSControlsProps()
) {
    val style = ODSControlsStyle().getStyle(scheme = scheme)
    ODSRow(
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        modifier = modifier.sizeWithinBounds(
            minWidth = style.minWidth ?: MIN_WIDTH.dp,
            minHeight = style.minHeight ?: MIN_HEIGHT.dp
        )
    ) {
        val switchIconProps = props.switchIconProps
        val checkboxIconProps = props.checkboxIconProps
        val radioIconProps = props.radioIconProps
        if (props.type == ODSControlsType.SWITCH_ICON && switchIconProps != null) {
            ODSSwitchIcon(scheme = scheme, props = switchIconProps)
        }
        if (props.type == ODSControlsType.CHECKBOX_ICON && checkboxIconProps != null) {
            ODSCheckboxIcon(scheme = scheme, props = checkboxIconProps)
        }
        if (props.type == ODSControlsType.RADIO_ICON && radioIconProps != null) {
            ODSRadioIcon(scheme = scheme, props = radioIconProps)
        }
    }
}
