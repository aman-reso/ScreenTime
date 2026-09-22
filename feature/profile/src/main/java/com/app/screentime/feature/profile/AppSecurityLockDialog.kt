package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * App Security Lock Dialog (Figma node-id 46-51).
 *
 * Rules:
 * 1. 100% ODS components.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Max text size 16sp (bodyMBold).
 * 4. Spacing and padding backed by `ODSVariables`.
 */
@Composable
fun AppSecurityLockDialog(
    initialEnabled: Boolean = false,
    scheme: ODSTheme = zonaODSTheme,
    onDismissRequest: () -> Unit = {},
    onDone: (Boolean) -> Unit = {}
) {
    var isEnabled by remember { mutableStateOf(initialEnabled) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ODSVariables.spacingLayout2),
            contentAlignment = Alignment.Center
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingLayout1,
                padding = ODSPadding(all = ODSVariables.spacingLayout1),
                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                border = ODSBorder(
                    width = ODSVariables.strokes2,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
            ) {
                // ── Header: "safety & security" tilted badge + "App Security Lock" title ──
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    ODSRow(
                        padding = ODSPadding(
                            top = ODSVariables.spacingComponent2,
                            bottom = ODSVariables.spacingComponent2,
                            left = ODSVariables.spacingComponent3,
                            right = ODSVariables.spacingComponent3
                        ),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start,
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                        rotate = -3f
                    ) {
                        ODSText(
                            text = "safety & security",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicBackground
                        )
                    }

                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = "App Security Lock",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText,
                        textAlign = TextAlign.Center
                    )
                }

                // ── Description & Toggle Card ──
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent4,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Protect your matches and secure your personal safety settings by enabling biometric lock.",
                        style = ODSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )

                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { isEnabled = !isEnabled }
                            ),
                        padding = ODSPadding(all = ODSVariables.spacingComponent4),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                    ) {
                        ODSColumn(
                            gap = ODSVariables.spacingComponent1,
                            verticalAlignment = Alignment.Top,
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            ODSText(
                                text = "Biometric Shield",
                                style = ODSTextStyles.bodySBold,
                                color = scheme.basicText
                            )
                            ODSText(
                                text = "Require lock on launch",
                                style = ODSTextStyles.microcopyRegular,
                                color = scheme.basicTextRecessive
                            )
                        }

                        // Toggle Knob
                        ODSRow(
                            padding = ODSPadding(all = ODSVariables.spacingComponent1),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                            horizontalAlignment = if (isEnabled) Alignment.End else Alignment.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = if (isEnabled) Arrangement.End else Arrangement.Start,
                            background = listOf(
                                ODSColorModel(hexColor = if (isEnabled) scheme.basicAccent else scheme.basicStroke)
                            ),
                            width = 44.dp,
                            height = 24.dp
                        ) {
                            ODSBox(
                                modifier = Modifier.size(20.dp),
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusFull)
                            )
                        }
                    }
                }

                // ── Done Action Button ──
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
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
                                onClick = { onDone(isEnabled) }
                            ),
                        gap = ODSVariables.spacingComponent3,
                        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                        height = 48.dp
                    ) {
                        ODSText(
                            text = "Done",
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicBackground
                        )
                    }
                }
            }
        }
    }
}
