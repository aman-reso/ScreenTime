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
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.dismissiblechip.ODSDismissibleChip
import com.telekom.odsystem.atoms.dismissiblechip.ODSDismissibleChipProps
import com.telekom.odsystem.atoms.dismissiblechip.ODSDismissibleChipVariant
import com.telekom.odsystem.atoms.filterchip.ODSFilterChip
import com.telekom.odsystem.atoms.filterchip.ODSFilterChipProps
import com.telekom.odsystem.atoms.filterchip.ODSFilterChipOptions
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.togglechip.ODSToggleChip
import com.telekom.odsystem.atoms.togglechip.ODSToggleChipProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme

@Preview(showBackground = true)
@Composable
fun ODSChipPreview() {
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
            // Toggle Chip Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Toggle Chip",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )

                // Basic Toggle Chips
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent2,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Start
                ) {
                    var selected1 by remember { mutableStateOf(false) }
                    ODSToggleChip(
                        scheme = neutralScheme,
                        props = ODSToggleChipProps(
                            label = "Unselected",
                            selected = selected1
                        ),
                        onToggle = { selected1 = it }
                    )

                    var selected2 by remember { mutableStateOf(true) }
                    ODSToggleChip(
                        scheme = neutralScheme,
                        props = ODSToggleChipProps(
                            label = "Selected",
                            selected = selected2
                        ),
                        onToggle = { selected2 = it }
                    )

                    var selected3 by remember { mutableStateOf(false) }
                    ODSToggleChip(
                        scheme = neutralScheme,
                        props = ODSToggleChipProps(
                            label = "Disabled",
                            selected = selected3,
                            disabled = true
                        ),
                        onToggle = { selected3 = it }
                    )
                }

                // Toggle Chips with Icons
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent2
                ) {
                    var selected4 by remember { mutableStateOf(false) }
                    ODSToggleChip(
                        scheme = neutralScheme,
                        props = ODSToggleChipProps(
                            label = "With Icon",
                            selected = selected4,
                            icon = ODSIconModel(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star"
                            )
                        ),
                        onToggle = { selected4 = it }
                    )

                    var selected5 by remember { mutableStateOf(true) }
                    ODSToggleChip(
                        scheme = neutralScheme,
                        props = ODSToggleChipProps(
                            label = "Selected Icon",
                            selected = selected5,
                            icon = ODSIconModel(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorite"
                            )
                        ),
                        onToggle = { selected5 = it }
                    )
                }

                // Toggle Chips with Images
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent2
                ) {
                    var selected6 by remember { mutableStateOf(false) }
                    ODSToggleChip(
                        scheme = neutralScheme,
                        props = ODSToggleChipProps(
                            label = "With Image",
                            selected = selected6,
                            image = ODSImageModel(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Person"
                            ),
                            showImage = true
                        ),
                        onToggle = { selected6 = it }
                    )

                    var selected7 by remember { mutableStateOf(true) }
                    ODSToggleChip(
                        scheme = neutralScheme,
                        props = ODSToggleChipProps(
                            label = "Selected Image",
                            selected = selected7,
                            image = ODSImageModel(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Account"
                            ),
                            showImage = true
                        ),
                        onToggle = { selected7 = it }
                    )
                }

                // Icon Only Toggle Chips
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent2
                ) {
                    var selected8 by remember { mutableStateOf(false) }
                    ODSToggleChip(
                        scheme = neutralScheme,
                        props = ODSToggleChipProps(
                            label = null,
                            selected = selected8,
                            icon = ODSIconModel(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        ),
                        onToggle = { selected8 = it }
                    )

                    var selected9 by remember { mutableStateOf(true) }
                    ODSToggleChip(
                        scheme = neutralScheme,
                        props = ODSToggleChipProps(
                            label = null,
                            selected = selected9,
                            icon = ODSIconModel(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                        ),
                        onToggle = { selected9 = it }
                    )
                }
            }

            // Filter Chip Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Filter Chip",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )

                // Basic Filter Chip
                var filterExpanded1 by remember { mutableStateOf(false) }
                var selectedFilter1 by remember { mutableStateOf<ODSFilterChipOptions?>(null) }
                val filterOptions1 = listOf(
                    ODSFilterChipOptions(labelText = "Option 1"),
                    ODSFilterChipOptions(labelText = "Option 2"),
                    ODSFilterChipOptions(labelText = "Option 3")
                )

                ODSFilterChip(
                    scheme = neutralScheme,
                    props = ODSFilterChipProps(
                        label = "Filter",
                        expanded = filterExpanded1,
                        selectedValue = selectedFilter1,
                        options = filterOptions1
                    ),
                    onClick = { filterExpanded1 = !filterExpanded1 },
                    onDismissRequest = { filterExpanded1 = false },
                    selectedOption = { option ->
                        selectedFilter1 = option
                        filterExpanded1 = false
                    }
                )

                // Filter Chip with Icons
                var filterExpanded2 by remember { mutableStateOf(false) }
                var selectedFilter2 by remember { mutableStateOf<ODSFilterChipOptions?>(null) }
                val filterOptions2 = listOf(
                    ODSFilterChipOptions(
                        labelText = "Home",
                        iconBefore = ODSIconModel(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    ),
                    ODSFilterChipOptions(
                        labelText = "Work",
                        iconBefore = ODSIconModel(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Work"
                        )
                    ),
                    ODSFilterChipOptions(
                        labelText = "Personal",
                        iconBefore = ODSIconModel(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Personal"
                        )
                    )
                )

                ODSFilterChip(
                    scheme = neutralScheme,
                    props = ODSFilterChipProps(
                        label = "Filter with Icons",
                        expanded = filterExpanded2,
                        selectedValue = selectedFilter2,
                        options = filterOptions2
                    ),
                    onClick = { filterExpanded2 = !filterExpanded2 },
                    onDismissRequest = { filterExpanded2 = false },
                    selectedOption = { option ->
                        selectedFilter2 = option
                        filterExpanded2 = false
                    }
                )

                // Disabled Filter Chip
                ODSFilterChip(
                    scheme = neutralScheme,
                    props = ODSFilterChipProps(
                        label = "Disabled Filter",
                        disabled = true,
                        options = filterOptions1
                    ),
                    onClick = {}
                )
            }

            // Dismissible Chip Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Dismissible Chip",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )

                // Standard Dismissible Chips
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent2,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Start
                ) {
                    var showChip1 by remember { mutableStateOf(true) }
                    if (showChip1) {
                        ODSDismissibleChip(
                            scheme = neutralScheme,
                            props = ODSDismissibleChipProps(
                                label = "Standard",
                                variant = ODSDismissibleChipVariant.STANDARD
                            ),
                            onDismiss = { showChip1 = false }
                        )
                    }

                    var showChip2 by remember { mutableStateOf(true) }
                    if (showChip2) {
                        ODSDismissibleChip(
                            scheme = neutralScheme,
                            props = ODSDismissibleChipProps(
                                label = "With Icon",
                                variant = ODSDismissibleChipVariant.WITH_ICON,
                                icon = ODSIconModel(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Info"
                                )
                            ),
                            onDismiss = { showChip2 = false }
                        )
                    }
                }

                // Dismissible Chips with Images
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent2
                ) {
                    var showChip3 by remember { mutableStateOf(true) }
                    if (showChip3) {
                        ODSDismissibleChip(
                            scheme = neutralScheme,
                            props = ODSDismissibleChipProps(
                                label = "With Image",
                                variant = ODSDismissibleChipVariant.WITH_IMAGE,
                                image = ODSImageModel(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Account"
                                )
                            ),
                            onDismiss = { showChip3 = false }
                        )
                    }

                    var showChip4 by remember { mutableStateOf(true) }
                    if (showChip4) {
                        ODSDismissibleChip(
                            scheme = neutralScheme,
                            props = ODSDismissibleChipProps(
                                label = "Disabled",
                                variant = ODSDismissibleChipVariant.WITH_ICON,
                                icon = ODSIconModel(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Warning"
                                ),
                                disabled = true
                            ),
                            onDismiss = { showChip4 = false }
                        )
                    }
                }

                // Long Text Dismissible Chip
                var showChip5 by remember { mutableStateOf(true) }
                if (showChip5) {
                    ODSDismissibleChip(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = neutralScheme,
                        props = ODSDismissibleChipProps(
                            label = "This is a dismissible chip with a longer text label",
                            variant = ODSDismissibleChipVariant.WITH_ICON,
                            icon = ODSIconModel(
                                imageVector = Icons.Default.Label,
                                contentDescription = "Label"
                            )
                        ),
                        onDismiss = { showChip5 = false }
                    )
                }
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

                // Multiple Selection with Toggle Chips
                ODSColumn(
                    gap = DSVariables.spacingComponent2
                ) {
                    ODSText(
                        text = "Select multiple options:",
                        style = DSTextStyles.bodyMRegular,
                        color = neutralScheme.basicText
                    )
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent2
                    ) {
                        var option1 by remember { mutableStateOf(false) }
                        var option2 by remember { mutableStateOf(false) }
                        var option3 by remember { mutableStateOf(false) }

                        ODSToggleChip(
                            scheme = neutralScheme,
                            props = ODSToggleChipProps(
                                label = "Option A",
                                selected = option1
                            ),
                            onToggle = { option1 = it }
                        )

                        ODSToggleChip(
                            scheme = neutralScheme,
                            props = ODSToggleChipProps(
                                label = "Option B",
                                selected = option2
                            ),
                            onToggle = { option2 = it }
                        )

                        ODSToggleChip(
                            scheme = neutralScheme,
                            props = ODSToggleChipProps(
                                label = "Option C",
                                selected = option3
                            ),
                            onToggle = { option3 = it }
                        )
                    }
                }
            }
        }
    }
}

