package com.telekom.odsystem.atoms.dim

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
/**
 * ODSDim composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 */
fun ODSDim(modifier: Modifier = Modifier, scheme: ODSTheme = neutralScheme) {
    val style = ODSDimStyle().getStyle(scheme)
    ODSRow(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        horizontalArrangement = style.horizontalArrangement,
        background = style.backgroundColor
    ) {
    }
}

@Composable
@Preview
fun ODSDimPreview() {
    ODSBox(
        modifier = Modifier.fillMaxSize(),
    ) {
        ODSDim(scheme = neutralScheme)
    }
}
