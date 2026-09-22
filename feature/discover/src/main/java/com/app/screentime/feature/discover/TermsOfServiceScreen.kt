package com.app.screentime.feature.discover

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.telekom.odsystem.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Terms of Service Screen ("Legal Energy").
 * Matches Left Screen in media_1789897352267.png.
 * 100% constructed with Telekom ODS components.
 */
@Composable
fun TermsOfServiceScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme
) {
    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = ZonaColors.CanvasDark))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            gap = 20.dp
        ) {
            // ── 1. Top Bar: [Back] -- Legal Energy ───────────────────────────
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                gap = 14.dp
            ) {
                ODSBox(
                    modifier = Modifier
                        .size(44.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onBack
                        ),
                    background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                    cornerRadius = ODSCorners(all = 22.dp),
                    border = ODSBorder(
                        width = 1.dp,
                        colorList = listOf(ODSColorModel(hexColor = ZonaColors.Border))
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_arrow_left,
                            contentDescription = "Back"
                        ),
                        tint = ZonaColors.TextPrimary.getColor(),
                        modifier = Modifier.size(20.dp)
                    )
                }

                ODSText(
                    text = "Legal Energy",
                    style = ODSTextStyles.titleM,
                    color = ZonaColors.TextPrimary
                )
            }

            // ── 2. Display Title & Last Updated ──────────────────────────────
            ODSColumn(gap = 6.dp) {
                ODSText(
                    text = "Terms of\nService",
                    style = ODSTextStyles.bodyL,
                    color = ZonaColors.ActiveLime
                )

                ODSText(
                    text = "Last updated: February 2026",
                    style = ODSTextStyles.bodySRegular,
                    color = ZonaColors.LavenderAlt
                )
            }

            // Thin Divider Line
            ODSBox(
                modifier = Modifier.fillMaxWidth(),
                height = 1.dp,
                background = listOf(ODSColorModel(hexColor = ZonaColors.Border))
            )

            // ── 3. Four Legal Sections ───────────────────────────────────────
            LegalSection(
                number = "1. Acceptable Behavior",
                content = "ZONA is a space for radical authenticity, louder self-expression, and safety. Harassment, hate speech, bullying, and predatory behavior will result in an immediate and irreversible ban."
            )

            LegalSection(
                number = "2. Profile Guidelines",
                content = "All uploaded photos must represent your real identity. Group shots are great, but your first photo must clearly show your face. Catfishing is totally against our core energy."
            )

            LegalSection(
                number = "3. User Data & Safety",
                content = "We safeguard your location and matches. Your precise coordinates are never shared—only relative distances used to match you in real-time."
            )

            LegalSection(
                number = "4. Intellectual Property",
                content = "By posting on ZONA, you grant us the right to display your public profile assets to matches. You retain ownership of all copyright on your media assets.",
                isBold = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ── 4. Bottom Verified Badge Card ────────────────────────────────
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                cornerRadius = ODSCorners(all = 16.dp),
                border = ODSBorder(
                    width = 1.5.dp,
                    colorList = listOf(ODSColorModel(hexColor = ZonaColors.ActiveLime))
                ),
                padding = ODSPadding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 12.dp
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = R.drawable.ic_shield_check),
                        tint = ZonaColors.ActiveLime.getColor(),
                        modifier = Modifier.size(22.dp)
                    )
                    ODSText(
                        text = "Your safety is our priority. Every profile is verified.",
                        style = ODSTextStyles.bodySBold,
                        color = ZonaColors.TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun LegalSection(
    number: String,
    content: String,
    isBold: Boolean = false
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        gap = 8.dp
    ) {
        ODSText(
            text = number,
            style = ODSTextStyles.bodyMBold,
            color = ZonaColors.ActionPrimary
        )
        ODSText(
            text = content,
            style = if (isBold) ODSTextStyles.bodyMBold else ODSTextStyles.bodySRegular,
            color = if (isBold) ZonaColors.TextPrimary else ZonaColors.LavenderAlt
        )
    }
}
