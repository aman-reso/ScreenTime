package com.app.screentime.feature.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.telekom.odsystem.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.screentime.core.ui.theme.ZonaColors
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

/**
 * "Before You Match..." Rules Dialog.
 * Matches Right Screen in media_1789897352267.png.
 * 100% constructed with Telekom ODS components.
 */
@Composable
fun BeforeYouMatchDialog(
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onReadTerms: () -> Unit
) {
    Dialog(
        onDismissRequest = onDecline,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ODSBox(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp),
            background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
            cornerRadius = ODSCorners(all = 26.dp),
            border = ODSBorder(
                width = 2.5.dp,
                colorList = listOf(ODSColorModel(hexColor = ZonaColors.ActiveLime))
            ),
            padding = ODSPadding(horizontal = 24.dp, vertical = 26.dp)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 18.dp
            ) {
                // ── 1. Top Pill Badge: "zona rules" ─────────────────────────
                ODSBox(
                    background = listOf(ODSColorModel(hexColor = ZonaColors.ActionPrimary)),
                    cornerRadius = ODSCorners(all = 12.dp),
                    padding = ODSPadding(horizontal = 14.dp, vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "zona rules",
                        style = ODSTextStyles.bodySBold,
                        color = ZonaColors.TextPrimary
                    )
                }

                // ── 2. Display Headline ─────────────────────────────────────
                ODSText(
                    text = "Before You\nMatch...",
                    style = ODSTextStyles.titleL,
                    color = ZonaColors.TextPrimary,
                    textAlign = TextAlign.Center
                )

                // ── 3. Subtitle / Core Energy ────────────────────────────────
                ODSText(
                    text = "We are built on absolute respect and authenticity. By tapping Accept, you promise to follow our guidelines.",
                    style = ODSTextStyles.bodySRegular,
                    color = ZonaColors.LavenderAlt,
                    textAlign = TextAlign.Center
                )

                // ── 4. Guidelines Checklist ──────────────────────────────────
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 12.dp
                ) {
                    GuidelineItem(text = "Be genuinely yourself.")
                    GuidelineItem(text = "No hate, harassment, or fake identities.")
                }

                // ── 5. Terms Link ────────────────────────────────────────────
                ODSRow(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onReadTerms
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    gap = 4.dp
                ) {
                    ODSText(
                        text = "Read our full",
                        style = ODSTextStyles.bodySRegular,
                        color = ZonaColors.LavenderAlt
                    )
                    ODSText(
                        text = "Terms of Service",
                        style = ODSTextStyles.bodySBold,
                        color = ZonaColors.ActiveLime
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ── 6. Actions: Accept & Decline ─────────────────────────────
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 10.dp
                ) {
                    // Accept Button (Neon Lime)
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onAccept
                            ),
                        height = 54.dp,
                        background = listOf(ODSColorModel(hexColor = ZonaColors.ActiveLime)),
                        cornerRadius = ODSCorners(all = 27.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = "Accept",
                            style = ODSTextStyles.bodyMBold,
                            color = ZonaColors.TextInverse
                        )
                    }

                    // Decline Button (Dark Violet Pill)
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onDecline
                            ),
                        height = 52.dp,
                        background = listOf(ODSColorModel(hexColor = ZonaColors.CanvasDark)),
                        cornerRadius = ODSCorners(all = 26.dp),
                        border = ODSBorder(
                            width = 1.dp,
                            colorList = listOf(ODSColorModel(hexColor = ZonaColors.Border))
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = "Decline",
                            style = ODSTextStyles.bodySBold,
                            color = ZonaColors.TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GuidelineItem(text: String) {
    ODSRow(
        verticalAlignment = Alignment.CenterVertically,
        gap = 10.dp
    ) {
        ODSIcon(
            iconModel = ODSIconModel(drawableRes = R.drawable.ic_check),
            tint = ZonaColors.ActiveLime.getColor(),
            modifier = Modifier.size(18.dp)
        )
        ODSText(
            text = text,
            style = ODSTextStyles.bodySRegular,
            color = ZonaColors.TextPrimary
        )
    }
}
