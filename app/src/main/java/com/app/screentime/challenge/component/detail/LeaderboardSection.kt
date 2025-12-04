package com.app.screentime.challenge.component.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.screentime.network.model.ChallengeRanking
import com.app.screentime.utils.DateUtils
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Leaderboard section.
 */
@Composable
fun LeaderboardSection(
    rankings: List<ChallengeRanking>,
    userRank: ChallengeRanking?,
    scheme: ODSTheme = neutralScheme
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        padding = ODSPadding(horizontal = DSVariables.spacingComponent5),
        gap = DSVariables.spacingComponent3
    ) {
        ODSText(
            text = "Leaderboard",
            style = DSTextStyles.subtitle,
            color = scheme.basicText
        )

        // User Rank (if available) - using scheme to highlight
        userRank?.let { rank ->
            val durationText = DateUtils.formatDuration(rank.totalDuration)

            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.STANDARD,
                    labelText = "Your Rank",
                    label = "#${rank.rank} - ${rank.userId}",
                    descriptionText = "$durationText • Out of ${rankings.size} participants"
                )
            )
        }

        // Top Rankings
        rankings.take(10).forEach { ranking ->
            // Format duration from milliseconds using DateUtils
            val durationText = DateUtils.formatDuration(ranking.totalDuration)

            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.STANDARD,
                    labelText = "#${ranking.rank}",
                    label = ranking.userId,
                    descriptionText = durationText
                )
            )
        }
    }
}

