package com.app.screentime.feature.chat.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.screentime.core.network.dto.WalletPackDto
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ChatPaywallDialog(
    packs: List<WalletPackDto>,
    isVerifying: Boolean,
    errorMessage: String?,
    scheme: ODSTheme,
    onBuyClick: (WalletPackDto) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.BottomCenter
        ) {
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = false) {}
                    .padding(horizontal = ODSVariables.spacingLayout1)
                    .padding(bottom = ODSVariables.spacingLayout2),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = 24.dp),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                ),
                padding = ODSPadding(
                    horizontal = ODSVariables.spacingComponent4,
                    vertical = ODSVariables.spacingComponent4
                )
            ) {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3
                ) {
                    // Header Row
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            gap = ODSVariables.spacingComponent2
                        ) {
                            ODSBox(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSText(
                                    text = "💎",
                                    style = ODSTextStyles.bodyMBold,
                                    color = scheme.basicTextDominant
                                )
                            }
                            ODSColumn(gap = 2.dp) {
                                ODSText(
                                    text = "Unlock Chat Messages",
                                    style = ODSTextStyles.bodyMBold,
                                    color = scheme.basicText
                                )
                                ODSText(
                                    text = "Instant In-App Purchase via Google Play",
                                    style = ODSTextStyles.microcopyRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }

                        // Close button
                        ODSBox(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .clickable { onDismiss() },
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle)),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = "✕",
                                style = ODSTextStyles.bodySRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    }

                    if (isVerifying) {
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = ODSVariables.spacingComponent3),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSColumn(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                gap = ODSVariables.spacingComponent2
                            ) {
                                ODSLoadingSpinner(
                                    scheme = scheme,
                                    props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.SMALL)
                                )
                                ODSText(
                                    text = "Processing Google Play Purchase...",
                                    style = ODSTextStyles.bodySRegular,
                                    color = scheme.basicText
                                )
                            }
                        }
                    }

                    if (!errorMessage.isNullOrBlank()) {
                        ODSBox(
                            modifier = Modifier.fillMaxWidth(),
                            background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveSubtle)),
                            cornerRadius = ODSCorners(all = 8.dp),
                            padding = ODSPadding(
                                horizontal = ODSVariables.spacingComponent3,
                                vertical = ODSVariables.spacingComponent2
                            )
                        ) {
                            ODSText(
                                text = errorMessage,
                                style = ODSTextStyles.microcopyRegular,
                                color = scheme.functionalDestructiveStandard
                            )
                        }
                    }

                    // Packages List
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(packs, key = { it.id }) { pack ->
                            ChatPaywallPackItem(
                                pack = pack,
                                scheme = scheme,
                                onBuyClick = { onBuyClick(pack) }
                            )
                        }
                    }

                    ODSText(
                        text = "🔒 Secured by Google Play Billing • Instant credit & no ads",
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatPaywallPackItem(
    pack: WalletPackDto,
    scheme: ODSTheme,
    onBuyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPopular = pack.isPopularPack
    val borderColor = if (isPopular) scheme.basicAccent else scheme.basicStrokeSubtle

    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onBuyClick() },
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle)),
        cornerRadius = ODSCorners(all = 14.dp),
        border = ODSBorder(
            width = if (isPopular) 1.5.dp else 1.dp,
            colorList = listOf(ODSColorModel(hexColor = borderColor))
        ),
        padding = ODSPadding(
            horizontal = ODSVariables.spacingComponent3,
            vertical = ODSVariables.spacingComponent3
        )
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                gap = ODSVariables.spacingComponent3,
                modifier = Modifier.weight(1f)
            ) {
                ODSBox(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    background = listOf(ODSColorModel(hexColor = if (isPopular) scheme.basicAccent else scheme.basicBackgroundCard)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = if (isPopular) "🔥" else "💬",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                }

                ODSColumn(gap = 2.dp) {
                    ODSRow(
                        verticalAlignment = Alignment.CenterVertically,
                        gap = 6.dp
                    ) {
                        ODSText(
                            text = pack.effectiveName,
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicText
                        )
                        if (isPopular) {
                            ODSBox(
                                background = listOf(ODSColorModel(hexColor = HexColor("#FF4365"))),
                                cornerRadius = ODSCorners(all = 4.dp),
                                padding = ODSPadding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                ODSText(
                                    text = "POPULAR",
                                    style = ODSTextStyles.microcopyBold,
                                    color = HexColor("#FFFFFF")
                                )
                            }
                        }
                    }
                    ODSText(
                        text = if (pack.description.isNotBlank()) pack.description else "${pack.effectiveCredits} message credits",
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }

            // Buy Button
            val priceStr = "₹${pack.effectivePrice.toInt()}"
            ODSBox(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onBuyClick() },
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                padding = ODSPadding(
                    horizontal = ODSVariables.spacingComponent3,
                    vertical = ODSVariables.spacingComponent2
                )
            ) {
                ODSText(
                    text = priceStr,
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicTextDominant
                )
            }
        }
    }
}
