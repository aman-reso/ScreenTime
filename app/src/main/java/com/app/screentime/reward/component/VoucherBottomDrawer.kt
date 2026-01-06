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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Voucher Bottom Drawer Component
 * Displays voucher details in a bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoucherBottomDrawer(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    offerName: String = "Caramel Frappucino",
    offerSize: String = "Large",
    expiryDate: String = "Ends on 31 Dec 2022",
    description: String = "Enjoy a delicious Caramel Frappucino at Starbucks",
    scheme: ODSTheme = neutralScheme
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ODSBottomSheet(
        scheme = scheme,
        props = ODSBottomSheetProps(),
        showBottomSheet = showBottomSheet,
        bottomSheetState = bottomSheetState,
        onDismissRequest = onDismiss,
        onCloseClicked = onDismiss,
        titleSlot = {
            ODSText(
                text = "Voucher Details",
                style = com.telekom.odsystem.DSTextStyles.titleL,
                color = scheme.basicText
            )
        },
        contentSlot = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DSVariables.spacingComponent4),
                verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3)
            ) {
                // Logo and Offer Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo
                    Box(
                        modifier = Modifier
                            .size(80.dp)
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
                        modifier = Modifier.weight(1f)
                    ) {
                        ODSText(
                            text = offerName,
                            style = com.telekom.odsystem.DSTextStyles.titleM,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = offerSize,
                            style = com.telekom.odsystem.DSTextStyles.bodyMRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }

                Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))

                // Description
                ODSText(
                    text = description,
                    style = com.telekom.odsystem.DSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )

                Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))

                // Expiry Date
                ODSText(
                    text = expiryDate,
                    style = com.telekom.odsystem.DSTextStyles.bodySRegular,
                    color = scheme.basicTextRecessive
                )
            }
        },
        actionSlot = {
            // Claim button can be added here if needed
        }
    )
}


















