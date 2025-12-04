package com.telekom.odsystem.organisms.chatbubble

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.molecules.chatbubbleleadingelement.ODSChatBubbleLeadingElement
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODS Chat Bubble.
 *
 * It is a custom Composable function that displays a chat bubble.
 * It supports different variants (incoming/outgoing), and can show an avatar, helper text, actions, and an error indicator.
 * The content of the chat bubble can be customized using the `slotContentIncoming` and `slotContentOutgoing` parameters.
 *
 * @param modifier Modifier to be applied to the chat bubble.
 * @param scheme Current ODS color scheme.
 * @param props Properties for the chat bubble.
 * @param actionSlot Optional slot for actions in incoming messages.
 * @param slotContentIncoming Optional slot for the content of incoming messages.
 * @param slotContentOutgoing Optional slot for the content of outgoing messages.
 * @param onClick Optional click listener for the button in outgoing messages.
 */
@Suppress("LongMethod")
@Composable
fun ODSChatBubble(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSChatBubbleProps = ODSChatBubbleProps(),
    actionSlot: (@Composable () -> Unit)? = null,
    slotContentIncoming: (@Composable () -> Unit)? = null,
    slotContentOutgoing: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
) {

    val style = ODSChatBubbleStyle().getStyle(scheme = scheme, props = props)

    ODSRow(
        modifier = modifier,
        gap = style.gap,
        clipContent = style.clipContent != false,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement
    ) {
        if (props.variant == ODSChatBubbleVariant.INCOMING) {

            ODSColumn(
                verticalAlignment = style.avatarVerticalAlignment,
                horizontalAlignment = style.avatarHorizontalAlignment,
                verticalArrangement = style.avatarVerticalArrangement,
                minWidth = style.avatarMinWidth,
                minHeight = style.avatarMinHeight
            ) {
                if (props.firstMessage) {
                    props.chatBubbleLeadingElementProps?.let { chatBubbleLeadingElementProps ->
                        ODSChatBubbleLeadingElement(
                            scheme = scheme,
                            props = chatBubbleLeadingElementProps,
                        )
                    }
                }
            }
        }
        ODSColumn(
            verticalAlignment = style.contentContainerVerticalAlignment,
            horizontalAlignment = style.contentContainerHorizontalAlignment,
            verticalArrangement = style.contentContainerVerticalArrangement
        ) {
            ODSRow(
                gap = style.bubbleBadgeGap,
                horizontalAlignment = style.bubbleBadgeHorizontalAlignment,
                verticalAlignment = style.bubbleBadgeVerticalAlignment,
                horizontalArrangement = style.bubbleBadgeHorizontalArrangement
            ) {
                if (props.variant == ODSChatBubbleVariant.OUTGOING && props.showError) {
                    props.chatBubbleLeadingElementProps?.let { chatBubbleLeadingElementProps ->
                        ODSChatBubbleLeadingElement(
                            scheme = scheme,
                            props = chatBubbleLeadingElementProps
                        )
                    }
                }
                ODSBox(
                    clipContent = style.bubbleContainerZStackClipContent != false,
                    contentAlignment = style.bubbleContainerZStackContentAlignment
                ) {
                    if (props.showBackground) {
                        ODSBox(
                            modifier = Modifier.matchParentSize(),
                            cornerRadius = style.bubbleBackgroundCornerRadius,
                            background = style.bubbleBackgroundBackground
                        ) {
                        }
                    }
                    ODSColumn(
                        clipContent = style.bubbleContainerClipContent != false,
                        verticalAlignment = style.bubbleContainerVerticalAlignment,
                        horizontalAlignment = style.bubbleContainerHorizontalAlignment,
                        verticalArrangement = style.bubbleContainerVerticalArrangement,
                        cornerRadius = style.bubbleContainerCornerRadius,
                        maxWidth = style.bubbleContainerMaxWidth
                    ) {
                        when (props.variant) {
                            ODSChatBubbleVariant.INCOMING -> slotContentIncoming?.invoke()
                            ODSChatBubbleVariant.OUTGOING -> slotContentOutgoing?.invoke()
                        }
                    }
                }
            }
            if (props.showFooter) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = style.footerHorizontalAlignment,
                    verticalAlignment = style.footerVerticalAlignment,
                    horizontalArrangement = style.footerHorizontalArrangement
                ) {
                    if (props.showHelperText) {
                        ODSRow(
                            gap = style.helperTextGap,
                            padding = style.helperTextPadding,
                            horizontalAlignment = style.helperTextHorizontalAlignment,
                            verticalAlignment = style.helperTextVerticalAlignment,
                            horizontalArrangement = style.helperTextHorizontalArrangement,
                            height = style.helperTextHeight
                        ) {
                            if (!props.helperText.isNullOrEmpty()) {
                                ODSText(
                                    text = props.helperText,
                                    style = style.textLabelStyle,
                                    color = style.textLabelColor,
                                    textAlign = style.textLabelTextAlign
                                )
                            }
                        }
                    }
                    if (props.variant == ODSChatBubbleVariant.OUTGOING) {
                        props.buttonProps?.let { buttonProps ->
                            ODSButton(
                                scheme = scheme,
                                props = buttonProps.toODSButtonProps(),
                                onClick = onClick
                            )
                        }
                    }
                    if (props.variant == ODSChatBubbleVariant.INCOMING && props.showActions) {
                        actionSlot?.invoke()
                    }
                }
            }
        }
    }
}
