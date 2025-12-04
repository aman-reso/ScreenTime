package com.app.screentime.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardchoicesimple.ODSCardChoiceSimple
import com.telekom.odsystem.organisms.cardchoicesimple.ODSCardChoiceSimpleProps
import com.telekom.odsystem.organisms.cardchoicesimple.ODSCardChoiceSimpleType

@Preview(showBackground = true)
@Composable
fun ODSCardChoiceSimplePreview() {
    ODSBox(
        modifier = Modifier,
        background = listOf(ODSColorModel(neutralScheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(DSVariables.spacingComponent5),
            gap = DSVariables.spacingComponent4
        ) {
            // Basic Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Basic Examples",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )

                // Filled, Unselected
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.FILLED,
                        selected = false
                    ),
                    onClick = {}
                )

                // Filled, Selected
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.FILLED,
                        selected = true
                    ),
                    onClick = {}
                )

                // Outline, Unselected
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.OUTLINE,
                        selected = false
                    ),
                    onClick = {}
                )

                // Outline, Selected
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.OUTLINE,
                        selected = true
                    ),
                    onClick = {}
                )
            }

            // With Labels
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "With Labels",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )

                // With Label Top
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.FILLED,
                        selected = false,
                        labelTop = "Label Top"
                    ),
                    onClick = {}
                )

                // With Label Bottom
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.FILLED,
                        selected = false,
                        labelBottom = "Label Bottom"
                    ),
                    onClick = {}
                )

                // With Heading
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.FILLED,
                        selected = false,
                        heading = "Card Heading"
                    ),
                    onClick = {}
                )

                // With All Labels
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.FILLED,
                        selected = true,
                        heading = "Complete Card",
                        labelTop = "Top Label",
                        labelBottom = "Bottom Label"
                    ),
                    onClick = {}
                )
            }

            // With Right Content Slot
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "With Right Content Slot",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )

                // With Icon
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.FILLED,
                        selected = false,
                        labelTop = "Card with Icon"
                    ),
                    rightContentSlot = {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = Icons.Default.Star,
                                tint = neutralScheme.basicAccent,
                                contentDescription = "Star"
                            ),
                            width = DSVariables.sizingComponent7,
                            height = DSVariables.sizingComponent7
                        )
                    },
                    onClick = {}
                )

                // With Text
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.FILLED,
                        selected = false,
                        labelTop = "Card with Text"
                    ),
                    rightContentSlot = {
                        ODSText(
                            text = "Info",
                            style = DSTextStyles.bodySRegular,
                            color = neutralScheme.basicTextRecessive
                        )
                    },
                    onClick = {}
                )

                // With Multiple Elements
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.FILLED,
                        selected = true,
                        labelTop = "Card with Multiple Elements"
                    ),
                    rightContentSlot = {
                        ODSRow(
                            gap = DSVariables.spacingComponent2,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            ODSText(
                                text = "3",
                                style = DSTextStyles.bodyMBold,
                                color = neutralScheme.basicText
                            )
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.Notifications,
                                    tint = neutralScheme.functionalInformationalStandard,
                                    contentDescription = "Notifications"
                                ),
                                width = DSVariables.sizingComponent6,
                                height = DSVariables.sizingComponent6
                            )
                        }
                    },
                    onClick = {}
                )
            }

            // With Bottom Content Slot
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "With Bottom Content Slot",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )

                // With Additional Text
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.FILLED,
                        selected = false,
                        labelTop = "Card with Bottom Content"
                    ),
                    bottomContentSlot = {
                        ODSText(
                            text = "This is additional content displayed at the bottom",
                            style = DSTextStyles.bodySRegular,
                            color = neutralScheme.basicTextRecessive
                        )
                    },
                    onClick = {}
                )

                // With Icon and Text
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.FILLED,
                        selected = true,
                        labelTop = "Card with Bottom Icon"
                    ),
                    bottomContentSlot = {
                        ODSRow(
                            gap = DSVariables.spacingComponent2,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.Info,
                                    tint = neutralScheme.functionalInformationalStandard,
                                    contentDescription = "Info"
                                ),
                                width = DSVariables.sizingComponent6,
                                height = DSVariables.sizingComponent6
                            )
                            ODSText(
                                text = "Additional information here",
                                style = DSTextStyles.bodySRegular,
                                color = neutralScheme.basicTextRecessive
                            )
                        }
                    },
                    onClick = {}
                )
            }

            // Interactive Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Interactive Examples",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )

                // Single Selection Group
                var selectedOption by remember { mutableStateOf(1) }
                ODSColumn(
                    gap = DSVariables.spacingComponent2
                ) {
                    ODSText(
                        text = "Select an option:",
                        style = DSTextStyles.bodyMRegular,
                        color = neutralScheme.basicText
                    )
                    ODSCardChoiceSimple(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = neutralScheme,
                        props = ODSCardChoiceSimpleProps(
                            type = ODSCardChoiceSimpleType.FILLED,
                            selected = selectedOption == 1,
                            labelTop = "Option 1"
                        ),
                        onClick = { selectedOption = 1 }
                    )
                    ODSCardChoiceSimple(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = neutralScheme,
                        props = ODSCardChoiceSimpleProps(
                            type = ODSCardChoiceSimpleType.FILLED,
                            selected = selectedOption == 2,
                            labelTop = "Option 2"
                        ),
                        onClick = { selectedOption = 2 }
                    )
                    ODSCardChoiceSimple(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = neutralScheme,
                        props = ODSCardChoiceSimpleProps(
                            type = ODSCardChoiceSimpleType.FILLED,
                            selected = selectedOption == 3,
                            labelTop = "Option 3"
                        ),
                        onClick = { selectedOption = 3 }
                    )
                }

                // Multiple Selection Group (simulated)
                var option1Selected by remember { mutableStateOf(false) }
                var option2Selected by remember { mutableStateOf(false) }
                var option3Selected by remember { mutableStateOf(false) }

                ODSColumn(
                    gap = DSVariables.spacingComponent2
                ) {
                    ODSText(
                        text = "Select multiple options:",
                        style = DSTextStyles.bodyMRegular,
                        color = neutralScheme.basicText
                    )
                    ODSCardChoiceSimple(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = neutralScheme,
                        props = ODSCardChoiceSimpleProps(
                            type = ODSCardChoiceSimpleType.OUTLINE,
                            selected = option1Selected,
                            labelTop = "Feature A"
                        ),
                        onClick = { option1Selected = !option1Selected }
                    )
                    ODSCardChoiceSimple(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = neutralScheme,
                        props = ODSCardChoiceSimpleProps(
                            type = ODSCardChoiceSimpleType.OUTLINE,
                            selected = option2Selected,
                            labelTop = "Feature B"
                        ),
                        onClick = { option2Selected = !option2Selected }
                    )
                    ODSCardChoiceSimple(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = neutralScheme,
                        props = ODSCardChoiceSimpleProps(
                            type = ODSCardChoiceSimpleType.OUTLINE,
                            selected = option3Selected,
                            labelTop = "Feature C"
                        ),
                        onClick = { option3Selected = !option3Selected }
                    )
                }
            }

            // Complex Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Complex Examples",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )

                // Full Featured Card
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.FILLED,
                        selected = true,
                        heading = "Premium Plan",
                        labelTop = "Best Value",
                        labelBottom = "$9.99/month"
                    ),
                    rightContentSlot = {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = Icons.Default.Star,
                                tint = neutralScheme.basicAccent,
                                contentDescription = "Star"
                            ),
                            width = DSVariables.sizingComponent7,
                            height = DSVariables.sizingComponent7
                        )
                    },
                    bottomContentSlot = {
                        ODSText(
                            text = "Includes all features and priority support",
                            style = DSTextStyles.bodySRegular,
                            color = neutralScheme.basicTextRecessive
                        )
                    },
                    onClick = {}
                )

                // Outline with Content
                ODSCardChoiceSimple(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardChoiceSimpleProps(
                        type = ODSCardChoiceSimpleType.OUTLINE,
                        selected = false,
                        labelTop = "Standard Plan"
                    ),
                    rightContentSlot = {
                        ODSText(
                            text = "$4.99",
                            style = DSTextStyles.bodyMBold,
                            color = neutralScheme.basicText
                        )
                    },
                    bottomContentSlot = {
                        ODSRow(
                            gap = DSVariables.spacingComponent2,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.CheckCircle,
                                    tint = neutralScheme.functionalSuccessStandard,
                                    contentDescription = "Check"
                                ),
                                width = DSVariables.sizingComponent6,
                                height = DSVariables.sizingComponent6
                            )
                            ODSText(
                                text = "Basic features included",
                                style = DSTextStyles.bodySRegular,
                                color = neutralScheme.basicTextRecessive
                            )
                        }
                    },
                    onClick = {}
                )
            }
        }
    }
}

