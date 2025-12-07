package com.app.screentime.reward.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor
import com.app.screentime.reward.shape.WavyShape

/**
 * Voucher Card Component
 * Displays a voucher with logo, offer details, and claim button
 */
@Composable
fun VoucherCard(
    logoUrl: String? = null,
    logoRes: Int? = null,
    offerName: String = "Caramel Frappucino",
    offerSize: String = "Large",
    expiryDate: String = "Ends on 31 Dec 2022",
    onClaimClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(WavyShape(amplitude = 5f, frequency = 0.8f))
            .background(Color.White)
            .clickable(onClick = onClaimClick)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(DSVariables.spacingComponent3),
            horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00704A)), // Starbucks green
                contentAlignment = Alignment.Center
            ) {
                ODSText(
                    text = "S",
                    style = com.telekom.odsystem.DSTextStyles.titleL,
                    color = HexColor("FFFFFF")
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Offer Name
                    ODSText(
                        text = offerName,
                        style = com.telekom.odsystem.DSTextStyles.bodyMBold,
                        color = HexColor("000000")
                    )
                    // Offer Size
                    ODSText(
                        text = offerSize,
                        style = com.telekom.odsystem.DSTextStyles.bodyMRegular,
                        color = HexColor("000000")
                    )
                }

                Column {
                    // Expiry Date
                    ODSText(
                        text = expiryDate,
                        style = com.telekom.odsystem.DSTextStyles.bodySRegular,
                        color = HexColor("666666")
                    )
                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent1))
                    // Claim Voucher Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFF6B35)) // Orange color
                            .clickable(onClick = onClaimClick)
                            .padding(
                                horizontal = DSVariables.spacingComponent3,
                                vertical = DSVariables.spacingComponent1
                            )
                    ) {
                        ODSText(
                            text = "Claim Voucher",
                            style = com.telekom.odsystem.DSTextStyles.bodySRegular,
                            color = HexColor("FFFFFF")
                        )
                    }
                }
            }
        }
    }
}

