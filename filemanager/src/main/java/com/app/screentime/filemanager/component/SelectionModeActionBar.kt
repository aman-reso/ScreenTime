package com.app.screentime.filemanager.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.extensions.onClick
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun SelectionModeActionBar(
    selectedCount: Int,
    totalCount: Int,
    scheme: ODSTheme,
    onSelectAll: () -> Unit,
    onClearSelection: () -> Unit,
    onDeleteSelected: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = DSVariables.spacingComponent4,
                vertical = DSVariables.spacingComponent2
            ),
        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
        padding = ODSPadding(all = DSVariables.spacingComponent3)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSText(
                text = "$selectedCount selected",
                style = DSTextStyles.bodyMBold,
                color = scheme.basicText
            )

            ODSRow(
                horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedCount < totalCount) {
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = stringResource(R.string.select_all),
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = onSelectAll
                    )
                } else {
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = stringResource(R.string.clear),
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = onClearSelection
                    )
                }

                // Select icon button (replaces Delete)
                ODSBox(
                    modifier = Modifier
                        .size(40.dp)
                        .onClick { onDeleteSelected() },
                    background = listOf(ODSColorModel(scheme.basicAccent)),
                    cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = Icons.Outlined.CheckCircle,
                            tint = scheme.basicTextOnAccent,
                            contentDescription = "Select"
                        ),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Cross icon button (replaces Cancel)
                ODSBox(
                    modifier = Modifier
                        .size(40.dp)
                        .onClick { onCancel() },
                    background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
                    cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = Icons.Outlined.Close,
                            tint = scheme.basicText,
                            contentDescription = stringResource(R.string.cancel)
                        ),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

