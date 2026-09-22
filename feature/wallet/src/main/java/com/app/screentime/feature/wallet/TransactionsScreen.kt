package com.app.screentime.feature.wallet

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.telekom.odsystem.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.ZonaColors
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
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

data class TransactionItemData(
    val id: String,
    val title: String,
    val dateText: String,
    val amountText: String,
    val isPositive: Boolean,
    val isCredits: Boolean
)

/**
 * Transactions History Screen (Matching media_1789898350333.png Left).
 * 100% constructed with Telekom ODS components and Zona design tokens.
 */
@Composable
fun TransactionsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    scheme: ODSTheme = zonaODSTheme
) {
    if (isLoading) {
        TransactionsShimmer(modifier = modifier, scheme = scheme)
        return
    }

    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("All") }

    val todayTransactions = remember {
        listOf(
            TransactionItemData(
                id = "tx_1",
                title = "100 Credits Package",
                dateText = "Feb 24, 2026 • 2:40 PM • Completed",
                amountText = "+$18.99",
                isPositive = true,
                isCredits = false
            ),
            TransactionItemData(
                id = "tx_2",
                title = "Super Like — Sarah",
                dateText = "Feb 24, 2026 • 11:15 AM • Used",
                amountText = "-10 credits",
                isPositive = false,
                isCredits = true
            )
        )
    }

    val pastTransactions = remember {
        listOf(
            TransactionItemData(
                id = "tx_3",
                title = "Profile Boost 1-Hour",
                dateText = "Feb 20, 2026 • 8:00 PM • Used",
                amountText = "-30 credits",
                isPositive = false,
                isCredits = true
            ),
            TransactionItemData(
                id = "tx_4",
                title = "50 Credits Package",
                dateText = "Feb 20, 2026 • 3:30 PM • Completed",
                amountText = "+$9.99",
                isPositive = true,
                isCredits = false
            )
        )
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = ZonaColors.Background))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            gap = 18.dp
        ) {
            // ── 1. Top Bar: Centered Title Header with Back Chevron ───────────
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = ODSVariables.spacingComponent3),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Back Chevron Button (16.dp corner radius, 12.dp padding)
                ODSBox(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onBack
                        ),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    border = ODSBorder(
                        width = ODSVariables.strokes1,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                    ),
                    padding = ODSPadding(all = ODSVariables.spacingComponent4),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_arrow_left,
                            contentDescription = "Back"
                        ),
                        tint = scheme.basicText.getColor(),
                        modifier = Modifier.size(ODSVariables.sizingComponent8)
                    )
                }

                // Center: "Transactions" Header (16sp Funnel Sans)
                ODSText(
                    text = "Transactions",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )

                // Right: Invisible Spacer (46.dp x 46.dp) for visual centering
                ODSBox(
                    modifier = Modifier.size(46.dp),
                    opacity = 0f
                )
            }

            // ── 2. Total Balance Available Card ───────────────────────────────
            ODSBox(
                modifier = Modifier.fillMaxWidth(),
                background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                cornerRadius = ODSCorners(all = 20.dp),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(ODSColorModel(hexColor = ZonaColors.Border))
                ),
                padding = ODSPadding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    ODSColumn(gap = 6.dp) {
                        ODSText(
                            text = "TOTAL BALANCE AVAILABLE",
                            style = ODSTextStyles.microcopyBold,
                            color = ZonaColors.LavenderMuted
                        )
                        ODSText(
                            text = "$24.50",
                            style = ODSTextStyles.bodyL,
                            color = ZonaColors.TextPrimary
                        )
                    }

                    ODSText(
                        text = "120 credits",
                        style = ODSTextStyles.titleS,
                        color = ZonaColors.ActiveLime
                    )
                }
            }

            // ── 3. Filter Chips: [All] [Purchases] [Spent] ────────────────────
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = 10.dp
            ) {
                listOf("All", "Purchases", "Spent").forEach { filter ->
                    val isSelected = selectedFilter == filter
                    ODSBox(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { selectedFilter = filter }
                            ),
                        background = listOf(
                            ODSColorModel(
                                hexColor = if (isSelected) ZonaColors.ActiveLime else ZonaColors.SurfaceRaised
                            )
                        ),
                        cornerRadius = ODSCorners(all = 16.dp),
                        border = if (!isSelected) {
                            ODSBorder(
                                width = 1.dp,
                                colorList = listOf(ODSColorModel(hexColor = ZonaColors.Border))
                            )
                        } else null,
                        padding = ODSPadding(horizontal = 18.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = filter,
                            style = ODSTextStyles.bodySBold,
                            color = if (isSelected) ZonaColors.TextInverse else ZonaColors.LavenderAlt
                        )
                    }
                }
            }

            // ── 4. Section: "TODAY" ───────────────────────────────────────────
            ODSColumn(gap = 10.dp) {
                ODSText(
                    text = "TODAY",
                    style = ODSTextStyles.microcopyBold,
                    color = ZonaColors.LavenderMuted
                )

                todayTransactions
                    .filter {
                        when (selectedFilter) {
                            "Purchases" -> it.isPositive
                            "Spent" -> !it.isPositive
                            else -> true
                        }
                    }
                    .forEach { tx ->
                        TransactionRowItem(tx = tx, onReceiptClick = {
                            Toast.makeText(context, "Receipt for ${tx.title}", Toast.LENGTH_SHORT).show()
                        })
                    }
            }

            // ── 5. Section: "FEBRUARY 20, 2026" ───────────────────────────────
            ODSColumn(gap = 10.dp) {
                ODSText(
                    text = "FEBRUARY 20, 2026",
                    style = ODSTextStyles.microcopyBold,
                    color = ZonaColors.LavenderMuted
                )

                pastTransactions
                    .filter {
                        when (selectedFilter) {
                            "Purchases" -> it.isPositive
                            "Spent" -> !it.isPositive
                            else -> true
                        }
                    }
                    .forEach { tx ->
                        TransactionRowItem(tx = tx, onReceiptClick = {
                            Toast.makeText(context, "Receipt for ${tx.title}", Toast.LENGTH_SHORT).show()
                        })
                    }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TransactionRowItem(
    tx: TransactionItemData,
    onReceiptClick: () -> Unit
) {
    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
        cornerRadius = ODSCorners(all = 18.dp),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = ZonaColors.Border))
        ),
        padding = ODSPadding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                gap = 12.dp,
                modifier = Modifier.weight(1f)
            ) {
                // Direction Arrow Icon Container
                ODSBox(
                    modifier = Modifier.size(42.dp),
                    background = listOf(ODSColorModel(hexColor = ZonaColors.Surface)),
                    cornerRadius = ODSCorners(all = 12.dp),
                    border = ODSBorder(
                        width = 1.dp,
                        colorList = listOf(ODSColorModel(hexColor = ZonaColors.Border))
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = if (tx.isPositive) R.drawable.ic_arrow_up_right else R.drawable.ic_arrow_down_left,
                            contentDescription = tx.title
                        ),
                        tint = if (tx.isPositive) ZonaColors.ActiveLime.getColor() else ZonaColors.ActionPrimary.getColor(),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Title & Date Info
                ODSColumn(gap = 3.dp) {
                    ODSText(
                        text = tx.title,
                        style = ODSTextStyles.bodyMBold,
                        color = ZonaColors.TextPrimary
                    )
                    ODSText(
                        text = tx.dateText,
                        style = ODSTextStyles.microcopyRegular,
                        color = ZonaColors.LavenderMuted
                    )
                }
            }

            // Amount & Receipt
            ODSColumn(
                horizontalAlignment = Alignment.End,
                gap = 3.dp
            ) {
                ODSText(
                    text = tx.amountText,
                    style = ODSTextStyles.bodyMBold,
                    color = if (tx.isPositive) ZonaColors.ActiveLime else ZonaColors.TextPrimary
                )
                ODSText(
                    text = "Receipt",
                    style = ODSTextStyles.microcopyRegular,
                    color = ZonaColors.LavenderAlt,
                    modifier = Modifier.clickable(onClick = onReceiptClick)
                )
            }
        }
    }
}
