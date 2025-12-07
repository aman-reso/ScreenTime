package com.app.screentime.reward.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor

/**
 * Reward Points Header Component
 * Displays points balance, level, and progress bar
 */
@Composable
fun RewardPointsHeader(
    points: Int = 3222,
    level: Int = 1,
    progress: Float = 0.3f, // Progress from 0.0 to 1.0
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(
                horizontal = DSVariables.spacingComponent4,
                vertical = DSVariables.spacingComponent6
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            ODSText(
                text = "My Rewards Points",
                style = com.telekom.odsystem.DSTextStyles.titleL,
                color = HexColor("FFFFFF")
            )

            Spacer(modifier = Modifier.height(DSVariables.spacingComponent1))

            // Subtitle
            ODSText(
                text = "Earned points",
                style = com.telekom.odsystem.DSTextStyles.bodyMRegular,
                color = HexColor("FFFFFF")
            )

            Spacer(modifier = Modifier.height(DSVariables.spacingComponent4))

            // Points Balance - First digit in teal, rest in white
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                // First digit in teal
                ODSText(
                    text = points.toString().take(1),
                    style = com.telekom.odsystem.DSTextStyles.display,
                    color = HexColor("00D4AA") // Teal color
                )
                // Rest of digits in white
                ODSText(
                    text = points.toString().drop(1),
                    style = com.telekom.odsystem.DSTextStyles.display,
                    color = HexColor("FFFFFF")
                )
            }

            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))

            // Level Indicator
            ODSText(
                text = "Level $level",
                style = com.telekom.odsystem.DSTextStyles.bodyMRegular,
                color = HexColor("FFFFFF")
            )

            Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))

            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF333333)) // Dark grey background
            ) {
                // Progress fill
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF00D4AA)) // Teal-green fill
                )
            }
        }
    }
}

