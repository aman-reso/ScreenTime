package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
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
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Quick Settings Section on My Account Screen.
 * Conforms strictly to Figma node-id 56-3 and project design guidelines (GEMINI.md).
 *
 * Rules:
 * 1. 100% ODS components.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Max text size 16sp with Funnel Sans font.
 * 4. Padding, margins, and gaps from ODSVariables.
 * 5. Modular component breakdown.
 */

@Composable
fun QuickSettingsHeader(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSText(
        modifier = modifier,
        text = "Quick Settings",
        style = ODSTextStyles.bodyMBold,
        color = scheme.basicTextDominant
    )
}

@Composable
fun QuickSettingsRow(
    iconRes: Int,
    title: String,
    onClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    contentRight: @Composable () -> Unit
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = ODSVariables.spacingComponent2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side: Icon pill + Title
        ODSRow(
            gap = ODSVariables.spacingComponent3,
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSBox(
                modifier = Modifier.size(28.dp),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = iconRes,
                        contentDescription = title
                    ),
                    tint = scheme.basicAccent.getColor(),
                    modifier = Modifier.size(16.dp)
                )
            }

            ODSText(
                text = title,
                style = ODSTextStyles.microcopyBold,
                color = scheme.basicText
            )
        }

        // Right side: Value / Badge + Chevron Right
        ODSRow(
            gap = ODSVariables.spacingComponent3,
            horizontalAlignment = Alignment.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            contentRight()

            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_chevron_right,
                    contentDescription = "Navigate"
                ),
                tint = scheme.basicTextRecessive.getColor(),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun QuickSettingsDivider(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp),
        background = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
    )
}

@Composable
fun QuickSettingsCard(
    themeName: String,
    languageName: String,
    visibilityStatus: String,
    onThemeClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onVisibilityClick: () -> Unit,
    onControlAccountClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier
            .fillMaxWidth(),
        gap = ODSVariables.spacingComponent3,
        padding = ODSPadding(all = ODSVariables.spacingComponent4),
        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
        border = ODSBorder(
            width = ODSVariables.strokes1,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        ),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
    ) {
        // ── 1. Theme Row ───────────────────────────────────────────────────
        QuickSettingsRow(
            iconRes = R.drawable.ic_moon,
            title = "Theme",
            onClick = onThemeClick,
            scheme = scheme
        ) {
            ODSText(
                text = themeName,
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
        }

        QuickSettingsDivider(scheme = scheme)

        // ── 2. Language Row ────────────────────────────────────────────────
        QuickSettingsRow(
            iconRes = R.drawable.ic_globe,
            title = "Language",
            onClick = onLanguageClick,
            scheme = scheme
        ) {
            ODSText(
                text = languageName,
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
        }

        QuickSettingsDivider(scheme = scheme)

        QuickSettingsRow(
            iconRes = R.drawable.ic_eye,
            title = "Profile visibility",
            onClick = onVisibilityClick,
            scheme = scheme
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
                verticalAlignment = Alignment.CenterVertically,
                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
            ) {
                ODSText(
                    text = visibilityStatus,
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
            }
        }

        QuickSettingsDivider(scheme = scheme)

        // ── 4. Control Account Row ─────────────────────────────────────────
        QuickSettingsRow(
            iconRes = R.drawable.ic_settings,
            title = "Control Account",
            onClick = onControlAccountClick,
            scheme = scheme
        ) {
            ODSText(
                text = "Manage",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicAccent
            )
        }
    }
}

/**
 * Complete Quick Settings Section.
 */
@Composable
fun AccountQuickSettings(
    themeName: String,
    languageName: String,
    visibilityStatus: String,
    onThemeClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onVisibilityClick: () -> Unit,
    onControlAccountClick: () -> Unit = {},
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ODSVariables.spacingLayout1),
        gap = ODSVariables.spacingComponent4
    ) {
        QuickSettingsHeader(scheme = scheme)
        QuickSettingsCard(
            themeName = themeName,
            languageName = languageName,
            visibilityStatus = visibilityStatus,
            onThemeClick = onThemeClick,
            onLanguageClick = onLanguageClick,
            onVisibilityClick = onVisibilityClick,
            onControlAccountClick = onControlAccountClick,
            scheme = scheme
        )
    }
}
