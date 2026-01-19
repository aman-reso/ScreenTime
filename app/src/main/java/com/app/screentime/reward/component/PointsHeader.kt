package com.app.screentime.reward.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.R as ODSR
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.app.screentime.reward.util.CoinLevelCalculator
import com.app.screentime.reward.util.ExpiringCoinsCalculator
import com.app.screentime.reward.model.CoinHistoryItem
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.invertedScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R

/**
 * Points Header Component
 * Displays points total, status, and progress indicator
 */
@Composable
fun PointsHeader(
    modifier: Modifier = Modifier,
    points: Int? = null,
    coinHistory: List<CoinHistoryItem> = emptyList(),
    onInfoClick: () -> Unit = {},
    scheme: ODSTheme,
    showWatchAdSection: Boolean = true,
    isAdLoading: Boolean = false,
    adError: String? = null,
    onWatchAdClick: () -> Unit = {}
) {
    val totalCoins = points ?: 0
    val currentLevel = CoinLevelCalculator.calculateLevel(totalCoins)
    val progressInLevel = CoinLevelCalculator.calculateProgressInLevel(totalCoins)
    val expiringCoins = ExpiringCoinsCalculator.getNearestExpiringCoins(coinHistory) ?: 0

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent2
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ODSRow(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            gap = DSVariables.spacingComponent2
        ) {
            ODSText(
                text = points?.toString()?.replace(Regex("(\\d)(?=(\\d{3})+(?!\\d))"), "$1,")
                    ?: "---",
                style = DSTextStyles.oxDisplayL,
                color = scheme.basicText
            )
            ODSBox(
                modifier = Modifier
                    .size(20.dp)
                    .customClickable(
                        onClick = onInfoClick,
                        isPressed = {}
                    ),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = ODSR.drawable.information_type_standard,
                        tint = scheme.basicTextRecessive,
                        contentDescription = stringResource(R.string.info)
                    ),
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        ODSText(
            text = stringResource(R.string.available_coin),
            style = DSTextStyles.bodyMBold,
            color = scheme.basicText,
            modifier = Modifier.wrapContentWidth()
        )

        Spacer(modifier = Modifier.height(DSVariables.spacingComponent5))

        ODSBox(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            PointsProgressBar(
                currentLevel = currentLevel,
                progressInLevel = progressInLevel,
                scheme = scheme
            )
        }

        if (expiringCoins > 0) {
            ExpiringPointsBanner(
                expiringPoints = expiringCoins,
                onUseClick = {
                    // Handle use click - navigate to coin history or rewards
                },
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme
            )
        }

        if (showWatchAdSection) {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent5))
            WatchAdSection(
                isAdLoading = isAdLoading,
                adError = adError,
                onWatchAdClick = onWatchAdClick,
                scheme = invertedScheme,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(DSVariables.spacingComponent4))
    }
}

/**
 * Progress bar with 5 segments
 * Maps 10 levels to 5 visual segments (2 levels per segment)
 */
@Composable
private fun PointsProgressBar(
    currentLevel: Int = 3,
    progressInLevel: Float = 0.3f,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        val totalBarWidth = maxWidth * 0.6f

        // Map 10 levels to 5 segments (2 levels per segment)
        // Segment 0: Levels 1-2, Segment 1: Levels 3-4, etc.
        val segmentIndex = (currentLevel - 1) / 2
        val isInSecondHalfOfSegment = currentLevel % 2 == 0
        val progressInSegment = if (isInSecondHalfOfSegment) {
            // Even level means we're in the second half of the segment (level 2, 4, 6, 8, 10)
            // First half (0.5) is already complete, add progress in second half
            0.5f + (progressInLevel / 2f)
        } else {
            // Odd level means we're in the first half of the segment (level 1, 3, 5, 7, 9)
            progressInLevel / 2f
        }

        ODSBox(modifier = Modifier.wrapContentWidth()) {
            ODSRow(
                modifier = Modifier.width(totalBarWidth),
                gap = 4.dp, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(5) { index ->
                    val isFilled = index < segmentIndex
                    val isPartiallyFilled = index == segmentIndex && progressInSegment > 0f
                    ODSBox(
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp),
                        background = listOf(
                            ODSColorModel(
                                if (isFilled) scheme.basicAccent
                                else scheme.basicStrokeSubtle
                            )
                        ),
                        cornerRadius = ODSCorners(all = 4.dp)
                    ) {
                        if (isPartiallyFilled) {
                            ODSBox(
                                modifier = Modifier
                                    .fillMaxWidth(progressInSegment)
                                    .height(8.dp),
                                background = listOf(ODSColorModel(scheme.basicAccent)),
                                cornerRadius = ODSCorners(all = 4.dp)
                            ) {}
                        }
                    }
                }
            }
        }
    }
}

/**
 * Watch Ad Section Component
 * Displays a card with title, subtitle, and watch ad button
 */
@Composable
private fun WatchAdSection(
    isAdLoading: Boolean,
    adError: String?,
    onWatchAdClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSCardBasic(
        modifier = modifier,
        scheme = scheme,
        contentPadding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent3
        ),
        props = ODSCardBasicProps(),
        contentSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = DSVariables.spacingComponent1
                ) {
                    ODSText(
                        text = stringResource(R.string.watch_ad_to_earn_coins),
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                    ODSText(
                        text = adError ?: stringResource(R.string.watch_ad_subtitle),
                        style = DSTextStyles.bodySRegular,
                        color = if (adError != null) {
                            scheme.functionalDestructiveStandard
                        } else {
                            scheme.basicTextRecessive
                        }
                    )
                }

                if (isAdLoading) {
                    ODSLoadingSpinner(
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(
                            size = ODSLoadingSpinnerSize.SMALL,
                            variant = ODSLoadingSpinnerVariant.STANDARD
                        )
                    )
                } else {
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = stringResource(R.string.watch_ad),
                            variant = ODSButtonVariant.OUTLINE,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = onWatchAdClick
                    )
                }
            }
        }
    )
}
