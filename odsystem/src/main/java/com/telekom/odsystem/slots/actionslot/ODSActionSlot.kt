package com.telekom.odsystem.slots.actionslot

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Composable function that displays a row of up to three action buttons.
 *
 * @param modifier The modifier to be applied to the component.
 * @param scheme The [ODSTheme] to be applied to the action slot and its buttons. Defaults to [neutralScheme].
 * @param props The [ODSActionSlotProps] to configure the properties of the action buttons. Defaults to an empty [ODSActionSlotProps].
 * @param onActionOneClick Lambda function to be executed when the first action button is clicked.
 * @param onActionTwoClick Lambda function to be executed when the second action button is clicked.
 * @param onActionThreeClick Lambda function to be executed when the third action button is clicked.
 */
@Composable
fun ODSActionSlot(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSActionSlotProps = ODSActionSlotProps(),
    onActionOneClick: () -> Unit = {},
    onActionTwoClick: () -> Unit = {},
    onActionThreeClick: () -> Unit = {},
) {

    val style = ODSActionSlotStyle().getStyle(scheme = scheme)

    ODSRow(
        modifier = modifier,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement
    ) {
        props.actionOneProps?.let { actionOneProps ->
            ODSButton(scheme = scheme, props = actionOneProps, onClick = onActionOneClick)
        }
        props.actionTwoProps?.let { actionTwoProps ->
            ODSButton(scheme = scheme, props = actionTwoProps, onClick = onActionTwoClick)
        }
        props.actionThreeProps?.let { actionThreeProps ->
            ODSButton(scheme = scheme, props = actionThreeProps, onClick = onActionThreeClick)
        }
    }
}
