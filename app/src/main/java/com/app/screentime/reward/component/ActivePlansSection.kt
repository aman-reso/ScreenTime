package com.app.screentime.reward.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row

/**
 * Active Plans Section Component
 * Displays section title and horizontally scrollable plan cards
 */
@Composable
fun ActivePlansSection(
    planCount: Int = 4,
    onPlanClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(DSVariables.spacingComponent4)
    ) {
        // Section title
        ODSText(
            text = "My Active Plans",
            style = com.telekom.odsystem.DSTextStyles.titleL,
            color = HexColor("000000")
        )

        Spacer(modifier = Modifier.height(DSVariables.spacingComponent1))

        // Subtitle
        ODSText(
            text = "You have $planCount active plans",
            style = com.telekom.odsystem.DSTextStyles.bodyMRegular,
            color = HexColor("666666")
        )

        Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))

        // Horizontally scrollable plan cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3)
        ) {
            // Plan cards
            ActivePlanCard(
                doctorName = "Dr. Alan Hathaway",
                specialty = "Cardiovascular",
                currentVisits = 45,
                totalVisits = 100,
                onClick = { onPlanClick(0) }
            )
            ActivePlanCard(
                doctorName = "Dr. Ruben Levin",
                specialty = "Cardiovascular",
                currentVisits = 23,
                totalVisits = 50,
                onClick = { onPlanClick(1) }
            )
            ActivePlanCard(
                doctorName = "Dr. Sarah Johnson",
                specialty = "Neurology",
                currentVisits = 12,
                totalVisits = 30,
                onClick = { onPlanClick(2) }
            )
            ActivePlanCard(
                doctorName = "Dr. Michael Chen",
                specialty = "Orthopedics",
                currentVisits = 8,
                totalVisits = 20,
                onClick = { onPlanClick(3) }
            )
        }
    }
}

