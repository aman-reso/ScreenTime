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
import androidx.compose.ui.unit.dp
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
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSElevation
import com.telekom.odsystem.foundations.ODSElevationType
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Profile Visibility Dialog (Figma node-id 56-222).
 * Allows users to choose between Public and Private visibility modes using ODSDialog.
 *
 * Rules:
 * 1. 100% ODS components + ODSDialog.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Max text size 16sp with Funnel Sans font family.
 * 4. Padding, margins, gaps, and corner radii use ODSVariables.
 * 5. Clean modular component breakdown.
 */

@Composable
fun ProfileVisibilityHeader(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Profile Visibility",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Control how your profile appears to other members.",
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
    }
}

/**
 * Custom circular radio indicator for visibility options.
 */
@Composable
fun ProfileVisibilityRadio(
    isSelected: Boolean,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier.size(20.dp),
        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
        border = ODSBorder(
            width = ODSVariables.strokes2,
            colorList = listOf(
                ODSColorModel(
                    hexColor = if (isSelected) scheme.basicAccent else scheme.basicStrokeSubtle
                )
            )
        ),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            ODSBox(
                modifier = Modifier.size(10.dp),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
            )
        }
    }
}

/**
 * Single selectable visibility option card (Public / Private).
 */
@Composable
fun ProfileVisibilityOptionCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        gap = ODSVariables.spacingComponent4,
        padding = ODSPadding(all = ODSVariables.spacingLayout1),
        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
        border = ODSBorder(
            width = ODSVariables.strokes2,
            colorList = listOf(
                ODSColorModel(
                    hexColor = if (isSelected) scheme.basicAccent else scheme.basicStrokeSubtle
                )
            )
        ),
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.Top,
        background = listOf(
            ODSColorModel(
                hexColor = if (isSelected) scheme.basicAccentSecondary else scheme.basicBackgroundCard
            )
        )
    ) {
        ProfileVisibilityRadio(
            isSelected = isSelected,
            scheme = scheme,
            modifier = Modifier.padding(top = ODSVariables.spacingComponent1)
        )

        ODSColumn(
            modifier = Modifier.weight(1f),
            gap = ODSVariables.spacingComponent1,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start
        ) {
            ODSText(
                text = title,
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicTextDominant
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = description,
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )
        }
    }
}

/**
 * Action button row (Save Changes only, since ODSDialog provides close icon).
 */
@Composable
fun ProfileVisibilityActions(
    onSave: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent3,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Save Changes Button
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onSave
                ),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
            effect = ODSEffect(
                elevations = listOf(
                    ODSElevation(
                        x = 0,
                        y = 4,
                        blur = 12,
                        spread = 0,
                        color = HexColor(0x33FF4365),
                        type = ODSElevationType.DROP_SHADOW
                    )
                )
            ),
            height = 46.dp
        ) {
            ODSText(
                text = "Save Changes",
                style = ODSTextStyles.bodySBold,
                color = scheme.basicTextOnAccent
            )
        }
    }
}

/**
 * Dialog composable for Profile Visibility selection using ODSDialog with built-in close icon.
 */
@Composable
fun ProfileVisibilityDialog(
    currentVisibility: String,
    onVisibilityChanged: (String) -> Unit,
    onDismissRequest: () -> Unit,
    scheme: ODSTheme = zonaODSTheme,
    modifier: Modifier = Modifier
) {
    var selectedOption by remember(currentVisibility) {
        mutableStateOf(
            if (currentVisibility.equals(
                    "Private",
                    ignoreCase = true
                )
            ) "Private" else "Public"
        )
    }

    ODSDialog(
        modifier = modifier,
        scheme = scheme,
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        props = ODSDialogProps(
            showCloseButton = true, // Built-in ODSDialog close icon
            showScrollbar = false,
            title = null,
            bodyText = null
        ),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                padding = ODSPadding(vertical = DSVariables.spacingComponent4),
                gap = ODSVariables.spacingComponent6,
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // ── 1. Header ──────────────────────────────────────────────
                ProfileVisibilityHeader(scheme = scheme)

                // ── 2. Option Cards (Short one-liner descriptions) ─────────
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent4,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    // Public Option
                    ProfileVisibilityOptionCard(
                        title = "Public",
                        description = "Anyone can see",
                        isSelected = selectedOption == "Public",
                        onClick = { selectedOption = "Public" },
                        scheme = scheme
                    )

                    // Private Option
                    ProfileVisibilityOptionCard(
                        title = "Private",
                        description = "Only visible to your matches",
                        isSelected = selectedOption == "Private",
                        onClick = { selectedOption = "Private" },
                        scheme = scheme
                    )
                }

                ProfileVisibilityActions(
                    onSave = {
                        onVisibilityChanged(selectedOption)
                        onDismissRequest()
                    },
                    scheme = scheme
                )
            }
        }
    )
}
