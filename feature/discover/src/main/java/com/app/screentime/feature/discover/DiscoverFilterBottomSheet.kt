package com.app.screentime.feature.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeader
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderSize
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverFilterBottomSheet(
    scheme: ODSTheme,
    selectedTab: String,
    selectedFilter: String,
    onTabSelected: (String) -> Unit,
    onFilterSelected: (String) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    ODSBottomSheet(
        scheme = scheme,
        showBottomSheet = true,
        props = ODSBottomSheetProps(showHandle = true),
        onDismissRequest = onDismiss,
        onCloseClicked = onDismiss,
        titleSlot = {
            ODSBottomSheetHeader(
                scheme = scheme,
                props = ODSBottomSheetHeaderProps(
                    largeHeading = "Filter Creators",
                    subtitle = "Customize models discovery",
                    size = ODSBottomSheetHeaderSize.LARGE
                )
            )
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                gap = 16.dp
            ) {
                // Connection Mode
                ODSColumn(gap = 8.dp) {
                    ODSText(
                        text = "Connection Mode",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicText
                    )
                    ODSRow(modifier = Modifier.fillMaxWidth(), gap = 8.dp) {
                        listOf("Discovery", "Matched").forEach { tab ->
                            val isSelected = selectedTab == tab
                            ODSBox(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onTabSelected(tab) },
                                background = listOf(
                                    ODSColorModel(
                                        hexColor = if (isSelected) scheme.basicAccent else scheme.basicBackground
                                    )
                                ),
                                cornerRadius = ODSCorners(all = 12.dp),
                                border = ODSBorder(
                                    width = 1.dp,
                                    colorList = listOf(ODSColorModel(hexColor = if (isSelected) scheme.basicAccent else scheme.basicStrokeSubtle))
                                ),
                                padding = ODSPadding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSText(
                                    text = if (tab == "Discovery") "🌟 Discovery Feed" else "💖 Matched & Favorites",
                                    style = ODSTextStyles.bodySBold,
                                    color = if (isSelected) scheme.basicTextOnAccent else scheme.basicText
                                )
                            }
                        }
                    }
                }

                // Categories & Status
                ODSColumn(gap = 8.dp) {
                    ODSText(
                        text = "Categories & Status",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicText
                    )
                    val categories = listOf("All", "New ✨", "Online Now 🟢", "Top Rated ⭐")
                    ODSWrap(horizontalGap = 8.dp, verticalGap = 8.dp) {
                        categories.forEach { category ->
                            val isSelected = selectedFilter.startsWith(category.take(3)) || selectedFilter == category
                            ODSBox(
                                modifier = Modifier.clickable { onFilterSelected(category) },
                                background = listOf(
                                    ODSColorModel(
                                        hexColor = if (isSelected) scheme.basicAccent else scheme.basicBackground
                                    )
                                ),
                                cornerRadius = ODSCorners(all = 12.dp),
                                border = ODSBorder(
                                    width = 1.dp,
                                    colorList = listOf(ODSColorModel(hexColor = if (isSelected) scheme.basicAccent else scheme.basicStrokeSubtle))
                                ),
                                padding = ODSPadding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                ODSText(
                                    text = category,
                                    style = if (isSelected) ODSTextStyles.bodySBold else ODSTextStyles.bodySRegular,
                                    color = if (isSelected) scheme.basicTextOnAccent else scheme.basicText
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        },
        actionSlot = {
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                gap = 12.dp
            ) {
                ODSButton(
                    modifier = Modifier.weight(1f),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Reset",
                        variant = ODSButtonVariant.GHOST,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onReset
                )
                ODSButton(
                    modifier = Modifier.weight(1.5f),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Apply Filters",
                        variant = ODSButtonVariant.PRIMARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onApply
                )
            }
        }
    )
}
