package com.app.screentime.feature.wallet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.app.screentime.core.network.dto.WalletPackDto
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun PackageCard(
    pack: WalletPackDto,
    isSelected: Boolean,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) scheme.basicAccent else scheme.basicStrokeSubtle
    val cardBackground = if (isSelected) scheme.basicBackgroundCard else scheme.basicBackgroundCardSubtle

    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        background = listOf(ODSColorModel(hexColor = cardBackground)),
        cornerRadius = ODSCorners(all = 16.dp),
        border = ODSBorder(
            width = if (isSelected) 2.dp else 1.dp,
            colorList = listOf(ODSColorModel(hexColor = borderColor))
        ),
        padding = ODSPadding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 10.dp
        ) {
            // ── Top Header Row ─────────────────────────────────────────────
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 12.dp
                ) {
                    // Coin / Credit Icon in Accent Circle
                    ODSBox(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.coin_icon),
                            tint = scheme.basicBackground.getColor(),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    ODSColumn(gap = 2.dp) {
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 8.dp
                        ) {
                            ODSText(
                                text = pack.effectiveName,
                                style = ODSTextStyles.bodyMBold,
                                color = scheme.basicText
                            )

                            if (pack.isPopularPack) {
                                ODSBox(
                                    background = listOf(ODSColorModel(hexColor = HexColor("#FF4365"))),
                                    cornerRadius = ODSCorners(all = 6.dp),
                                    padding = ODSPadding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    ODSText(
                                        text = stringResource(R.string.wallet_popular),
                                        style = ODSTextStyles.microcopyBold,
                                        color = HexColor("#FFFFFF")
                                    )
                                }
                            }
                        }

                        // Credits & minutes summary
                        val minutesText = if (pack.voice_minutes > 0 || pack.video_minutes > 0) {
                            "${pack.effectiveCredits} Credits • ${pack.voice_minutes}m Voice / ${pack.video_minutes}m Video"
                        } else {
                            stringResource(R.string.wallet_coins_count, pack.effectiveCredits)
                        }

                        ODSText(
                            text = minutesText,
                            style = ODSTextStyles.bodySRegular,
                            color = scheme.basicAccent
                        )
                    }
                }

                // Price and Selection Pill
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 10.dp
                ) {
                    ODSText(
                        text = "₹${pack.effectivePrice.toInt()}",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )

                    // Selection Circle Indicator
                    if (isSelected) {
                        ODSBox(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_check),
                                tint = scheme.basicBackground.getColor(),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else {
                        ODSBox(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                            border = ODSBorder(
                                width = 1.dp,
                                colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                            )
                        ) {}
                    }
                }
            }

            // ── Bulleted Benefits (Show when benefits list is available) ────
            if (pack.benefits.isNotEmpty()) {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 54.dp, top = 2.dp),
                    gap = 4.dp
                ) {
                    pack.benefits.forEach { benefit ->
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 6.dp
                        ) {
                            ODSText(
                                text = "•",
                                style = ODSTextStyles.microcopyBold,
                                color = scheme.basicAccent
                            )
                            ODSText(
                                text = benefit,
                                style = ODSTextStyles.microcopyRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    }
                }
            } else if (pack.description.isNotBlank()) {
                ODSText(
                    text = pack.description,
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive,
                    modifier = Modifier.padding(start = 54.dp)
                )
            }
        }
    }
}
