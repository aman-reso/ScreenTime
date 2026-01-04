package com.app.screentime.challenge.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.screentime.network.model.Challenge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType
import com.telekom.odsystem.organisms.cardquickactiondeprecated.ODSCardQuickActionDeprecated
import com.telekom.odsystem.organisms.cardquickactiondeprecated.ODSCardQuickActionDeprecatedProps
import com.telekom.odsystem.organisms.cardquickactiondeprecated.ODSCardQuickActionDeprecatedVariant
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Quick join card component for displaying challenges with a simple join action.
 * Uses ODSCardQuickActionDeprecated for consistent styling and navigation indication.
 *
 * @param challenge The challenge to display
 * @param modifier Modifier to be applied to the component
 * @param onJoin Callback when join button is clicked
 * @param scheme ODS theme scheme
 */
@Composable
fun QuickJoinCard(
    challenge: Challenge,
    modifier: Modifier = Modifier,
    onJoin: () -> Unit,
    scheme: ODSTheme
) {
    ODSCardQuickActionDeprecated(
        modifier = modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardQuickActionDeprecatedProps(
            logo = if (!challenge.thumbnail.isNullOrEmpty()) {
                ODSImageModel(url = challenge.thumbnail)
            } else null,
            variant = ODSCardQuickActionDeprecatedVariant.TITLE,
            title = challenge.title,
            subtitle = challenge.description,
            tag1Props = if (challenge.reward.isNotEmpty()) {
                ODSTagStaticProps(
                    label = challenge.reward,
                    icon = ODSIconModel(
                        imageVector = Icons.Default.EmojiEvents,
                        tint = scheme.basicAccent,
                        contentDescription = null
                    ),
                    type = ODSTagStaticType.STRONG
                )
            } else null
        ),
        onClick = onJoin
    )
}

