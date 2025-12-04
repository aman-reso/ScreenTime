package com.app.screentime.permission.component.infocard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotification
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotificationProps
import com.telekom.odsystem.slots.cardcontentbasic.ODSCardContentBasic
import com.telekom.odsystem.slots.cardcontentbasic.ODSCardContentBasicProps
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
    val tokens = defaultInfoCardTokens

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

private val infoCardList = listOf(
    ODSCardNotificationProps(
        showCloseButton = false,
        title = "Why We Need App Usage Data",
        text = "To provide you with accurate insights into your digital habits, ScreenTime requires access to your app usage statistics. This permission allows us to track which applications you use and how much time you spend on each one."
    ),
    ODSCardNotificationProps(
        showCloseButton = false,
        title = "Why This Permission is Essential",
        text = "Without this permission, we cannot accurately measure your screen time, identify your most-used apps, or provide meaningful insights about your digital wellness. This data is the foundation of all features in ScreenTime."
    ),
    ODSCardNotificationProps(
        showCloseButton = false,
        title = "What We Track",
        text = "We only track essential information: app names, usage duration, and timestamps. We do NOT access your personal data, messages, passwords, or any sensitive information within apps. Your privacy is our top priority."
    ),
    ODSCardNotificationProps(
        showCloseButton = false,
        title = "Your Privacy is Protected",
        text = "All usage data is stored locally on your device and is only used to generate your personal screen time reports. We never share your data with third parties."
    )
)
