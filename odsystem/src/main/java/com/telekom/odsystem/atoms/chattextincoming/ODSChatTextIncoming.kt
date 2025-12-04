package com.telekom.odsystem.atoms.chattextincoming

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
 * Displays an incoming chat text message.
 *
 * This composable function renders a styled row containing a text message,
 * typically used to represent a message received in a chat interface.
 *
 * @param modifier The modifier to be applied to the component.
 * @param scheme The [ODSTheme] to use for styling the component. Defaults to [neutralScheme].
 * @param props The [ODSChatTextIncomingProps] containing the text content for the message.
 *              Defaults to an empty [ODSChatTextIncomingProps].
 */
@Composable
fun ODSChatTextIncoming(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSChatTextIncomingProps = ODSChatTextIncomingProps(),
) {
    val style = ODSChatTextIncomingStyle().getStyle(scheme = scheme)
    val context = LocalContext.current

    ODSRow(
        modifier = modifier.clearAndSetSemantics {
            contentDescription =
                "${context.getString(R.string.semantics_incoming)} ${props.slotText ?: ""}"
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
