package com.app.screentime.blocking.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.app.screentime.blocking.model.BlockingRule
import com.app.screentime.ui.atom.AppImageIcon

/**
 * Card component for displaying a blocking rule with app name, description, and remove action.
 * Uses ODS components for consistent styling.
 *
 * @param rule The blocking rule to display
 * @param onRemove Callback when the remove button is clicked
 * @param scheme ODS theme scheme
 */
@Composable
fun BlockingRuleCard(
    rule: BlockingRule,
    onRemove: () -> Unit,
    scheme: ODSTheme
) {
    val context = LocalContext.current

    val (packageName, title, description) = when (rule) {
        is BlockingRule.InstantBlock -> Triple(
            rule.packageName,
            rule.appName,
            "Blocked instantly"
        )

        is BlockingRule.LaunchBasedBlock -> Triple(
            rule.packageName,
            rule.appName,
            "Block after ${rule.maxLaunches} launches (Current: ${rule.currentLaunches})"
        )

        is BlockingRule.DurationBasedBlock -> Triple(
            rule.packageName,
            rule.appName,
            "Block after ${rule.maxDurationMinutes} minutes (Current: ${rule.currentDurationMinutes} min)"
        )
    }

    val appInfo = remember(packageName) {
        try {
            context.packageManager.getApplicationInfo(packageName, 0)
        } catch (e: Exception) {
            null
        }
    }

    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent3,
            vertical = DSVariables.spacingComponent4
        )
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            gap = DSVariables.spacingComponent3
        ) {
            // App Icon
            val appIconModel = AppImageIcon(appInfo = appInfo)
            if (appIconModel != null) {
                ODSImage(
                    modifier = Modifier.size(DSVariables.sizingComponent7),
                    imageModel = appIconModel,
                    cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                    contentScale = ContentScale.Fit
                )
            }

            ODSColumn(
                modifier = Modifier.weight(1f),
                gap = DSVariables.spacingComponent1
            ) {
                ODSText(
                    text = title,
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )
                ODSText(
                    text = description,
                    style = DSTextStyles.bodySRegular,
                    color = scheme.basicTextRecessive
                )
            }
            ODSButton(
                scheme = scheme,
                props = ODSButtonProps(
                    buttonIcon = ODSIconModel(
                        imageVector = Icons.Default.Delete,
                        tint = scheme.functionalDestructiveStandard,
                        contentDescription = "Remove"
                    ),
                    buttonType = ODSButtonButtonType.ICON_ONLY,
                    variant = ODSButtonVariant.GHOST,
                    size = ODSButtonSize.SMALL
                ),
                onClick = onRemove
            )
        }
    }
}

