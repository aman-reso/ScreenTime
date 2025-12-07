package com.app.screentime.reward.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor

/**
 * Total Earned Points Card
 * Displays total earned points and equivalent rupees
 */
@Composable
fun TotalEarnedPointsCard(
    points: Int = 3280,
    rupees: Int = 3280,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(DSVariables.spacingComponent4),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        ODSText(
            text = "TOTAL EARNED POINTS",
            style = com.telekom.odsystem.DSTextStyles.bodyMBold,
            color = HexColor("FFFFFF")
        )

        Spacer(modifier = Modifier.height(DSVariables.spacingComponent4))

        // Points and Rupees
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
        ) {
            // Left Column - Points
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                ODSText(
                    text = points.toString(),
                    style = com.telekom.odsystem.DSTextStyles.titleL,
                    color = HexColor("000000")
                )
                ODSText(
                    text = "POINTS",
                    style = com.telekom.odsystem.DSTextStyles.bodyMBold,
                    color = HexColor("000000")
                )
            }

            // Right Column - Rupees
            Column(
                horizontalAlignment = Alignment.End
            ) {
                ODSText(
                    text = rupees.toString(),
                    style = com.telekom.odsystem.DSTextStyles.titleL,
                    color = HexColor("000000")
                )
                ODSText(
                    text = "RUPEES",
                    style = com.telekom.odsystem.DSTextStyles.bodyMBold,
                    color = HexColor("000000")
                )
            }
        }
    }
}



