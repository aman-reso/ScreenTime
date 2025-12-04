package com.telekom.odsystem.atoms.divider

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSDivider composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSDivider(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSDividerProps = ODSDividerProps()
) {
    val style = ODSDividerStyle().getStyle(scheme = scheme, props = props)

    ODSRow(
        modifier = modifier,
        padding = style.padding,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        horizontalArrangement = style.horizontalArrangement
    ) {
        if (props.variant == ODSDividerVariant.HORIZONTAL) {
            style.backgroundColor?.getColor()?.let {
                HorizontalDivider(
                    color = it,
                    thickness = style.thickness ?: 0.dp
                )
            }
        } else {
            style.backgroundColor?.getColor()?.let {
                VerticalDivider(
                    color = it,
                    thickness = style.thickness ?: 0.dp
                )
            }
        }
    }
}

@Composable
@Preview
fun ODSDividerPreview() {
    ODSDivider(
        scheme = neutralScheme,
        props = ODSDividerProps()
    )
}
