package com.app.screentime.challenge.component.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Error state component using ODSInlineNotification.
 */
@Composable
fun ChallengeErrorState(
    message: String,
    onRetry: () -> Unit,
    scheme: ODSTheme = neutralScheme
) {
    ODSColumn(
        modifier = Modifier.fillMaxSize(),
        padding = ODSPadding(all = DSVariables.spacingComponent4)
    ) {
        ODSInlineNotification(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSInlineNotificationProps(
                mode = ODSInlineNotificationMode.ERROR,
                title = stringResource(R.string.error),
                text = message,
                link1Props = ODSLinkProps(
                    label = stringResource(R.string.retry)
                ),
                showCloseButton = false
            ),
            onFirstLinkClicked = onRetry
        )
    }
}

