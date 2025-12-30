package com.app.screentime.blocking.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
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
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.organisms.cardchoicesimple.ODSCardChoiceSimple
import com.telekom.odsystem.organisms.cardchoicesimple.ODSCardChoiceSimpleProps
import com.telekom.odsystem.organisms.cardchoicesimple.ODSCardChoiceSimpleType
import com.telekom.odsystem.slots.actionslot.ODSActionSlotProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Enum representing different types of blocking rules
 */
enum class BlockType {
    INSTANT,
    LAUNCH,
    DURATION
}

/**
 * Bottom sheet for adding a new blocking rule.
 * Allows users to select blocking type (instant, launch-based, or duration-based) and configure parameters.
 *
 * @param selectedAppName The name of the app to block
 * @param selectedPackageName The package name of the app to block
 * @param onDismiss Callback when the bottom sheet is dismissed
 * @param onBlockInstantly Callback when instant block is selected
 * @param onBlockAfterLaunches Callback when launch-based block is selected with max launches count
 * @param onBlockAfterDuration Callback when duration-based block is selected with max duration in minutes
 * @param scheme ODS theme scheme
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBlockingRuleBottomSheet(
    selectedAppName: String,
    selectedPackageName: String,
    onDismiss: () -> Unit,
    onBlockInstantly: (String, String) -> Unit,
    onBlockAfterLaunches: (String, String, Int) -> Unit,
    onBlockAfterDuration: (String, String, Int) -> Unit,
    scheme: ODSTheme
) {
    var blockType by remember { mutableStateOf<BlockType?>(BlockType.INSTANT) }
    var launchCount by remember { mutableIntStateOf(3) }
    var durationMinutes by remember { mutableIntStateOf(10) }

    ODSBottomSheet(
        scheme = scheme,
        props = ODSBottomSheetProps(),
        showBottomSheet = true,
        onDismissRequest = onDismiss,
        actionSlot = {
            ODSButton(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSButtonProps(
                    label = "Add Blocking Rule",
                    disabled = blockType == null,
                    size = ODSButtonSize.SMALL,
                    variant = ODSButtonVariant.SECONDARY
                ),
                onClick = {
                    when (blockType) {
                        BlockType.INSTANT -> onBlockInstantly(
                            selectedPackageName,
                            selectedAppName
                        )

                        BlockType.LAUNCH -> onBlockAfterLaunches(
                            selectedPackageName,
                            selectedAppName,
                            launchCount
                        )

                        BlockType.DURATION -> onBlockAfterDuration(
                            selectedPackageName,
                            selectedAppName,
                            durationMinutes
                        )

                        null -> {}
                    }
                }
            )
        },
        titleSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSColumn(
                    gap = DSVariables.spacingComponent1
                ) {
                    ODSText(
                        text = "Block App",
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicText
                    )
                    ODSText(
                        text = selectedAppName,
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                padding = ODSPadding(
                    horizontal = DSVariables.spacingComponent3,
                    vertical = DSVariables.spacingComponent3
                ),
                gap = DSVariables.spacingComponent4
            ) {
                ODSText(
                    text = "BLOCKING TYPE",
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicTextRecessive
                )

                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent3
                ) {
                    ODSCardChoiceSimple(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSCardChoiceSimpleProps(
                            type = ODSCardChoiceSimpleType.FILLED,
                            selected = blockType == BlockType.INSTANT,
                            labelTop = "Block Instantly"
                        ),
                        rightContentSlot = {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.Block,
                                    tint = if (blockType == BlockType.INSTANT) {
                                        scheme.basicTextOnAccent
                                    } else {
                                        scheme.basicText
                                    },
                                    contentDescription = "Block Instantly"
                                ),
                                width = DSVariables.sizingComponent7,
                                height = DSVariables.sizingComponent7
                            )
                        },
                        onClick = { blockType = BlockType.INSTANT }
                    )

                    // Block After Launches
                    ODSCardChoiceSimple(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSCardChoiceSimpleProps(
                            type = ODSCardChoiceSimpleType.FILLED,
                            selected = blockType == BlockType.LAUNCH,
                            labelTop = "Block After Launches"
                        ),
                        rightContentSlot = {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.Refresh,
                                    tint = if (blockType == BlockType.LAUNCH) {
                                        scheme.basicTextOnAccent
                                    } else {
                                        scheme.basicText
                                    },
                                    contentDescription = "Block After Launches"
                                ),
                                width = DSVariables.sizingComponent7,
                                height = DSVariables.sizingComponent7
                            )
                        },
                        bottomContentSlot = if (blockType == BlockType.LAUNCH) {
                            {
                                ODSBox(
                                    modifier = Modifier.fillMaxWidth(),
                                    background = listOf(ODSColorModel(scheme.basicBackground)),
                                    cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
                                    padding = ODSPadding(all = DSVariables.spacingComponent5)
                                ) {
                                    ODSColumn(
                                        gap = DSVariables.spacingComponent4
                                    ) {
                                        ODSText(
                                            text = "$launchCount launches",
                                            style = DSTextStyles.bodyMRegular,
                                            color = scheme.basicTextRecessive
                                        )

                                        Slider(
                                            value = launchCount.toFloat(),
                                            onValueChange = { launchCount = it.toInt() },
                                            valueRange = 1f..20f,
                                            steps = 18,
                                            colors = SliderDefaults.colors(
                                                thumbColor = scheme.basicAccent.getColor(),
                                                activeTrackColor = scheme.basicAccent.getColor(),
                                                inactiveTrackColor = scheme.basicBackgroundSubtle.getColor()
                                            ),
                                            modifier = Modifier.padding(horizontal = DSVariables.spacingComponent1)
                                        )

                                        // Chips
                                        ODSRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            gap = DSVariables.spacingComponent3
                                        ) {
                                            listOf(1, 3, 5, 10, 15, 20).forEach { count ->
                                                val isSelected = launchCount == count
                                                ODSBox(
                                                    modifier = Modifier
                                                        .clickable { launchCount = count },
                                                    background = if (isSelected) {
                                                        listOf(ODSColorModel(scheme.basicTextOnAccent))
                                                    } else {
                                                        listOf(ODSColorModel(scheme.basicBackgroundCard))
                                                    },
                                                    cornerRadius = ODSCorners(all = 9999.dp),
                                                    padding = ODSPadding(
                                                        horizontal = DSVariables.spacingComponent3,
                                                        vertical = DSVariables.spacingComponent2
                                                    ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    ODSText(
                                                        text = "$count",
                                                        style = DSTextStyles.bodyMBold,
                                                        color = if (isSelected) {
                                                            scheme.basicAccent
                                                        } else {
                                                            scheme.basicText
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else null,
                        onClick = { blockType = BlockType.LAUNCH }
                    )

                    ODSCardChoiceSimple(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSCardChoiceSimpleProps(
                            type = ODSCardChoiceSimpleType.FILLED,
                            selected = blockType == BlockType.DURATION,
                            labelTop = "Block After Duration"
                        ),
                        rightContentSlot = {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.Timer,
                                    tint = if (blockType == BlockType.DURATION) {
                                        scheme.basicTextOnAccent
                                    } else {
                                        scheme.basicText
                                    },
                                    contentDescription = "Block After Duration"
                                ),
                                width = DSVariables.sizingComponent7,
                                height = DSVariables.sizingComponent7
                            )
                        },
                        onClick = { blockType = BlockType.DURATION }
                    )
                }
            }
        },
        onCloseClicked = onDismiss
    )
}


