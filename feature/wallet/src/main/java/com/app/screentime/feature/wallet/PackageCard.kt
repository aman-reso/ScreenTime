package com.app.screentime.feature.wallet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.core.network.dto.WalletPackDto
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.*

@Composable
fun PackageCard(
    pack: WalletPackDto,
    isSelected: Boolean,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ODSBox(
        modifier = modifier.clickable(onClick = onClick),
        background = listOf(
            ODSColorModel(
                hexColor = if (isSelected) cheddarSecondaryScheme.basicBackgroundSubtle else scheme.basicBackgroundCard
            )
        ),
        cornerRadius = ODSCorners(all = 16.dp),
        border = ODSBorder(
            width = 2.dp,
            colorList = listOf(
                ODSColorModel(
                    hexColor = if (isSelected) scheme.basicAccent else scheme.basicBackgroundCard
                )
            )
        ),
        padding = ODSPadding(all = 14.dp)
    ) {
        ODSColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (pack.is_popular) {
                ODSBox(
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                    cornerRadius = ODSCorners(all = 10.dp),
                    padding = ODSPadding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    ODSText(
                        text = "POPULAR ✦",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicTextOnAccent
                    )
                }
            }
            ODSText(
                text = "✦ ${pack.coins}",
                style = ODSTextStyles.pompiereTitleL,
                color = scheme.basicText
            )
            if (pack.bonus_coins > 0) {
                ODSBox(
                    background = listOf(ODSColorModel(hexColor = macawSecondaryScheme.basicBackgroundSubtle)),
                    cornerRadius = ODSCorners(all = 8.dp),
                    padding = ODSPadding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    ODSText(
                        text = "+${pack.bonus_coins} bonus",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.functionalSuccessStandard
                    )
                }
            }
            ODSDivider(
                scheme = scheme,
                props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL)
            )
            ODSText(
                text = "₹${pack.price_inr.toInt()}",
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicText
            )
        }
    }
}
