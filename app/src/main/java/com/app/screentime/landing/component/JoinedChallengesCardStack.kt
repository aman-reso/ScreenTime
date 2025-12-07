package com.app.screentime.landing.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.app.screentime.R
import com.app.screentime.navigation.Screen
import com.app.screentime.network.model.UserChallenge
import com.telekom.odsystem.atoms.link.ODSLink
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotificationProps
import com.telekom.odsystem.organisms.cardnotificationstack.ODSCardNotificationStack
import com.telekom.odsystem.organisms.cardnotificationstack.ODSCardNotificationStackLinkAlignment
import com.telekom.odsystem.organisms.cardnotificationstack.ODSCardNotificationStackProps
import com.telekom.odsystem.organisms.cardnotificationstack.ODSCardNotificationStackViewAllButtonProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * CardStack notification component for displaying joined challenges
 * Shows up to 2 challenges in a stack with "View All" option
 */
@Composable
fun JoinedChallengesCardStack(
    joinedChallenges: List<UserChallenge>,
    modifier: Modifier = Modifier,
    onNavigateToChallengeDetail: (String) -> Unit = {},
    onNavigateToChallenges: () -> Unit = {},
    scheme: ODSTheme = neutralScheme,
    onDismiss: () -> Unit = {}
) {
    if (joinedChallenges.isEmpty()) {
        return
    }

    val firstChallenge = joinedChallenges.firstOrNull()
    val showSecondCard = joinedChallenges.size > 1
    val showThirdCard = joinedChallenges.size > 2

    firstChallenge?.let { challenge ->
        ODSCardNotificationStack(
            modifier = modifier,
            scheme = scheme,
            props = ODSCardNotificationStackProps(
                show2ndCard = showSecondCard,
                show3rdCard = showThirdCard,
                linkAlignment = ODSCardNotificationStackLinkAlignment.CENTERED,
                notificationCardProps = ODSCardNotificationProps(
                    title = challenge.title,
                    text = challenge.description,
                    showCloseButton = false
                ),
                viewAllButtonProps = ODSCardNotificationStackViewAllButtonProps(
                    buttonLabel = stringResource(R.string.view_all_challenges)
                )
            ),
            onClick = {
                onNavigateToChallengeDetail(challenge.id)
            },
            onDismiss = onDismiss,
            onViewAllClick = onNavigateToChallenges,
            actionSlot = {
                ODSLink(
                    scheme = scheme,
                    props = ODSLinkProps(
                        label = stringResource(R.string.see_detail)
                    ),
                    onClick = {
                        onNavigateToChallengeDetail(challenge.id)
                    }
                )
            }
        )
    }
}

