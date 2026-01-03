package com.app.screentime.applock.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.screentime.applock.model.AppLockRule
import com.app.screentime.ui.atom.AppImageIcon
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
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Card component for displaying a locked app with app name and remove action.
 * Uses ODS components for consistent styling.
 */
@Composable
fun AppLockCard(
    rule: AppLockRule,
    onRemove: () -> Unit,
    scheme: ODSTheme
) {
    val context = LocalContext.current

    val appInfo = remember(rule.packageName) {
        try {
            context.packageManager.getApplicationInfo(rule.packageName, 0)
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
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = DSVariables.spacingComponent1
                ) {
                    ODSText(
                        text = rule.appName,
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicText
                    )
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = Icons.Default.Lock,
                            tint = scheme.basicTextRecessive,
                            contentDescription = "Locked"
                        ),
                        width = 16.dp,
                        height = 16.dp
                    )
                }
                ODSText(
                    text = "Protected with PIN",
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

