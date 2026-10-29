package com.app.screentime.feature.profile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.app.screentime.feature.profile.ProfileTileItemUiModel
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Pure, declarative Essentials & Lifestyle sections.
 */
@Composable
fun ProfileEssentialsSection(
    essentialItems: List<ProfileTileItemUiModel>,
    lifestyleItems: List<ProfileTileItemUiModel>,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent4
    ) {
        // ── 1. Essentials Card ────────────────────────────────────────
        if (essentialItems.isNotEmpty()) {
            ODSCardBasic(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ODSVariables.spacingLayout1),
                scheme = scheme,
                props = ODSCardBasicProps(),
                contentPadding = ODSPadding(all = ODSVariables.spacingLayout1),
                contentSlot = {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = ODSVariables.spacingComponent3
                    ) {
                        ODSText(
                            text = stringResource(R.string.profile_essentials),
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicTextDominant
                        )

                        ODSDivider(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL)
                        )

                        essentialItems.forEachIndexed { index, item ->
                            ProfileTileItem(
                                label = item.label,
                                value = item.value,
                                scheme = scheme
                            )

                            if (index < essentialItems.size - 1) {
                                ODSDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    scheme = scheme,
                                    props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL)
                                )
                            }
                        }
                    }
                }
            )
        }

        // ── 2. Lifestyle Card ─────────────────────────────────────────
        if (lifestyleItems.isNotEmpty()) {
            ODSCardBasic(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ODSVariables.spacingLayout1),
                scheme = scheme,
                props = ODSCardBasicProps(),
                contentPadding = ODSPadding(all = ODSVariables.spacingLayout1),
                contentSlot = {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = ODSVariables.spacingComponent3
                    ) {
                        ODSText(
                            text = stringResource(R.string.profile_lifestyle),
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicTextDominant
                        )

                        ODSDivider(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL)
                        )

                        lifestyleItems.forEachIndexed { index, item ->
                            ProfileTileItem(
                                label = item.label,
                                value = item.value,
                                scheme = scheme
                            )

                            if (index < lifestyleItems.size - 1) {
                                ODSDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    scheme = scheme,
                                    props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileTileItem(
    label: String,
    value: String,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent1
    ) {
        ODSText(
            text = label,
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
        ODSText(
            text = value,
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicText
        )
    }
}
