package com.app.screentime.challenge.screen.variant

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.app.screentime.challenge.component.util.CountdownLabel
import com.app.screentime.challenge.component.util.formatCountdown
import com.app.screentime.challenge.component.util.formatDate
import com.app.screentime.challenge.component.util.getCountdownInfo
import com.app.screentime.network.model.Challenge
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.delay

/**
 * Loads the appropriate challenge variant card based on the challenge's variant property.
 * Falls back to default cards if variant is not specified.
 * Uses date format (e.g. "26 Jan 2026") for challenge start/end display.
 * Shows a live countdown when within 24h of start or 24h of end.
 */
@Composable
fun ChallengeVariantCardLoader(
    challenge: Challenge,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val variant = challenge.variant?.lowercase() ?: ""
    val allTags = mutableListOf<String>()
    challenge.tags?.let { allTags.addAll(it) }
    challenge.tag?.let { allTags.add(it) }

    val startDateFormatted = formatDate(challenge.startTime)
    val endDateFormatted = formatDate(challenge.endTime)
    val dateFormatted = "$startDateFormatted - $endDateFormatted"

    val countdownInfo = getCountdownInfo(challenge.startTime, challenge.endTime)
    val countdownLabel = when (countdownInfo?.first) {
        CountdownLabel.STARTS_IN -> stringResource(R.string.starts_in)
        CountdownLabel.ENDS_IN -> stringResource(R.string.ends_in)
        null -> null
    }
    var countdownText by remember(countdownInfo) {
        mutableStateOf(
            if (countdownInfo != null && countdownLabel != null)
                formatCountdown(countdownInfo.second, countdownLabel)
            else
                null
        )
    }
    LaunchedEffect(countdownInfo, countdownLabel) {
        val info = countdownInfo ?: return@LaunchedEffect
        val lbl = countdownLabel ?: return@LaunchedEffect
        while (java.time.Instant.now() < info.second) {
            countdownText = formatCountdown(info.second, lbl)
            delay(1000)
        }
        countdownText = null
    }

    when (variant) {
        "variant1" -> {
            ChallengeVariantCard6(
                title = challenge.title,
                description = challenge.description.takeIf { it.isNotEmpty() },
                startTime = startDateFormatted,
                endTime = endDateFormatted,
                tags = allTags,
                imageUrl = challenge.thumbnail,
                countdownText = countdownText,
                modifier = modifier,
                onClick = onClick,
                scheme = challenge.getTheme()
            )
        }

        "variant2" -> {
            ChallengeVariantCard2(
                title = challenge.title,
                description = challenge.description.takeIf { it.isNotEmpty() },
                date = dateFormatted,
                countdownText = countdownText,
                tag = challenge.tag ?: challenge.tags?.firstOrNull(),
                reward = challenge.reward.takeIf { it.isNotEmpty() },
                imageUrl = challenge.thumbnail,
                modifier = modifier,
                onClick = onClick,
                cardScheme = challenge.getTheme()
            )
        }

        "variant3" -> {
            ChallengeVariantCard3(
                title = challenge.title,
                description = challenge.description.takeIf { it.isNotEmpty() },
                startTime = startDateFormatted,
                endTime = endDateFormatted,
                participantCount = challenge.participantCount,
                reward = challenge.reward.takeIf { it.isNotEmpty() },
                imageUrl = challenge.thumbnail,
                countdownText = countdownText,
                modifier = modifier,
                onClick = onClick,
                scheme = challenge.getTheme()
            )
        }

        "variant4" -> {
            ChallengeVariantCard4(
                tag = challenge.tag ?: "NEW",
                challengeType = challenge.displayType ?: "",
                title = challenge.title,
                description = challenge.description.takeIf { it.isNotEmpty() },
                startTime = startDateFormatted,
                endTime = endDateFormatted,
                participantCount = challenge.participantCount,
                reward = challenge.reward.takeIf { it.isNotEmpty() },
                imageUrl = challenge.thumbnail,
                countdownText = countdownText,
                modifier = modifier,
                onClick = onClick,
                scheme = challenge.getTheme()
            )
        }

        "variant5" -> {
            ChallengeVariantCard5(
                title = challenge.title,
                description = challenge.description.takeIf { it.isNotEmpty() },
                startTime = startDateFormatted,
                endTime = endDateFormatted,
                participantCount = challenge.participantCount,
                reward = challenge.reward.takeIf { it.isNotEmpty() },
                imageUrl = challenge.thumbnail,
                countdownText = countdownText,
                modifier = modifier,
                onClick = onClick,
                scheme = challenge.getTheme()
            )
        }

        "variant6" -> {
            ChallengeVariantCard6(
                title = challenge.title,
                description = challenge.description.takeIf { it.isNotEmpty() },
                startTime = startDateFormatted,
                endTime = endDateFormatted,
                tags = allTags,
                imageUrl = challenge.thumbnail,
                countdownText = countdownText,
                modifier = modifier,
                onClick = onClick,
                scheme = challenge.getTheme()
            )
        }

        else -> {
            when (challenge.displayType) {
                "TRENDING" -> {
                    ChallengeVariantCard6(
                        title = challenge.title,
                        description = challenge.description.takeIf { it.isNotEmpty() },
                        startTime = startDateFormatted,
                        endTime = endDateFormatted,
                        tags = allTags,
                        imageUrl = challenge.thumbnail,
                        countdownText = countdownText,
                        modifier = modifier.fillMaxWidth(0.75f),
                        onClick = onClick,
                        scheme = challenge.getTheme()
                    )
                }

                "SPECIAL" -> {
                    ChallengeVariantCard5(
                        title = challenge.title,
                        description = challenge.description.takeIf { it.isNotEmpty() },
                        startTime = startDateFormatted,
                        endTime = endDateFormatted,
                        participantCount = challenge.participantCount,
                        reward = challenge.reward.takeIf { it.isNotEmpty() },
                        imageUrl = challenge.thumbnail,
                        countdownText = countdownText,
                        modifier = modifier,
                        onClick = onClick,
                        scheme = challenge.getTheme()
                    )
                }

                else -> {
                    ChallengeVariantCard6(
                        title = challenge.title,
                        description = challenge.description.takeIf { it.isNotEmpty() },
                        startTime = startDateFormatted,
                        endTime = endDateFormatted,
                        tags = allTags,
                        imageUrl = challenge.thumbnail,
                        countdownText = countdownText,
                        modifier = modifier,
                        onClick = onClick,
                        scheme = challenge.getTheme()
                    )
                }
            }
        }
    }
}

