package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Zona Wallet Card Component for "My Account" Screen (Figma node-id 10-1163).
 *
 * Rules:
 * 1. 100% ODS components.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Max text size 16sp (bodyMBold).
 * 4. Padding and margins from `ODSVariables`.
 */
@Composable
fun AccountWalletCard(
    balanceFormatted: String = "$24.50",
    creditsCount: Int = 120,
    scheme: ODSTheme = zonaODSTheme,
    onAddFundsClick: () -> Unit = {},
    onTransactionsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ODSVariables.spacingLayout1),
        gap = ODSVariables.spacingLayout1,
        padding = ODSPadding(all = ODSVariables.spacingLayout1),
        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
        border = ODSBorder(
            width = ODSVariables.strokes1,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        ),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
    ) {
        // ── 1. Header: Wallet Icon + "Zona Wallet" | "Refreshes weekly" ─────
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSRow(
                gap = ODSVariables.spacingComponent3,
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_credit_card,
                        contentDescription = "Wallet"
                    ),
                    tint = scheme.basicAccent.getColor(),
                    modifier = Modifier.size(ODSVariables.sizingComponent8)
                )
                ODSText(
                    text = "Zona Wallet",
                    style = ODSTextStyles.bodyMBold, // 16sp
                    color = scheme.basicText
                )
            }

            ODSText(
                text = "Refreshes weekly",
                style = ODSTextStyles.microcopyRegular, // 12sp
                color = scheme.basicTextRecessive
            )
        }

        // ── 2. Balance & Credits Metrics ─────────────────────────────────────
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Available Balance
            ODSColumn(
                gap = ODSVariables.spacingComponent1,
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                ODSText(
                    text = "AVAILABLE BALANCE",
                    style = ODSTextStyles.microcopyBold, // 12sp
                    color = scheme.basicTextRecessive
                )
                ODSText(
                    text = balanceFormatted,
                    style = ODSTextStyles.bodyMBold, // 16sp
                    color = scheme.basicText
                )
            }

            // Zona Credits
            ODSColumn(
                gap = ODSVariables.spacingComponent1,
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Top
            ) {
                ODSText(
                    text = "ZONA CREDITS",
                    style = ODSTextStyles.microcopyBold, // 12sp
                    color = scheme.basicTextRecessive
                )
                ODSRow(
                    gap = ODSVariables.spacingComponent2,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    ODSText(
                        text = "$creditsCount",
                        style = ODSTextStyles.bodyMBold, // 16sp
                        color = scheme.basicText
                    )
                    ODSText(
                        text = "pts",
                        style = ODSTextStyles.microcopyRegular, // 12sp
                        color = scheme.basicTextRecessive
                    )
                }
            }
        }

        // ── 3. Action Buttons: [ Add Funds ] [ Transaction history ] ─────────
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent4,
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            // "Add Funds" (Soft Pink Button)
            ODSRow(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onAddFundsClick
                    ),
                gap = ODSVariables.spacingComponent3,
                padding = ODSPadding(all = ODSVariables.spacingComponent4),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_plus,
                        contentDescription = "Add"
                    ),
                    tint = scheme.basicAccent.getColor(),
                    modifier = Modifier.size(ODSVariables.sizingComponent7)
                )
                ODSText(
                    text = "Add Funds",
                    style = ODSTextStyles.bodySBold, // 14sp
                    color = scheme.basicAccent
                )
            }

            // "Transaction history" (Card Outline Button)
            ODSRow(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onTransactionsClick
                    ),
                gap = ODSVariables.spacingComponent3,
                padding = ODSPadding(all = ODSVariables.spacingComponent4),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                border = ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
            ) {
                ODSText(
                    text = "Transactions",
                    style = ODSTextStyles.bodySBold, // 14sp
                    color = scheme.basicText
                )
            }
        }
    }
}
