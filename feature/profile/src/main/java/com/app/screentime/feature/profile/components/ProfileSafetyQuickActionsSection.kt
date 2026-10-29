package com.app.screentime.feature.profile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R as ConfigR
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickAction
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickActionProps
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickActionSize
import com.telekom.odsystem.slots.quickactioncardpreferredcontent.ODSQuickActionCardPreferredContent
import com.telekom.odsystem.slots.quickactioncardpreferredcontent.ODSQuickActionCardPreferredContentProps
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Moderation and Safety quick action cards for user profile (Block and Report).
 */
@Composable
fun ProfileSafetyQuickActionsSection(
    userName: String,
    scheme: ODSTheme,
    onBlockClick: () -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val blockTitle = stringResource(ConfigR.string.profile_block_title, userName)
    val reportTitle = stringResource(ConfigR.string.profile_report_title, userName)

    ODSColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ODSVariables.spacingLayout1),
        gap = ODSVariables.spacingComponent3
    ) {
        // Block Profile Quick Action Card
        ODSCardQuickAction(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSCardQuickActionProps(
                size = ODSCardQuickActionSize.SMALL,
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_close,
                    contentDescription = blockTitle
                )
            ),
            onClick = onBlockClick,
            contentSlot = {
                ODSQuickActionCardPreferredContent(
                    scheme = scheme,
                    props = ODSQuickActionCardPreferredContentProps(
                        title = blockTitle,
                        showTags = false
                    )
                )
            }
        )

        // Report Profile Quick Action Card
        ODSCardQuickAction(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSCardQuickActionProps(
                size = ODSCardQuickActionSize.SMALL,
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_alert_triangle,
                    contentDescription = reportTitle
                )
            ),
            onClick = onReportClick,
            contentSlot = {
                ODSQuickActionCardPreferredContent(
                    scheme = scheme,
                    props = ODSQuickActionCardPreferredContentProps(
                        title = reportTitle,
                        showTags = false
                    )
                )
            }
        )
    }
}
