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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.R
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

/**
 * Points Header Component
 * Displays points total, status, and progress indicator
 */
@Composable
fun PointsHeader(
    points: Int? = null,
    modifier: Modifier = Modifier,
    onInfoClick: () -> Unit = {},
    onOrderHistoryClick: () -> Unit = {},
    onCoinHistoryClick: () -> Unit = {},
    scheme: ODSTheme
) {
    var showMenu by remember { mutableStateOf(false) }

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent2
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ODSRow(
            padding = ODSPadding(vertical = DSVariables.spacingComponent4),
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
                        drawableRes = R.drawable.information_type_standard,
                        tint = scheme.basicTextRecessive,
                        contentDescription = "Info"
                    ),
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        ODSText(
            text = "Points Available",
            style = DSTextStyles.oxBodyMRegular,
            color = scheme.basicText,
            modifier = Modifier.wrapContentWidth()
        )
        Spacer(modifier = Modifier.height(DSVariables.spacingComponent5))

        ODSBox(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            PointsProgressBar(
                currentLevel = 3,
                progressInLevel = 0.3f,
                scheme = scheme
            )
        }

        ExpiringPointsBanner(
            expiringPoints = 20,
            onUseClick = {
                // Handle use click
            },
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme
        )
    }
}

/**
 * Progress bar with 5 segments
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

        ODSBox(modifier = Modifier.wrapContentWidth()) {
            ODSRow(
                modifier = Modifier.width(totalBarWidth),
                gap = 4.dp, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(5) { index ->
                    val isFilled = index < currentLevel
                    val isPartiallyFilled = index == currentLevel && progressInLevel > 0f
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
                                    .fillMaxWidth(progressInLevel)
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
