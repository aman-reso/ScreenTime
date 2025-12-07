package com.app.screentime.reward.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor

/**
 * Active Plan Card Component
 * Displays a plan card with doctor info and progress
 */
@Composable
fun ActivePlanCard(
    doctorName: String = "Dr. Alan Hathaway",
    specialty: String = "Cardiovascular",
    currentVisits: Int = 45,
    totalVisits: Int = 100,
    profileImageUrl: String? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val progress = currentVisits.toFloat() / totalVisits.toFloat()

    Box(
        modifier = modifier
            .width(280.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF9B59B6), // Purple
                        Color(0xFF8E44AD) // Darker purple
                    )
                )
            )
            .clickable(onClick = onClick)
            .padding(DSVariables.spacingComponent3)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section - Profile and arrow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile picture placeholder
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = doctorName.take(1),
                        style = com.telekom.odsystem.DSTextStyles.titleM,
                        color = HexColor("FFFFFF")
                    )
                }

                // Arrow icon
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = Icons.Default.TrendingUp,
                        tint = HexColor("FFFFFF"),
                        contentDescription = "View plan"
                    )
                )
            }

            // Bottom section - Name, specialty, and progress
            Column {
                ODSText(
                    text = doctorName,
                    style = com.telekom.odsystem.DSTextStyles.bodyMBold,
                    color = HexColor("FFFFFF")
                )
                ODSText(
                    text = specialty,
                    style = com.telekom.odsystem.DSTextStyles.bodySRegular,
                    color = HexColor("FFFFFF", alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))

                // Progress section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSText(
                        text = "Total Visits",
                        style = com.telekom.odsystem.DSTextStyles.bodySRegular,
                        color = HexColor("FFFFFF", alpha = 0.9f)
                    )
                    ODSText(
                        text = "$currentVisits/$totalVisits",
                        style = com.telekom.odsystem.DSTextStyles.bodyMBold,
                        color = HexColor("FFFFFF")
                    )
                }

                Spacer(modifier = Modifier.height(DSVariables.spacingComponent1))

                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White)
                    )
                }
            }
        }
    }
}

