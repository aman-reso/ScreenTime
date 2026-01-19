package com.app.screentime.permission.component.infocard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotification
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * InfoCardList component that renders multiple InfoCards with proper spacing.
 *
 * @param modifier Modifier to be applied to the component.
 * @param scheme ODS theme scheme for styling.
 * @param propsList List of InfoCardProps to render.
 */
@Composable
fun InfoCardList(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme
) {
    val infoCardList = getInfoCardList()
    
    ODSColumn(
        modifier = modifier,
        gap = DSVariables.spacingComponent5
    ) {
        infoCardList.forEach { props ->
            ODSCardNotification(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = props
            )
        }
    }
}

@Composable
private fun getInfoCardList(): List<ODSCardNotificationProps> {
    return listOf(
        ODSCardNotificationProps(
            showCloseButton = false,
            title = stringResource(R.string.permission_explanation_title),
            text = stringResource(R.string.permission_explanation_detail)
        ),
        ODSCardNotificationProps(
            showCloseButton = false,
            title = stringResource(R.string.permission_why_required),
            text = stringResource(R.string.permission_why_required_detail)
        ),
        ODSCardNotificationProps(
            showCloseButton = false,
            title = stringResource(R.string.permission_what_we_track),
            text = stringResource(R.string.permission_what_we_track_detail)
        ),
        ODSCardNotificationProps(
            showCloseButton = false,
            title = stringResource(R.string.permission_privacy_assurance),
            text = stringResource(R.string.permission_privacy_assurance_detail)
        )
    )
}
