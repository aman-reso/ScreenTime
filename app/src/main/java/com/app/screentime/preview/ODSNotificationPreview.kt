package com.app.screentime.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotification
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotificationProps
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps

@Preview(showBackground = true)
@Composable
fun ODSNotificationPreview() {
    ODSBox(
        modifier = Modifier,
        background = listOf(ODSColorModel(neutralScheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(DSVariables.spacingComponent5),
            gap = DSVariables.spacingComponent4
        ) {
            // Inline Notification - Success
            ODSInlineNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSInlineNotificationProps(
                    mode = ODSInlineNotificationMode.SUCCESS,
                    title = "Success Notification",
                    text = "Your action was completed successfully.",
                    showCloseButton = true
                ),
                onDismiss = {}
            )

            // Inline Notification - Success without close button
            ODSInlineNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSInlineNotificationProps(
                    mode = ODSInlineNotificationMode.SUCCESS,
                    title = "Success without Close",
                    text = "This notification cannot be dismissed.",
                    showCloseButton = false
                ),
                onDismiss = {}
            )

            // Inline Notification - Success with link
            ODSInlineNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSInlineNotificationProps(
                    mode = ODSInlineNotificationMode.SUCCESS,
                    title = "Success with Link",
                    text = "Your changes have been saved.",
                    link1Props = ODSLinkProps(label = "View Details"),
                    showCloseButton = true
                ),
                onFirstLinkClicked = {},
                onDismiss = {}
            )

            // Inline Notification - Success with two links
            ODSInlineNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSInlineNotificationProps(
                    mode = ODSInlineNotificationMode.SUCCESS,
                    title = "Success with Multiple Links",
                    text = "Operation completed successfully.",
                    link1Props = ODSLinkProps(label = "View"),
                    link2Props = ODSLinkProps(label = "Learn More"),
                    showCloseButton = true
                ),
                onFirstLinkClicked = {},
                onSecondLinkClicked = {},
                onDismiss = {}
            )

            // Inline Notification - Error
            ODSInlineNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSInlineNotificationProps(
                    mode = ODSInlineNotificationMode.ERROR,
                    title = "Error Notification",
                    text = "Something went wrong. Please try again.",
                    showCloseButton = true
                ),
                onDismiss = {}
            )

            // Inline Notification - Error with link
            ODSInlineNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSInlineNotificationProps(
                    mode = ODSInlineNotificationMode.ERROR,
                    title = "Error with Action",
                    text = "Failed to save your changes.",
                    link1Props = ODSLinkProps(label = "Retry"),
                    showCloseButton = true
                ),
                onFirstLinkClicked = {},
                onDismiss = {}
            )

            // Inline Notification - Warning
            ODSInlineNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSInlineNotificationProps(
                    mode = ODSInlineNotificationMode.WARNING,
                    title = "Warning Notification",
                    text = "Please review your settings before continuing.",
                    showCloseButton = true
                ),
                onDismiss = {}
            )

            // Inline Notification - Warning with link
            ODSInlineNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSInlineNotificationProps(
                    mode = ODSInlineNotificationMode.WARNING,
                    title = "Warning with Action",
                    text = "Your session will expire soon.",
                    link1Props = ODSLinkProps(label = "Extend Session"),
                    showCloseButton = true
                ),
                onFirstLinkClicked = {},
                onDismiss = {}
            )

            // Inline Notification - Informative
            ODSInlineNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSInlineNotificationProps(
                    mode = ODSInlineNotificationMode.INFORMATIVE,
                    title = "Informative Notification",
                    text = "New features are available in the latest update.",
                    showCloseButton = true
                ),
                onDismiss = {}
            )

            // Inline Notification - Informative with link
            ODSInlineNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSInlineNotificationProps(
                    mode = ODSInlineNotificationMode.INFORMATIVE,
                    title = "Informative with Link",
                    text = "Check out what's new in version 2.0.",
                    link1Props = ODSLinkProps(label = "Learn More"),
                    showCloseButton = true
                ),
                onFirstLinkClicked = {},
                onDismiss = {}
            )

            // Inline Notification - Text only (no title)
            ODSInlineNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSInlineNotificationProps(
                    mode = ODSInlineNotificationMode.SUCCESS,
                    text = "This is a notification with only text, no title.",
                    showCloseButton = true
                ),
                onDismiss = {}
            )

            // Inline Notification - Title only (no text)
            ODSInlineNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSInlineNotificationProps(
                    mode = ODSInlineNotificationMode.INFORMATIVE,
                    title = "Notification with only title",
                    showCloseButton = true
                ),
                onDismiss = {}
            )

            // Card Notification - Basic
            ODSCardNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardNotificationProps(
                    title = "Card Notification",
                    text = "This is a card notification with title and text.",
                    showCloseButton = true
                ),
                onClick = {},
                onDismiss = {}
            )

            // Card Notification - Without close button
            ODSCardNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardNotificationProps(
                    title = "Card Notification",
                    text = "This card notification cannot be dismissed.",
                    showCloseButton = false
                ),
                onClick = {},
                onDismiss = {}
            )

            // Card Notification - Text only
            ODSCardNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardNotificationProps(
                    text = "This is a card notification with only text, no title.",
                    showCloseButton = true
                ),
                onClick = {},
                onDismiss = {}
            )

            // Card Notification - Title only
            ODSCardNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardNotificationProps(
                    title = "Card notification with only title",
                    showCloseButton = true
                ),
                onClick = {},
                onDismiss = {}
            )

            // Card Notification - With action slot
            ODSCardNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardNotificationProps(
                    title = "Card with Action",
                    text = "This card notification has a custom action slot.",
                    showCloseButton = true
                ),
                onClick = {},
                onDismiss = {},
                actionSlot = {
                    // Custom action content can be added here
                }
            )

            // Card Notification - Long text
            ODSCardNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardNotificationProps(
                    title = "Card with Long Text",
                    text = "This is a card notification with a very long text that demonstrates how the component handles longer content that might wrap to multiple lines and shows the proper text wrapping behavior.",
                    showCloseButton = true
                ),
                onClick = {},
                onDismiss = {}
            )
        }
    }
}

