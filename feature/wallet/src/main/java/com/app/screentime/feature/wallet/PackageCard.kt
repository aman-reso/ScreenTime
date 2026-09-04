package com.app.screentime.feature.wallet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.app.screentime.core.network.dto.WalletPackDto
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
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
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
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                gap = 14.dp
            ) {
                // Coin Icon in Accent Capsule
                ODSBox(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape),
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.coin_icon),
                        tint = scheme.basicBackground.getColor(),
                        modifier = Modifier.size(24.dp)
                    )
                }

                ODSColumn(gap = 2.dp) {
                    ODSRow(
                        verticalAlignment = Alignment.CenterVertically,
                        gap = 8.dp
                    ) {
                        ODSText(
                            text = stringResource(R.string.wallet_coins_count, pack.total_coins),
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                        if (pack.bonus_coins > 0) {
                            ODSBox(
                                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                                cornerRadius = ODSCorners(all = 6.dp),
                                padding = ODSPadding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                ODSText(
                                    text = stringResource(R.string.wallet_bonus_free, pack.bonus_coins),
                                    style = ODSTextStyles.microcopyBold,
                                    color = scheme.basicBackground
                                )
                            }
                        } else if (pack.is_popular) {
                            ODSBox(
                                background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard)),
                                cornerRadius = ODSCorners(all = 6.dp),
                                padding = ODSPadding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                ODSText(
                                    text = stringResource(R.string.wallet_popular),
                                    style = ODSTextStyles.microcopyBold,
                                    color = scheme.basicText
                                )
                            }
                        }
                    }

                    ODSText(
                        text = "₹${pack.price_inr.toInt()} · ₹${
                            String.format(
                                "%.2f",
                                pack.price_inr / maxOf(1, pack.total_coins)
                            )
                        }/coin",
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }

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
                        iconModel = ODSIconModel(imageVector = Icons.Default.Check),
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
}
