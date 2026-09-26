package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.AppThemeManager
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Theme Selection Dialog (Figma node-id 52-63).
 *
 * Allows users to choose between:
 * 1. "System default" - automatically follows system appearance.
 * 2. "Light" - crisp light background with warm, romantic tones.
 * 3. "Dark" - mysterious dark surfaces ideal for low-light browsing.
 *
 * Strict ODS Rules:
 * 1. 100% ODS components + ODSDialog.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Max text size 16sp with Funnel Sans font family.
 * 4. Padding and margins backed by `ODSVariables`.
 */
@Composable
fun ThemeSelectionDialog(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    initialTheme: String = AppThemeManager.currentThemeName.collectAsState().value,
    onDismissRequest: () -> Unit = {},
    onThemeApplied: (String) -> Unit = {}
) {
    var selectedTheme by remember(initialTheme) {
        mutableStateOf(
            when (initialTheme) {
                "Light", "Light Mode" -> "Light"
                "Dark", "Dark Mode", "Dark (Onyx)" -> "Dark"
                else -> "System default"
            }
        )
    }

    ODSDialog(
        modifier = modifier,
        scheme = scheme,
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        props = ODSDialogProps(
            showCloseButton = false,
            showScrollbar = false,
            title = null,
            bodyText = null
        ),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                padding = ODSPadding(vertical = DSVariables.spacingComponent4),
                gap = ODSVariables.spacingLayout1,
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                // ── Header: Rotated "zona style" badge + "Change Theme" title ─
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Change Theme",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText,
                        textAlign = TextAlign.Center
                    )
                }

                // ── Subtitle & Option Rows ──────────────────────────────────
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent4,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Choose how Zona looks on your device.",
                        style = ODSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive,
                        textAlign = TextAlign.Center
                    )

                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = ODSVariables.spacingComponent3,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        // Option 1: System Default
                        ThemeOptionRow(
                            title = "System default",
                            subtitle = "Winter will automatically match your active system appearance settings.",
                            isSelected = selectedTheme == "System default",
                            onClick = { selectedTheme = "System default" },
                            scheme = scheme
                        )

                        // Option 2: Light
                        ThemeOptionRow(
                            title = "Light",
                            subtitle = "Crisp light background with warm, romantic tones.",
                            isSelected = selectedTheme == "Light",
                            onClick = { selectedTheme = "Light" },
                            scheme = scheme
                        )

                        // Option 3: Dark
                        ThemeOptionRow(
                            title = "Dark",
                            subtitle = "Mysterious dark surfaces ideal for low-light browsing.",
                            isSelected = selectedTheme == "Dark",
                            onClick = { selectedTheme = "Dark" },
                            scheme = scheme
                        )
                    }
                }

                // ── Actions: Apply Theme & Cancel ───────────────────────────
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Apply Theme Button
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    onThemeApplied(selectedTheme)
                                    onDismissRequest()
                                }
                            ),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                        height = 44.dp
                    ) {
                        ODSText(
                            text = "Apply Theme",
                            style = ODSTextStyles.bodySBold,
                            color = HexColor("#FFFFFF", 1.00f)
                        )
                    }

                    // Cancel Button
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onDismissRequest
                            ),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                        border = ODSBorder(
                            width = ODSVariables.strokes1,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        background = listOf(ODSColorModel(hexColor = HexColor("#000000", 0.00f))),
                        height = 44.dp
                    ) {
                        ODSText(
                            text = "Cancel",
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }
        }
    )
}

/**
 * Individual Theme Selection Option Row with custom Radio Button.
 */
@Composable
private fun ThemeOptionRow(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    scheme: ODSTheme
) {
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        gap = ODSVariables.spacingComponent4,
        padding = ODSPadding(all = ODSVariables.spacingComponent4),
        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
        border = ODSBorder(
            width = if (isSelected) 2.dp else 1.dp,
            colorList = listOf(
                ODSColorModel(
                    hexColor = if (isSelected) scheme.basicAccent else scheme.basicStrokeSubtle
                )
            )
        ),
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        background = listOf(
            ODSColorModel(
                hexColor = if (isSelected) scheme.basicAccentSecondary else HexColor(
                    "#000000",
                    0.00f
                )
            )
        )
    ) {
        // Radio Button
        ODSBox(
            modifier = Modifier.size(20.dp),
            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
            border = ODSBorder(
                width = if (isSelected) 2.dp else 1.5.dp,
                colorList = listOf(
                    ODSColorModel(
                        hexColor = if (isSelected) scheme.basicAccent else scheme.basicStrokeSubtle
                    )
                )
            ),
            contentAlignment = Alignment.Center,
            background = listOf(ODSColorModel(hexColor = HexColor("#000000", 0.00f)))
        ) {
            if (isSelected) {
                ODSBox(
                    modifier = Modifier.size(10.dp),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                )
            }
        }

        // Title and Subtitle Text Column
        ODSColumn(
            modifier = Modifier.weight(1f),
            gap = ODSVariables.spacingComponent1,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSText(
                text = title,
                style = ODSTextStyles.bodySBold,
                color = scheme.basicText
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = subtitle,
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
        }
    }
}
