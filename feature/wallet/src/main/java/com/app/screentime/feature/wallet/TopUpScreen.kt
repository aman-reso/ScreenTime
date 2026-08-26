package com.app.screentime.feature.wallet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.TopUpPackage
import com.app.screentime.core.ui.components.PompiereTitle
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.*

private val topUpPackages = listOf(
    TopUpPackage("p1", 100, 49, 0, false),
    TopUpPackage("p2", 500, 199, 50, false),
    TopUpPackage("p3", 1000, 349, 150, true),
    TopUpPackage("p4", 2500, 799, 500, false),
    TopUpPackage("p5", 5000, 1499, 1000, false)
)

@Composable
fun TopUpScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onBackClick: () -> Unit = {},
    onTopUpSuccess: () -> Unit = {},
    viewModel: WalletViewModel = hiltViewModel()
) {
    var selectedPackage by remember { mutableStateOf<TopUpPackage?>(topUpPackages[2]) }

    ODSColumn(
        modifier = modifier
            .fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            gap = 10.dp
        ) {
            IconButton(onClick = onBackClick) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.navigation_left_type_standard_size_standard),
                    tint = scheme.basicText.getColor()
                )
            }
            PompiereTitle(
                text = "Add Coins",
                scheme = scheme,
                style = ODSTextStyles.pompiereHeader
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                ODSBox(
                    modifier = Modifier.fillMaxWidth(),
                    background = listOf(ODSColorModel(hexColor = orchidSecondaryScheme.basicBackgroundSubtle)),
                    cornerRadius = ODSCorners(all = 16.dp),
                    padding = ODSPadding(all = 18.dp)
                ) {
                    ODSColumn(gap = 4.dp) {
                        ODSText(
                            text = "✦ Coins never expire",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = "Use for instant voice calls & private chat messages",
                            style = ODSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }

            item {
                PompiereTitle(
                    text = "Select a Coin Pack",
                    scheme = scheme,
                    style = ODSTextStyles.pompiereTitleM
                )
            }

            items(topUpPackages.chunked(2)) { row ->
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 12.dp
                ) {
                    row.forEach { pkg ->
                        PackageCard(
                            pkg = pkg,
                            isSelected = selectedPackage?.id == pkg.id,
                            scheme = scheme,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedPackage = pkg }
                        )
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }

            item {
                Spacer(Modifier.height(4.dp))
                val pkg = selectedPackage
                if (pkg != null) {
                    val totalCoins = pkg.coins + pkg.bonusCoins
                    ODSColumn(gap = 8.dp) {
                        ODSText(
                            text = "You get $totalCoins coins for ₹${pkg.priceInr}",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Pay ₹${pkg.priceInr} via Razorpay",
                                variant = ODSButtonVariant.PRIMARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = {
                                viewModel.recharge(10.0)
                            }
                        )
                    }
                }
            }

            item { Spacer(Modifier.navigationBarsPadding()) }
        }
    }
}

@Composable
private fun PackageCard(
    pkg: TopUpPackage,
    isSelected: Boolean,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ODSBox(
        modifier = modifier
            .clickable(onClick = onClick),
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
        padding = ODSPadding(all = 16.dp)
    ) {
        ODSColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (pkg.isPopular) {
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
                text = "✦ ${pkg.coins}",
                style = ODSTextStyles.pompiereTitleL,
                color = scheme.basicText
            )
            if (pkg.bonusCoins > 0) {
                ODSBox(
                    background = listOf(ODSColorModel(hexColor = macawSecondaryScheme.basicBackgroundSubtle)),
                    cornerRadius = ODSCorners(all = 8.dp),
                    padding = ODSPadding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    ODSText(
                        text = "+${pkg.bonusCoins} bonus",
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
                text = "₹${pkg.priceInr}",
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicText
            )
        }
    }
}
