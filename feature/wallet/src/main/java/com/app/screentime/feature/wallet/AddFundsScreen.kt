package com.app.screentime.feature.wallet

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSElevation
import com.telekom.odsystem.foundations.ODSElevationType
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

data class CreditPackage(
    val id: String,
    val creditsCount: Int,
    val priceText: String,
    val priceAmount: Double,
    val subtitle: String = "Get priority matches & boosts",
    val isHot: Boolean = false
)

/**
 * Add Funds & Top-Up Screen (Matching Figma node-id 21-7: https://figma.com/design/1TELYpr19qLAv99DexRNpi/Untitled?node-id=21-7).
 *
 * Strictly adheres to project rules (GEMINI.md):
 * 1. 100% ODS components & ODSLazyColumn.
 * 2. Colors picked from composable's `scheme: ODSTheme`.
 * 3. Max text size is 16sp with Funnel Sans font.
 * 4. Padding, margins, and gaps mapped to ODSVariables.
 * 5. Status bar excluded as requested.
 */
@Composable
fun AddFundsScreen(
    onBack: () -> Unit,
    onPaymentSuccess: () -> Unit = onBack,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme
) {
    val context = LocalContext.current

    val packages = remember {
        listOf(
            CreditPackage(
                id = "pack_50",
                creditsCount = 50,
                priceText = "$9.99",
                priceAmount = 9.99
            ),
            CreditPackage(
                id = "pack_100",
                creditsCount = 100,
                priceText = "$18.99",
                priceAmount = 18.99,
                isHot = true
            ),
            CreditPackage(
                id = "pack_250",
                creditsCount = 250,
                priceText = "$44.99",
                priceAmount = 44.99
            )
        )
    }

    var selectedPackage by remember { mutableStateOf(packages[1]) }

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        clipContent = true,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        background = listOf(
            ODSColorModel(
                gradient = ODSLinearGradientModel(
                    colorStops = arrayOf(
                        0.00f to scheme.basicBackground,
                        0.52f to scheme.basicBackgroundSubtle,
                        1.00f to scheme.basicBackgroundCard
                    ),
                    opacity = 1.00f,
                    angleInDegrees = 180f
                )
            )
        )
    ) {
        // ── 1. Top Header Bar (No dummy status bar) ────────────────────────────
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            padding = ODSPadding(
                top = ODSVariables.spacingComponent2,
                bottom = ODSVariables.spacingComponent3,
                left = ODSVariables.spacingLayout1,
                right = ODSVariables.spacingLayout1
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSRow(
                padding = ODSPadding(all = ODSVariables.spacingComponent4),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onBack
                )
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_arrow_left,
                        contentDescription = "Back"
                    ),
                    tint = scheme.basicTextDominant.getColor(),
                    modifier = Modifier.size(20.dp)
                )
            }

            ODSText(
                text = "Add Funds",
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicTextDominant
            )

            // Symmetric balance spacer box
            ODSBox(modifier = Modifier.size(44.dp))
        }

        // ── 2. Scrollable Body Content using ODSLazyColumn (Rule #1) ───────────
        ODSLazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            gap = ODSVariables.spacingComponent4
        ) {
            // ── Item 1: Current Balance Card ───────────────────────────────────
            item {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    padding = ODSPadding(all = ODSVariables.spacingLayout1),
                    cornerRadius = ODSCorners(all = 20.dp),
                    border = ODSBorder(
                        width = 2.dp,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                ) {
                    ODSColumn(
                        gap = 4.dp,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        ODSText(
                            text = "CURRENT BALANCE",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicTextDominant
                        )
                        ODSText(
                            text = "$24.50",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicTextDominant
                        )
                    }

                    ODSColumn(
                        gap = 4.dp,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Top
                    ) {
                        ODSText(
                            text = "ZONA CREDITS",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicTextDominant
                        )
                        ODSText(
                            text = "120 pts",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicAccent
                        )
                    }
                }
            }

            // ── Item 2: Select Credit Package Section ──────────────────────────
            item {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    gap = ODSVariables.spacingComponent3,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    ODSText(
                        text = "Select Credit Package",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicTextDominant
                    )

                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = 10.dp,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        packages.forEach { pack ->
                            val isSelected = pack.id == selectedPackage.id

                            ODSRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = { selectedPackage = pack }
                                    ),
                                padding = ODSPadding(all = ODSVariables.spacingLayout1),
                                cornerRadius = ODSCorners(all = 20.dp),
                                border = ODSBorder(
                                    width = 2.dp,
                                    colorList = listOf(
                                        ODSColorModel(
                                            hexColor = if (isSelected) scheme.basicAccent else scheme.basicStroke
                                        )
                                    )
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                background = listOf(
                                    ODSColorModel(
                                        hexColor = if (isSelected) scheme.basicBackgroundCard else scheme.basicBackgroundCardSubtle
                                    )
                                )
                            ) {
                                ODSRow(
                                    gap = 12.dp,
                                    horizontalAlignment = Alignment.Start,
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    // Custom Radio Indicator Circle
                                    ODSBox(
                                        modifier = Modifier.size(20.dp),
                                        cornerRadius = ODSCorners(all = 10.dp),
                                        border = if (!isSelected) {
                                            ODSBorder(
                                                width = 2.dp,
                                                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                            )
                                        } else null,
                                        background = if (isSelected) {
                                            listOf(ODSColorModel(hexColor = scheme.basicAccent))
                                        } else emptyList(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            ODSBox(
                                                modifier = Modifier.size(8.dp),
                                                cornerRadius = ODSCorners(all = 4.dp),
                                                background = listOf(ODSColorModel(hexColor = scheme.basicTextOnAccent))
                                            )
                                        }
                                    }

                                    ODSColumn(
                                        gap = 2.dp,
                                        verticalAlignment = Alignment.Top,
                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.Top
                                    ) {
                                        ODSText(
                                            text = "${pack.creditsCount} Credits",
                                            style = ODSTextStyles.bodyMBold,
                                            color = scheme.basicTextDominant
                                        )
                                        ODSText(
                                            text = pack.subtitle,
                                            style = ODSTextStyles.microcopyRegular,
                                            color = scheme.basicTextRecessive
                                        )
                                    }
                                }

                                ODSRow(
                                    gap = 8.dp,
                                    horizontalAlignment = Alignment.End,
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    if (pack.isHot) {
                                        ODSRow(
                                            padding = ODSPadding(
                                                top = 4.dp,
                                                bottom = 4.dp,
                                                left = 8.dp,
                                                right = 8.dp
                                            ),
                                            cornerRadius = ODSCorners(all = 8.dp),
                                            horizontalAlignment = Alignment.Start,
                                            verticalAlignment = Alignment.Top,
                                            horizontalArrangement = Arrangement.Start,
                                            background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                                        ) {
                                            ODSText(
                                                text = "HOT",
                                                style = ODSTextStyles.microcopyBold,
                                                color = scheme.basicTextOnAccent
                                            )
                                        }
                                    }

                                    ODSText(
                                        text = pack.priceText,
                                        style = ODSTextStyles.bodyMBold,
                                        color = scheme.basicTextDominant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── Item 3: Payment Method Section ─────────────────────────────────
            item {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    gap = ODSVariables.spacingComponent3,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    ODSText(
                        text = "Payment Method",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicTextDominant
                    )

                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        padding = ODSPadding(all = ODSVariables.spacingLayout1),
                        cornerRadius = ODSCorners(all = 16.dp),
                        border = ODSBorder(
                            width = 1.dp,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                    ) {
                        ODSRow(
                            gap = 12.dp,
                            horizontalAlignment = Alignment.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // VISA badge container
                            ODSRow(
                                cornerRadius = ODSCorners(all = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                                width = 40.dp,
                                height = 24.dp
                            ) {
                                ODSText(
                                    text = "VISA",
                                    style = ODSTextStyles.microcopyBold,
                                    color = scheme.basicTextDominant
                                )
                            }

                            ODSColumn(
                                gap = 2.dp,
                                verticalAlignment = Alignment.Top,
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Top
                            ) {
                                ODSText(
                                    text = "Visa ending in •••• 4821",
                                    style = ODSTextStyles.bodySBold,
                                    color = scheme.basicTextDominant
                                )
                                ODSText(
                                    text = "Expires 09/28",
                                    style = ODSTextStyles.microcopyRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }

                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_edit_3,
                                contentDescription = "Edit Card"
                            ),
                            tint = scheme.basicTextRecessive.getColor(),
                            modifier = Modifier
                                .size(18.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        Toast.makeText(context, "Payment method selection", Toast.LENGTH_SHORT).show()
                                    }
                                )
                        )
                    }

                    // SSL Secured note
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        gap = 8.dp,
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_shield_check,
                                contentDescription = "Secured"
                            ),
                            tint = scheme.basicTextDominant.getColor(),
                            modifier = Modifier.size(16.dp)
                        )
                        ODSText(
                            text = "SSL Secured payment. Your credentials are encrypted safely.",
                            style = ODSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }

            // Bottom clearance spacer
            item {
                Spacer(modifier = Modifier.height(ODSVariables.spacingComponent5))
            }
        }

        // ── 3. Sticky Bottom CTA & Home Indicator ──────────────────────────────
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    horizontal = ODSVariables.spacingLayout1,
                    vertical = ODSVariables.spacingComponent3
                ),
            gap = ODSVariables.spacingComponent3,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            Toast.makeText(
                                context,
                                "Successfully purchased ${selectedPackage.creditsCount} credits!",
                                Toast.LENGTH_LONG
                            ).show()
                            onPaymentSuccess()
                        }
                    ),
                padding = ODSPadding(all = ODSVariables.spacingLayout1),
                cornerRadius = ODSCorners(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                effect = ODSEffect(
                    elevations = listOf(
                        ODSElevation(
                            x = 0,
                            y = 6,
                            blur = 14,
                            spread = 0,
                            color = HexColor(0x38D81B60),
                            type = ODSElevationType.DROP_SHADOW
                        )
                    )
                )
            ) {
                ODSText(
                    text = "Pay ${selectedPackage.priceText} Securely",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextOnAccent
                )
            }

            // Home indicator bar
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = ODSVariables.spacingComponent4, bottom = ODSVariables.spacingComponent2),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                ODSBox(
                    modifier = Modifier.size(width = 134.dp, height = 4.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicStroke)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull)
                )
            }
        }
    }
}
