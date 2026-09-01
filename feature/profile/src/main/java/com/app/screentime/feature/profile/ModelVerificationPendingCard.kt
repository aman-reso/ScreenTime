package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ModelVerificationPendingCard(
    scheme: ODSTheme,
    onClickComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Prominent Yellow / Cyber Gold Card for Pending Creator Verification
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable(onClick = onClickComplete),
        background = listOf(ODSColorModel(hexColor = HexColor(0xFFFBBF24))),
        cornerRadius = ODSCorners(all = 12.dp),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = HexColor(0xFFF59E0B)))
        ),
        padding = ODSPadding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 10.dp
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 10.dp,
                    modifier = Modifier.weight(1f)
                ) {
                    ODSBox(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                        background = listOf(ODSColorModel(hexColor = HexColor(0xFF78350F))),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(imageVector = Icons.Outlined.WarningAmber),
                            tint = HexColor(0xFFFDE68A).getColor()
                        )
                    }

                    ODSColumn(gap = 2.dp) {
                        ODSText(
                            text = "Creator Verification Pending",
                            style = ODSTextStyles.bodySBold,
                            color = HexColor(0xFF1E1B18)
                        )
                        ODSText(
                            text = "Fill details to activate your calls & earnings",
                            style = ODSTextStyles.microcopyRegular,
                            color = HexColor(0xFF451A03)
                        )
                    }
                }

                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Complete",
                        variant = ODSButtonVariant.PRIMARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onClickComplete
                )
            }
        }
    }
}
