package com.telekom.odsystem.atoms.chattextoutgoing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Composable function that displays an outgoing chat message text.
 *
 * @param modifier The modifier to be applied to the component.
 * @param scheme The ODSTheme to be used for styling the component. Defaults to [neutralScheme].
 * @param props The [ODSChatTextOutgoingProps] to configure the component.
 */
@Composable
fun ODSChatTextOutgoing(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSChatTextOutgoingProps = ODSChatTextOutgoingProps(),
) {
    val style = ODSChatTextOutgoingStyle().getStyle(scheme = scheme)
    val context = LocalContext.current

    ODSRow(
        modifier = modifier.clearAndSetSemantics {
            contentDescription =
                "${context.getString(R.string.semantics_outgoing)} ${props.slotText ?: ""}"
        },
        padding = style.padding,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement,
    ) {
        if (!props.slotText.isNullOrEmpty()) {
            ODSText(
                text = props.slotText,
                style = style.textStyle,
                color = style.textColor,
                textAlign = style.textTextAlign,
                maxWidth = style.textMaxWidth
            )
        }
    }
}
