package com.telekom.odsystem.molecules.segmentedbutton

import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.segments.ODSSegments
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSSegmentedButton composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param selectedSegmentIndex Parameter for customization.
 * @param onSelectedSegmentChange Callback triggered when action occurs.
 */
@Composable
fun ODSSegmentedButton(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSegmentedButtonProps = ODSSegmentedButtonProps(),
    selectedSegmentIndex: Int = 0,
    onSelectedSegmentChange: (Int) -> Unit
) {

    val style = ODSSegmentedButtonStyle().getStyle(scheme = scheme, props = props)

    ODSRow(
        modifier = modifier.selectableGroup(),
        gap = style.gap,
        padding = style.padding,
        cornerRadius = style.borderRadius,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        background = style.backgroundColor,
    ) {
        props.segments.forEachIndexed { index, it ->
            val selected = !it.disabled && index == selectedSegmentIndex
            ODSSegments(
                modifier = if (props.variant == ODSSegmentedButtonVariant.FILL) {
                    Modifier.weight(1f)
                } else {
                    Modifier
                },
                scheme = scheme,
                props = it.toODSSegmentsProps(
                    size = props.size,
                    variant = props.variant,
                    selected = selected
                ),
                onClick = {
                    onSelectedSegmentChange(index)
                }
            )
        }
    }
}
