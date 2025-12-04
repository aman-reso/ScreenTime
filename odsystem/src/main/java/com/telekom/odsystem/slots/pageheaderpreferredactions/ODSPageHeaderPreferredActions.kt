package com.telekom.odsystem.slots.pageheaderpreferredactions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ODSPageHeaderPreferredActions(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSPageHeaderPreferredActionsProps = ODSPageHeaderPreferredActionsProps(),
    action3onClick: () -> Unit = { },
    action2onClick: () -> Unit = { },
    action1onClick: () -> Unit = { }
) {
    val style = ODSPageHeaderPreferredActionsStyle().getStyle(scheme = scheme)
    ODSRow(
        gap = style.gap,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        modifier = modifier
    ) {
        props.action3Props?.let {
            ODSButton(scheme = scheme, props = it, onClick = action3onClick)
        }
        props.action2Props?.let {
            ODSButton(scheme = scheme, props = it, onClick = action2onClick)
        }
        props.action1Props?.let {
            ODSButton(scheme = scheme, props = it, onClick = action1onClick)
        }
    }
}
