package com.app.screentime.blocking.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickAction
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickActionProps
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickActionSize
import com.telekom.odsystem.tokens.tokens.ODSTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle

/**
 * Permission card component for displaying permission status and allowing user interaction.
 * Uses ODSCardQuickAction for consistent styling and navigation indication.
 *
 * @param title The title of the permission
 * @param description The description explaining why the permission is needed
 * @param isGranted Whether the permission is currently granted
 * @param icon The icon to display for this permission
 * @param onClick Callback when the card is clicked
 * @param scheme ODS theme scheme
 */
@Composable
fun PermissionCard(
    title: String,
    description: String,
    isGranted: Boolean,
    icon: ODSIconModel,
    onClick: () -> Unit,
    scheme: ODSTheme
) {
    ODSCardQuickAction(
        modifier = Modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardQuickActionProps(
            size = ODSCardQuickActionSize.SMALL,
            filled = true,
            disabled = false
        ),
        onClick = onClick,
        contentSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ODSBox(
                    modifier = Modifier.size(DSVariables.sizingComponent12),
                    background = listOf(ODSColorModel(scheme.basicAccent)),
                    cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = icon.imageVector!!,
                            tint = scheme.basicTextOnAccent,
                            contentDescription = title
                        ),
                        width = DSVariables.sizingComponent8,
                        height = DSVariables.sizingComponent8
                    )
                }

                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = DSVariables.spacingComponent2
                ) {
                    ODSText(
                        text = title,
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                    ODSText(
                        text = description,
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }

                if (isGranted) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = Icons.Default.CheckCircle,
                            tint = scheme.functionalSuccessStandard,
                            contentDescription = stringResource(R.string.permission_granted)
                        ),
                        width = DSVariables.sizingComponent7,
                        height = DSVariables.sizingComponent7
                    )
                }
            }
        }
    )
}

